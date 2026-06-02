package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.plato.PlatoResponse;
import com.restaurante.inventario.dto.plato.PlatoResponse.IngredienteInfo;
import com.restaurante.inventario.dto.plato.RecetaIngredienteRequest;
import com.restaurante.inventario.exception.BadRequestException;
import com.restaurante.inventario.exception.ResourceNotFoundException;
import com.restaurante.inventario.model.Plato;
import com.restaurante.inventario.model.Producto;
import com.restaurante.inventario.model.RecetaIngrediente;
import com.restaurante.inventario.repository.PlatoRepository;
import com.restaurante.inventario.repository.ProductoRepository;
import com.restaurante.inventario.repository.RecetaIngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecetaService {

    private final PlatoRepository platoRepository;
    private final ProductoRepository productoRepository;
    private final RecetaIngredienteRepository recetaRepository;

    public RecetaService(PlatoRepository platoRepository,
                         ProductoRepository productoRepository,
                         RecetaIngredienteRepository recetaRepository) {
        this.platoRepository = platoRepository;
        this.productoRepository = productoRepository;
        this.recetaRepository = recetaRepository;
    }

    public List<IngredienteInfo> listarIngredientes(Long platoId) {
        Plato plato = platoRepository.findById(platoId)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + platoId));
        return recetaRepository.findByPlatoIdOrderByProductoNombreAsc(plato.getId()).stream()
                .map(i -> new IngredienteInfo(i.getId(), i.getProducto().getNombre(),
                        i.getCantidad(), i.getProducto().getUnidad().name()))
                .toList();
    }

    @Transactional
    public IngredienteInfo agregarIngrediente(Long platoId, RecetaIngredienteRequest request) {
        Plato plato = platoRepository.findById(platoId)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + platoId));

        Producto producto = productoRepository.findByIdAndActivoTrue(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        if (recetaRepository.existsByPlatoIdAndProductoId(platoId, request.getProductoId())) {
            throw new BadRequestException("El producto ya está en la receta de este plato");
        }

        RecetaIngrediente item = new RecetaIngrediente();
        item.setPlato(plato);
        item.setProducto(producto);
        item.setCantidad(request.getCantidad());
        item = recetaRepository.save(item);

        return new IngredienteInfo(item.getId(), item.getProducto().getNombre(),
                item.getCantidad(), item.getProducto().getUnidad().name());
    }

    @Transactional
    public IngredienteInfo actualizarIngrediente(Long platoId, Long itemId, RecetaIngredienteRequest request) {
        RecetaIngrediente item = recetaRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con id: " + itemId));

        if (!item.getPlato().getId().equals(platoId)) {
            throw new BadRequestException("El ingrediente no pertenece a este plato");
        }

        item.setCantidad(request.getCantidad());
        item = recetaRepository.save(item);

        return new IngredienteInfo(item.getId(), item.getProducto().getNombre(),
                item.getCantidad(), item.getProducto().getUnidad().name());
    }

    @Transactional
    public void eliminarIngrediente(Long platoId, Long itemId) {
        RecetaIngrediente item = recetaRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con id: " + itemId));

        if (!item.getPlato().getId().equals(platoId)) {
            throw new BadRequestException("El ingrediente no pertenece a este plato");
        }

        recetaRepository.delete(item);
    }
}

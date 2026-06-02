package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.plato.PlatoRequest;
import com.restaurante.inventario.dto.plato.PlatoResponse;
import com.restaurante.inventario.dto.plato.PlatoResponse.IngredienteInfo;
import com.restaurante.inventario.exception.BadRequestException;
import com.restaurante.inventario.exception.ResourceNotFoundException;
import com.restaurante.inventario.model.Plato;
import com.restaurante.inventario.model.Producto;
import com.restaurante.inventario.model.RecetaIngrediente;
import com.restaurante.inventario.repository.PlatoRepository;
import com.restaurante.inventario.repository.RecetaIngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;
    private final RecetaIngredienteRepository recetaRepository;

    public PlatoService(PlatoRepository platoRepository, RecetaIngredienteRepository recetaRepository) {
        this.platoRepository = platoRepository;
        this.recetaRepository = recetaRepository;
    }

    public List<PlatoResponse> listarTodos() {
        return platoRepository.findAll().stream()
                .map(this::toResponseWithIngredientes)
                .toList();
    }

    public List<PlatoResponse> listarDisponibles() {
        return platoRepository.findByDisponibleTrueOrderByNombreAsc().stream()
                .map(this::toResponseWithIngredientes)
                .toList();
    }

    public PlatoResponse buscarPorId(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));
        return toResponseWithIngredientes(plato);
    }

    @Transactional
    public PlatoResponse crear(PlatoRequest request) {
        if (platoRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe un plato con el nombre: " + request.getNombre());
        }

        Plato plato = new Plato();
        plato.setNombre(request.getNombre());
        plato.setPrecioVenta(request.getPrecioVenta());
        plato.setDescripcion(request.getDescripcion());
        plato = platoRepository.save(plato);
        return toResponseWithIngredientes(plato);
    }

    @Transactional
    public PlatoResponse actualizar(Long id, PlatoRequest request) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));

        if (!plato.getNombre().equals(request.getNombre()) && platoRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe otro plato con el nombre: " + request.getNombre());
        }

        plato.setNombre(request.getNombre());
        plato.setPrecioVenta(request.getPrecioVenta());
        plato.setDescripcion(request.getDescripcion());
        plato = platoRepository.save(plato);
        return toResponseWithIngredientes(plato);
    }

    @Transactional
    public void toggleDisponibilidad(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + id));
        plato.setDisponible(!plato.isDisponible());
        platoRepository.save(plato);
    }

    private PlatoResponse toResponseWithIngredientes(Plato plato) {
        List<RecetaIngrediente> ingredientes = recetaRepository.findByPlatoIdOrderByProductoNombreAsc(plato.getId());
        List<IngredienteInfo> ingredientesInfo = ingredientes.stream()
                .map(i -> new IngredienteInfo(i.getId(), i.getProducto().getNombre(),
                        i.getCantidad(), i.getProducto().getUnidad().name()))
                .toList();
        boolean conStock = !ingredientes.isEmpty() && ingredientes.stream()
                .allMatch(i -> i.getProducto().getStockActual().compareTo(i.getCantidad()) >= 0);
        return new PlatoResponse(plato.getId(), plato.getNombre(), plato.getPrecioVenta(),
                plato.isDisponible(), plato.getDescripcion(), ingredientesInfo, conStock);
    }
}

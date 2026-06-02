package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.producto.ProductoRequest;
import com.restaurante.inventario.dto.producto.ProductoResponse;
import com.restaurante.inventario.exception.BadRequestException;
import com.restaurante.inventario.exception.ResourceNotFoundException;
import com.restaurante.inventario.model.Producto;
import com.restaurante.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductoResponse buscarPorId(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        if (productoRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe un producto con el nombre: " + request.getNombre());
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setUnidad(request.getUnidad());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setStockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : BigDecimal.ZERO);
        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (!producto.getNombre().equals(request.getNombre()) && productoRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe otro producto con el nombre: " + request.getNombre());
        }

        producto.setNombre(request.getNombre());
        producto.setUnidad(request.getUnidad());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setStockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : BigDecimal.ZERO);
        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public List<ProductoResponse> listarStockBajo() {
        return productoRepository.findByActivoTrueOrderByNombreAsc().stream()
                .filter(p -> p.getStockActual().compareTo(p.getStockMinimo()) < 0)
                .map(this::toResponse)
                .toList();
    }

    private ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getUnidad(),
                producto.getStockActual(), producto.getStockMinimo(), producto.getPrecioCompra(), producto.isActivo());
    }
}

package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.movimiento.AjusteRequest;
import com.restaurante.inventario.dto.movimiento.EntradaRequest;
import com.restaurante.inventario.dto.movimiento.MovimientoResponse;
import com.restaurante.inventario.dto.movimiento.SalidaRequest;
import com.restaurante.inventario.exception.BadRequestException;
import com.restaurante.inventario.exception.ResourceNotFoundException;
import com.restaurante.inventario.model.*;
import com.restaurante.inventario.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final PlatoRepository platoRepository;
    private final RecetaIngredienteRepository recetaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                             ProductoRepository productoRepository,
                             PlatoRepository platoRepository,
                             RecetaIngredienteRepository recetaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.platoRepository = platoRepository;
        this.recetaRepository = recetaRepository;
    }

    @Transactional
    public MovimientoResponse registrarEntrada(EntradaRequest request, Usuario usuario) {
        Producto producto = productoRepository.findByIdAndActivoTrue(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setCantidad(request.getCantidad());
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setProveedor(request.getProveedor());
        movimiento = movimientoRepository.save(movimiento);

        producto.setStockActual(producto.getStockActual().add(request.getCantidad()));
        productoRepository.save(producto);

        return toResponse(movimiento);
    }

    @Transactional
    public List<MovimientoResponse> registrarSalida(SalidaRequest request, Usuario usuario) {
        Plato plato = platoRepository.findById(request.getPlatoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + request.getPlatoId()));

        if (!plato.isDisponible()) {
            throw new BadRequestException("El plato no está disponible: " + plato.getNombre());
        }

        List<RecetaIngrediente> ingredientes = recetaRepository.findByPlatoIdOrderByProductoNombreAsc(plato.getId());

        if (ingredientes.isEmpty()) {
            throw new BadRequestException("El plato no tiene receta definida");
        }

        for (RecetaIngrediente item : ingredientes) {
            Producto producto = item.getProducto();
            BigDecimal cantidadNecesaria = item.getCantidad();

            if (producto.getStockActual().compareTo(cantidadNecesaria) < 0) {
                throw new BadRequestException(
                    "Stock insuficiente de " + producto.getNombre() +
                    " (disponible: " + producto.getStockActual() +
                    ", necesario: " + cantidadNecesaria + " " + producto.getUnidad().name() + ")"
                );
            }
        }

        for (RecetaIngrediente item : ingredientes) {
            Producto producto = item.getProducto();

            MovimientoStock movimiento = new MovimientoStock();
            movimiento.setTipo(TipoMovimiento.SALIDA);
            movimiento.setCantidad(item.getCantidad().negate());
            movimiento.setProducto(producto);
            movimiento.setUsuario(usuario);
            movimiento.setPlato(plato);
            movimientoRepository.save(movimiento);

            producto.setStockActual(producto.getStockActual().subtract(item.getCantidad()));
            productoRepository.save(producto);
        }

        return movimientoRepository.findByPlatoId(plato.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MovimientoResponse registrarAjuste(AjusteRequest request, Usuario usuario) {
        Producto producto = productoRepository.findByIdAndActivoTrue(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        if (request.getCantidad().compareTo(BigDecimal.ZERO) < 0 &&
            producto.getStockActual().add(request.getCantidad()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(
                "El ajuste no puede reducir el stock por debajo de cero. Stock actual: " +
                producto.getStockActual() + ", ajuste: " + request.getCantidad()
            );
        }

        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setTipo(TipoMovimiento.AJUSTE);
        movimiento.setCantidad(request.getCantidad());
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setMotivo(request.getMotivo());
        movimiento = movimientoRepository.save(movimiento);

        producto.setStockActual(producto.getStockActual().add(request.getCantidad()));
        productoRepository.save(producto);

        return toResponse(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarTodos(Long productoId, String tipo,
                                                 LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        TipoMovimiento tipoEnum = tipo != null ? TipoMovimiento.valueOf(tipo.toUpperCase()) : null;
        return movimientoRepository.findAllConRelaciones().stream()
                .filter(m -> productoId == null || m.getProducto().getId().equals(productoId))
                .filter(m -> tipoEnum == null || m.getTipo() == tipoEnum)
                .filter(m -> fechaDesde == null || !m.getFecha().isBefore(fechaDesde))
                .filter(m -> fechaHasta == null || !m.getFecha().isAfter(fechaHasta))
                .map(this::toResponse)
                .toList();
    }

    private MovimientoResponse toResponse(MovimientoStock m) {
        MovimientoResponse r = new MovimientoResponse();
        r.setId(m.getId());
        r.setTipo(m.getTipo().name());
        r.setCantidad(m.getCantidad());
        r.setFecha(m.getFecha());
        r.setProveedor(m.getProveedor());
        r.setMotivo(m.getMotivo());
        r.setProductoId(m.getProducto().getId());
        r.setProductNombre(m.getProducto().getNombre());
        r.setUsuarioEmail(m.getUsuario().getEmail());
        if (m.getPlato() != null) {
            r.setPlatoId(m.getPlato().getId());
            r.setPlatoNombre(m.getPlato().getNombre());
        }
        return r;
    }
}

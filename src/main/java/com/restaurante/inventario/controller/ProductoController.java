package com.restaurante.inventario.controller;

import com.restaurante.inventario.dto.producto.ProductoRequest;
import com.restaurante.inventario.dto.producto.ProductoResponse;
import com.restaurante.inventario.dto.MensajeResponse;
import com.restaurante.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/stock-bajo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoResponse>> listarStockBajo() {
        return ResponseEntity.ok(productoService.listarStockBajo());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.ok(new MensajeResponse("Producto desactivado correctamente"));
    }
}

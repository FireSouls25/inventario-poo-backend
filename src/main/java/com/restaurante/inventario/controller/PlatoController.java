package com.restaurante.inventario.controller;

import com.restaurante.inventario.dto.plato.PlatoRequest;
import com.restaurante.inventario.dto.plato.PlatoResponse;
import com.restaurante.inventario.dto.MensajeResponse;
import com.restaurante.inventario.service.PlatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    @GetMapping
    public ResponseEntity<List<PlatoResponse>> listarTodos() {
        return ResponseEntity.ok(platoService.listarTodos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PlatoResponse>> listarDisponibles() {
        return ResponseEntity.ok(platoService.listarDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(platoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatoResponse> crear(@Valid @RequestBody PlatoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody PlatoRequest request) {
        return ResponseEntity.ok(platoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/disponibilidad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> toggleDisponibilidad(@PathVariable Long id) {
        platoService.toggleDisponibilidad(id);
        return ResponseEntity.ok(new MensajeResponse("Disponibilidad actualizada correctamente"));
    }
}

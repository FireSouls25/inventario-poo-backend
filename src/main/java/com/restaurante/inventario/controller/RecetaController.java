package com.restaurante.inventario.controller;

import com.restaurante.inventario.dto.plato.PlatoResponse.IngredienteInfo;
import com.restaurante.inventario.dto.plato.RecetaIngredienteRequest;
import com.restaurante.inventario.dto.MensajeResponse;
import com.restaurante.inventario.service.RecetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos/{platoId}/receta")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    public ResponseEntity<List<IngredienteInfo>> listarIngredientes(@PathVariable Long platoId) {
        return ResponseEntity.ok(recetaService.listarIngredientes(platoId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredienteInfo> agregarIngrediente(
            @PathVariable Long platoId,
            @Valid @RequestBody RecetaIngredienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recetaService.agregarIngrediente(platoId, request));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredienteInfo> actualizarIngrediente(
            @PathVariable Long platoId,
            @PathVariable Long itemId,
            @Valid @RequestBody RecetaIngredienteRequest request) {
        return ResponseEntity.ok(recetaService.actualizarIngrediente(platoId, itemId, request));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeResponse> eliminarIngrediente(
            @PathVariable Long platoId,
            @PathVariable Long itemId) {
        recetaService.eliminarIngrediente(platoId, itemId);
        return ResponseEntity.ok(new MensajeResponse("Ingrediente eliminado de la receta"));
    }
}

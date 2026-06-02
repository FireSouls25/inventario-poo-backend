package com.restaurante.inventario.controller;

import com.restaurante.inventario.dto.movimiento.AjusteRequest;
import com.restaurante.inventario.dto.movimiento.EntradaRequest;
import com.restaurante.inventario.dto.movimiento.MovimientoResponse;
import com.restaurante.inventario.dto.movimiento.SalidaRequest;
import com.restaurante.inventario.model.Usuario;
import com.restaurante.inventario.repository.UsuarioRepository;
import com.restaurante.inventario.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final UsuarioRepository usuarioRepository;

    public MovimientoController(MovimientoService movimientoService, UsuarioRepository usuarioRepository) {
        this.movimientoService = movimientoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) LocalDateTime fechaDesde,
            @RequestParam(required = false) LocalDateTime fechaHasta) {
        return ResponseEntity.ok(movimientoService.listarTodos(productoId, tipo, fechaDesde, fechaHasta));
    }

    @PostMapping("/entrada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovimientoResponse> registrarEntrada(
            @Valid @RequestBody EntradaRequest request,
            Authentication authentication) {
        Usuario usuario = getAuthenticatedUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarEntrada(request, usuario));
    }

    @PostMapping("/salida")
    @PreAuthorize("hasAnyRole('MESERO', 'ADMIN')")
    public ResponseEntity<List<MovimientoResponse>> registrarSalida(
            @Valid @RequestBody SalidaRequest request,
            Authentication authentication) {
        Usuario usuario = getAuthenticatedUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarSalida(request, usuario));
    }

    @PostMapping("/ajuste")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovimientoResponse> registrarAjuste(
            @Valid @RequestBody AjusteRequest request,
            Authentication authentication) {
        Usuario usuario = getAuthenticatedUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarAjuste(request, usuario));
    }

    private Usuario getAuthenticatedUser(Authentication auth) {
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

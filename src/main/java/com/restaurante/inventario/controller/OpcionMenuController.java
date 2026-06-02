package com.restaurante.inventario.controller;

import com.restaurante.inventario.dto.opcion.OpcionMenuDTO;
import com.restaurante.inventario.service.OpcionMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones-menu")
public class OpcionMenuController {

    private final OpcionMenuService service;

    public OpcionMenuController(OpcionMenuService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OpcionMenuDTO>> obtenerMenu(@RequestParam String rol) {
        return ResponseEntity.ok(service.obtenerMenu(rol));
    }
}

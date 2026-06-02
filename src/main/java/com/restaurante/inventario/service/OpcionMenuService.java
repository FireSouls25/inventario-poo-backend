package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.opcion.OpcionMenuDTO;
import com.restaurante.inventario.model.OpcionMenu;
import com.restaurante.inventario.repository.OpcionMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpcionMenuService {

    private final OpcionMenuRepository repository;

    public OpcionMenuService(OpcionMenuRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OpcionMenuDTO> obtenerMenu(String rol) {
        List<OpcionMenu> raices = repository.findByPadreIsNullOrderByOrdenAsc();
        return raices.stream()
                .map(r -> toDTO(r, rol))
                .filter(dto -> !dto.getHijos().isEmpty())
                .collect(Collectors.toList());
    }

    private OpcionMenuDTO toDTO(OpcionMenu entity, String rol) {
        OpcionMenuDTO dto = new OpcionMenuDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setRuta(entity.getRuta());
        dto.setRoles(entity.getRoles());
        dto.setIcono(entity.getIcono());
        dto.setOrden(entity.getOrden());

        List<OpcionMenuDTO> hijosFiltrados = new ArrayList<>();
        for (OpcionMenu hijo : entity.getHijos()) {
            if (tieneRol(hijo.getRoles(), rol)) {
                OpcionMenuDTO hijoDTO = toDTO(hijo, rol);
                hijosFiltrados.add(hijoDTO);
            }
        }
        dto.setHijos(hijosFiltrados);

        return dto;
    }

    private boolean tieneRol(String roles, String rol) {
        if (roles == null || roles.isBlank()) return true;
        for (String r : roles.split(",")) {
            if (r.trim().equalsIgnoreCase(rol)) return true;
        }
        return false;
    }
}

package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByDisponibleTrueOrderByNombreAsc();
    boolean existsByNombre(String nombre);
}

package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoStock, Long> {
    List<MovimientoStock> findByProductoIdOrderByFechaDesc(Long productoId);

    @Query("SELECT m FROM MovimientoStock m " +
           "LEFT JOIN FETCH m.producto " +
           "LEFT JOIN FETCH m.usuario " +
           "LEFT JOIN FETCH m.plato " +
           "ORDER BY m.fecha DESC")
    List<MovimientoStock> findAllConRelaciones();

    List<MovimientoStock> findByPlatoId(Long platoId);
}

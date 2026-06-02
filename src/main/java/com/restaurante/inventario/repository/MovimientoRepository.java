package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.MovimientoStock;
import com.restaurante.inventario.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoStock, Long> {
    List<MovimientoStock> findByProductoIdOrderByFechaDesc(Long productoId);

    @Query("SELECT m FROM MovimientoStock m WHERE " +
           "(:productoId IS NULL OR m.producto.id = :productoId) AND " +
           "(:tipo IS NULL OR m.tipo = :tipo) AND " +
           "(:fechaDesde IS NULL OR m.fecha >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR m.fecha <= :fechaHasta) " +
           "ORDER BY m.fecha DESC")
    List<MovimientoStock> buscarConFiltros(
        @Param("productoId") Long productoId,
        @Param("tipo") TipoMovimiento tipo,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta
    );

    List<MovimientoStock> findByPlatoId(Long platoId);
}

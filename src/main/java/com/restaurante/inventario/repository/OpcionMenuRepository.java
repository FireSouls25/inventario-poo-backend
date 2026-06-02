package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.OpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Long> {

    @Query("SELECT o FROM OpcionMenu o LEFT JOIN FETCH o.hijos h WHERE o.padre IS NULL ORDER BY o.orden ASC, h.orden ASC")
    List<OpcionMenu> findAllWithHijos();

    List<OpcionMenu> findByPadreIsNullOrderByOrdenAsc();
}

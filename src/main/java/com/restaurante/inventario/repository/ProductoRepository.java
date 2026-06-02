package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrueOrderByNombreAsc();
    Optional<Producto> findByIdAndActivoTrue(Long id);
    List<Producto> findByStockActualLessThanEqualAndActivoTrue(BigDecimal stockMinimo);
    boolean existsByNombre(String nombre);
}

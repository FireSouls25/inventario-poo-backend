package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.Plato;
import com.restaurante.inventario.model.Producto;
import com.restaurante.inventario.model.RecetaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, Long> {
    List<RecetaIngrediente> findByPlatoIdOrderByProductoNombreAsc(Long platoId);
    Optional<RecetaIngrediente> findByPlatoIdAndProductoId(Long platoId, Long productoId);
    boolean existsByPlatoIdAndProductoId(Long platoId, Long productoId);
    void deleteByPlatoId(Long platoId);
    List<RecetaIngrediente> findByProducto(Producto producto);
}

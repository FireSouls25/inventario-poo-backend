package com.restaurante.inventario.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "receta_ingredientes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"plato_id", "producto_id"})
})
public class RecetaIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plato_id", nullable = false)
    private Plato plato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal cantidad;

    public RecetaIngrediente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Plato getPlato() { return plato; }
    public void setPlato(Plato plato) { this.plato = plato; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
}

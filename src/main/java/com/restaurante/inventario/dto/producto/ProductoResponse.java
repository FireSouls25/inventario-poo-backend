package com.restaurante.inventario.dto.producto;

import com.restaurante.inventario.model.UnidadMedida;
import java.math.BigDecimal;

public class ProductoResponse {
    private Long id;
    private String nombre;
    private UnidadMedida unidad;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private BigDecimal precioCompra;
    private boolean activo;

    public ProductoResponse() {}

    public ProductoResponse(Long id, String nombre, UnidadMedida unidad, BigDecimal stockActual,
                            BigDecimal stockMinimo, BigDecimal precioCompra, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.unidad = unidad;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public UnidadMedida getUnidad() { return unidad; }
    public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }
    public BigDecimal getStockActual() { return stockActual; }
    public void setStockActual(BigDecimal stockActual) { this.stockActual = stockActual; }
    public BigDecimal getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(BigDecimal stockMinimo) { this.stockMinimo = stockMinimo; }
    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

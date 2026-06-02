package com.restaurante.inventario.dto.producto;

import com.restaurante.inventario.model.UnidadMedida;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidad;

    @NotNull(message = "El precio de compra es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal precioCompra;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public UnidadMedida getUnidad() { return unidad; }
    public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }
    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }
    public BigDecimal getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(BigDecimal stockMinimo) { this.stockMinimo = stockMinimo; }
}

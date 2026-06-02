package com.restaurante.inventario.dto.plato;

import java.math.BigDecimal;
import java.util.List;

public class PlatoResponse {
    private Long id;
    private String nombre;
    private BigDecimal precioVenta;
    private boolean disponible;
    private String descripcion;
    private List<IngredienteInfo> ingredientes;
    private boolean conStock;

    public PlatoResponse() {}

    public PlatoResponse(Long id, String nombre, BigDecimal precioVenta, boolean disponible,
                         String descripcion, List<IngredienteInfo> ingredientes, boolean conStock) {
        this.id = id;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.disponible = disponible;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.conStock = conStock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<IngredienteInfo> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<IngredienteInfo> ingredientes) { this.ingredientes = ingredientes; }
    public boolean isConStock() { return conStock; }
    public void setConStock(boolean conStock) { this.conStock = conStock; }

    public static class IngredienteInfo {
        private Long id;
        private String productoNombre;
        private BigDecimal cantidad;
        private String unidad;

        public IngredienteInfo() {}

        public IngredienteInfo(Long id, String productoNombre, BigDecimal cantidad, String unidad) {
            this.id = id;
            this.productoNombre = productoNombre;
            this.cantidad = cantidad;
            this.unidad = unidad;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
        public BigDecimal getCantidad() { return cantidad; }
        public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
        public String getUnidad() { return unidad; }
        public void setUnidad(String unidad) { this.unidad = unidad; }
    }
}

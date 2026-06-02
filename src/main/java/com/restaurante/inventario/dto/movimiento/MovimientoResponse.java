package com.restaurante.inventario.dto.movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoResponse {
    private Long id;
    private String tipo;
    private BigDecimal cantidad;
    private LocalDateTime fecha;
    private String proveedor;
    private String motivo;
    private Long productoId;
    private String productNombre;
    private String usuarioEmail;
    private Long platoId;
    private String platoNombre;

    public MovimientoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getProductNombre() { return productNombre; }
    public void setProductNombre(String productNombre) { this.productNombre = productNombre; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public Long getPlatoId() { return platoId; }
    public void setPlatoId(Long platoId) { this.platoId = platoId; }
    public String getPlatoNombre() { return platoNombre; }
    public void setPlatoNombre(String platoNombre) { this.platoNombre = platoNombre; }
}

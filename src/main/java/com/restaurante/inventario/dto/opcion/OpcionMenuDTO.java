package com.restaurante.inventario.dto.opcion;

import java.util.ArrayList;
import java.util.List;

public class OpcionMenuDTO {

    private Long id;
    private String nombre;
    private String ruta;
    private String roles;
    private String icono;
    private int orden;
    private List<OpcionMenuDTO> hijos = new ArrayList<>();

    public OpcionMenuDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    public List<OpcionMenuDTO> getHijos() { return hijos; }
    public void setHijos(List<OpcionMenuDTO> hijos) { this.hijos = hijos; }
}

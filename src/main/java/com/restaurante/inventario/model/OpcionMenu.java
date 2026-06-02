package com.restaurante.inventario.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opciones")
public class OpcionMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_opcion_id")
    private OpcionMenu padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<OpcionMenu> hijos = new ArrayList<>();

    @Column(length = 200)
    private String ruta;

    @Column(nullable = false, length = 100)
    private String roles;

    @Column(length = 50)
    private String icono;

    @Column(nullable = false)
    private int orden = 0;

    public OpcionMenu() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public OpcionMenu getPadre() { return padre; }
    public void setPadre(OpcionMenu padre) { this.padre = padre; }
    public List<OpcionMenu> getHijos() { return hijos; }
    public void setHijos(List<OpcionMenu> hijos) { this.hijos = hijos; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}

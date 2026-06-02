package com.restaurante.inventario.dto.auth;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String rol;
    private String nombre;

    public AuthResponse() {}

    public AuthResponse(String token, String refreshToken, String email, String rol, String nombre) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.rol = rol;
        this.nombre = nombre;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

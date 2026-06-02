package com.restaurante.inventario.repository;

import com.restaurante.inventario.model.RefreshToken;
import com.restaurante.inventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}

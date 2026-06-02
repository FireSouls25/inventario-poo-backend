package com.restaurante.inventario.service;

import com.restaurante.inventario.dto.auth.AuthResponse;
import com.restaurante.inventario.dto.auth.LoginRequest;
import com.restaurante.inventario.dto.auth.RefreshTokenRequest;
import com.restaurante.inventario.dto.auth.RegisterRequest;
import com.restaurante.inventario.exception.BadRequestException;
import com.restaurante.inventario.exception.UnauthorizedException;
import com.restaurante.inventario.model.RefreshToken;
import com.restaurante.inventario.model.Usuario;
import com.restaurante.inventario.repository.RefreshTokenRepository;
import com.restaurante.inventario.repository.UsuarioRepository;
import com.restaurante.inventario.security.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;

    public AuthService(UsuarioRepository usuarioRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.entityManager = entityManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());
        entityManager.persist(usuario);
        entityManager.flush();

        return generateAuthResponse(usuario);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return generateAuthResponse(usuario);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Refresh token inválido"));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new BadRequestException("Refresh token expirado");
        }

        Usuario usuario = stored.getUsuario();
        refreshTokenRepository.deleteByUsuario(usuario);

        return generateAuthResponse(usuario);
    }

    private AuthResponse generateAuthResponse(Usuario usuario) {
        String accessToken = jwtTokenProvider.generateAccessToken(usuario.getEmail(), usuario.getRol().name());
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(usuario.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshTokenStr, usuario.getEmail(),
                usuario.getRol().name(), usuario.getNombre());
    }
}

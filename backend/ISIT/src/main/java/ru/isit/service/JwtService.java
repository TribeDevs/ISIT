package ru.isit.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.isit.models.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HexFormat;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            HexFormat hex = HexFormat.of();
            byte[] keyBytes = hex.parseHex(secret);

            if (keyBytes.length != 64) {
                throw new IllegalArgumentException("Ключ должен быть 512 бит (64 байта)");
            }

            key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось инициализировать JWT сервис", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof User)) {
            throw new IllegalArgumentException("UserDetails должен быть экземпляром User");
        }

        User user = (User) userDetails;
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public UUID extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return UUID.fromString(claims.get("userId", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Невалидный токен", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            final String username = claims.getSubject();
            final boolean isExpired = claims.getExpiration().before(new Date());

            return username.equals(userDetails.getUsername()) && !isExpired;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
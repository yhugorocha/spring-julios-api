package com.expense.saas.security;

import com.expense.saas.domain.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenService {

    private static final String USER_ID_CLAIM = "userId";
    private static final String ROLE_CLAIM = "role";

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = this.resolveSigningKey();
    }

    public String generateToken(UUID userId, UserRole role, String email) {
        var now = Instant.now();
        var expiresAt = now.plusMillis(this.expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim(USER_ID_CLAIM, userId.toString())
                .claim(ROLE_CLAIM, role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(this.signingKey)
                .compact();
    }

    public JwtUserClaims parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtUserClaims(
                UUID.fromString(claims.get(USER_ID_CLAIM, String.class)),
                UserRole.valueOf(claims.get(ROLE_CLAIM, String.class)),
                claims.getSubject()
        );
    }

    public long getExpirationMs() {
        return this.expirationMs;
    }

    public boolean isValid(String token) {
        try {
            this.parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private SecretKey resolveSigningKey() {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
        } catch (IllegalArgumentException ex) {
            return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    public record JwtUserClaims(
            UUID userId,
            UserRole role,
            String email
    ) {
    }
}

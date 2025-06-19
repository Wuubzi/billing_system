package com.billing_system.gateway.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtToken {

    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        byte[] ketBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(ketBytes);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("The token cannot be null or empty.");
        }

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

    }



    }

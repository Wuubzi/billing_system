package com.billing_system.auth.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    public String getEmailFromToken(String token){
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

    public String generateToken(Long id_user, String gmail) {
        Map<String, Object> claims = new HashMap<>();claims.put("id_user", id_user);
        return doGenerateToken(claims, gmail);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


}

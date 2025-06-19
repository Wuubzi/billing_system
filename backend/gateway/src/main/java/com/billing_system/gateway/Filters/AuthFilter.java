package com.billing_system.gateway.Filters;

import com.billing_system.gateway.Utils.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter> {


    private final JwtToken jwtToken;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthFilter(JwtToken jwtToken, ObjectMapper objectMapper) {
        super(AuthFilter.class);
        this.jwtToken = jwtToken;
        this.objectMapper = objectMapper;
    }

    public AuthFilter() {
        super(AuthFilter.class);
        this.jwtToken = null;
        this.objectMapper = null;
    }

    public GatewayFilter apply(AuthFilter config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String tokenRequest = request.getHeaders().getFirst("Authorization");
            if (tokenRequest == null || tokenRequest.isEmpty()) {
                return handleError(exchange, HttpStatus.BAD_REQUEST, "The token cannot be null or empty.");
            }
            if (!tokenRequest.startsWith("Bearer ")) {
                return handleError(exchange, HttpStatus.BAD_REQUEST, "Authorization header must start with 'Bearer '.");
            }

            String token = tokenRequest.substring(7);
            try {
                assert jwtToken != null;
                jwtToken.validateToken(token);
                return chain.filter(exchange);

            } catch (ExpiredJwtException e) {
                return handleError(exchange, HttpStatus.UNAUTHORIZED, "Token has expired");

            } catch (SignatureException e) {
                return handleError(exchange, HttpStatus.UNAUTHORIZED, "Invalid token signature");

            } catch (MalformedJwtException e) {
                return handleError(exchange, HttpStatus.BAD_REQUEST, "Malformed token");

            } catch (UnsupportedJwtException e) {
                return handleError(exchange, HttpStatus.BAD_REQUEST, "Unsupported token format");

            } catch (IllegalArgumentException e) {
                return handleError(exchange, HttpStatus.BAD_REQUEST, "Invalid token: " + e.getMessage());

            } catch (JwtException e) {
                return handleError(exchange, HttpStatus.UNAUTHORIZED,
                        "Authentication error: " + e.getMessage());
            }
        };
    }
    private Mono<Void> handleError(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        errorDetails.put("path", exchange.getRequest().getPath().toString());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorDetails);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }
    }


    @Override
    public String name() {
        return "AuthFilter";
    }
}

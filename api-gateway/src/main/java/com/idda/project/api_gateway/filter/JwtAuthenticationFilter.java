package com.idda.project.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j; // <-- YENİ İMPORT
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j // <-- YENİ ANNOTASİYA
public class JwtAuthenticationFilter implements GatewayFilter {

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        log.info("JWT Secret Key initialized for Gateway.");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info(">>> GATEWAY FILTER: Request received for path: {}", request.getURI().getPath());

        final List<String> publicEndpoints = List.of("/api/auth/register", "/api/auth/login");
        Predicate<ServerHttpRequest> isPublic =
                (r) -> publicEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));

        if (isPublic.test(request)) {
            log.info(">>> Path is public. Skipping token validation.");
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey("Authorization")) {
            log.warn(">>> Authorization header is missing!");
            return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        final String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
        if (!authHeader.startsWith("Bearer ")) {
            log.warn(">>> Authorization header does not start with Bearer!");
            return this.onError(exchange, "Authorization header is not a Bearer token", HttpStatus.UNAUTHORIZED);
        }

        final String token = authHeader.substring(7);
        log.info(">>> Extracted Token: {}", token);

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            log.info(">>> Token validation successful. Claims: {}", claims);

            String userId = claims.get("userId").toString();
            log.info(">>> Extracted userId from token: {}", userId);

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            log.error(">>> !!! TOKEN VALIDATION FAILED !!! Exception: {}, Message: {}", e.getClass().getName(), e.getMessage());
            return this.onError(exchange, "Authorization error: Token is invalid or expired", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        log.error(">>> Responding with error. Status: {}, Message: {}", httpStatus, err);
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }
}
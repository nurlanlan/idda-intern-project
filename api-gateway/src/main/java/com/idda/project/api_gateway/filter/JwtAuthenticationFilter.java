package com.idda.project.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
public class JwtAuthenticationFilter implements GatewayFilter {


    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. Endpointin public (açıq) olub-olmadığını yoxlayırıq.
        final List<String> publicEndpoints = List.of("/api/auth/register", "/api/auth/login");
        Predicate<ServerHttpRequest> isPublic =
                (r) -> publicEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));

        if (isPublic.test(request)) {
            return chain.filter(exchange); // Əgər public-dirsə, heç nə etmə, sorğunu ötür.
        }

        // 2. Authorization başlığının olub-olmadığını yoxlayırıq.
        if (!request.getHeaders().containsKey("Authorization")) {
            return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        // 3. Tokeni başlıqdan alırıq.
        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7); // "Bearer " hissəsini kəsirik

        try {
            // 4. Tokeni yoxlayırıq (parse edirik). Əgər səhvdirsə, Exception atacaq.
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String userId = claims.getSubject(); // Tokenin içindən userId-ni alırıq

            // 5. Sorğuya yeni bir başlıq əlavə edirik və arxadakı servisə yönləndiririk.
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            // Əgər token yoxlaması uğursuz olsa (müddəti bitib, saxtadır və s.)
            return this.onError(exchange, "Authorization error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

}

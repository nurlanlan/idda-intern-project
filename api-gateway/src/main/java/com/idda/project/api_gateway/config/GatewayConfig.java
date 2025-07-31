package com.idda.project.api_gateway.config;

import com.idda.project.api_gateway.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service-route", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8082"))
                .route("user-service-route", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8081"))
                .route("card-service-route", r -> r.path("/api/cards/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8083"))
                .route("product-service-route", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8084"))
                .route("transaction-service-route", r -> r.path("/api/payments/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8085"))
                .build();
    }
}

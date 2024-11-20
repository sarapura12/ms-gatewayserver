package com.ms.gatewayserver.config;

import com.ms.gatewayserver.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p ->
                        p.path("/applications/apiclients/**")
                        .filters(f -> f.filter(filter)
                                .rewritePath("/applications/apiclients/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://ms-clients"))

                .route(p -> p
                        .path("/applications/apiidentity/**")
                        .filters(f -> f.filter(filter)
                                .rewritePath("/applications/apiidentity/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://ms-identity"))

                .route(p -> p.path("/applications/apiproducts/**")
                        .filters(f -> f.filter(filter)
                                .rewritePath("/applications/apiproducts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://ms-products"))
                .route(p -> p.path("/applications/apisales/**")
                        .filters(f -> f.filter(filter)
                                .rewritePath("/applications/apisales/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://ms-sales"))
                .build();
    }

}

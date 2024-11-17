package com.ms.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication

public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route(p -> p
                        .path("/applications/apiclients/**")
                        .filters(f ->
                                f.rewritePath("/applications/apiclients/(?<segment>.*)","/${segment}")
                                        .addResponseHeader("X-Response-Time",new Date().toString()))
                        .uri("lb://ms-clients")).


                route(p -> p
                        .path("/applications/apiproducts/**")
                        .filters(f ->
                                f.rewritePath("/applications/apiproducts/(?<segment>.*)","/${segment}")
                                        .addResponseHeader("X-Response-Time",new Date().toString()))
                        .uri("lb://ms-products")).


                route(p -> p
                        .path("/applications/apisales/**")
                        .filters(f ->
                                f.rewritePath("/applications/apisales/(?<segment>.*)","/${segment}")
            .addResponseHeader("X-Response-Time",new Date().toString()))
            .uri("lb://ms-sales")).build();
    }

}

package com.ms.gatewayserver.filter;

import com.ms.gatewayserver.external.identity.IIdentityFeignClient;
import com.ms.gatewayserver.external.identity.models.TokenRequest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    private final IIdentityFeignClient identityFeignClient;

    public JwtAuthenticationFilter(@Lazy IIdentityFeignClient identityFeignClient) {
        this.identityFeignClient = identityFeignClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/api/auth/register", "/api/auth/token", "/api/auth/signup", "/eureka");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (authMissing(request)) return onError(exchange);

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (token != null && token.startsWith("Bearer ")) token = token.substring(7);

            String finalToken = token;
            return Mono.fromCallable(() -> {
                        var tokenRequest = new TokenRequest(finalToken);
                        identityFeignClient.validateToken(tokenRequest);
                        return true;
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                    .then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        if (!response.isCommitted()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}

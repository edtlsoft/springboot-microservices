package com.edtlsoft.microservices.gateway.filters;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilterExample implements GlobalFilter, Ordered {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalFilterExample.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Executing Global Filter Pre");

        // modify request
        exchange.getRequest().mutate().headers(h -> h.add("token", "1234567890"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Executing Global Filter Post");

            Optional
                .ofNullable(exchange.getRequest().getHeaders().getFirst("token"))
                .ifPresent(token -> {
                    exchange.getResponse().getHeaders().add("token", token);
                    logger.info("token: " + token);
                });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "blue").build());
            // exchange.getResponse().getHeaders().add("Content-Type", "text/plain");
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }

}

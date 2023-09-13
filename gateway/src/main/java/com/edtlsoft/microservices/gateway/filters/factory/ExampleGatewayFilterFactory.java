package com.edtlsoft.microservices.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class ExampleGatewayFilterFactory extends AbstractGatewayFilterFactory<ExampleGatewayFilterFactory.Config> {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ExampleGatewayFilterFactory.class);

    public ExampleGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "FilterToSetResponseCookie";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("Executing ExampleGatewayFilterFactory Pre: " + config.message);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.cookieValue).ifPresent(cookieValue -> {
                    exchange.getResponse().getCookies().add(
                        config.cookieName, 
                        ResponseCookie.from(config.cookieName, cookieValue).build()
                    );
                });

                logger.info("Executing ExampleGatewayFilterFactory Post: " + config.message);
            }));
        };
    }

    public static class Config {
        private String message;
        private String cookieValue;
        private String cookieName;

        public Config() {
        }

        public Void setMessage(String message) {
            this.message = message;
            return null;
        }

        public String getMessage() {
            return message;
        }

        public String getCookieValue() {
            return cookieValue;
        }

        public void setCookieValue(String cookieValue) {
            this.cookieValue = cookieValue;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }
    }
}

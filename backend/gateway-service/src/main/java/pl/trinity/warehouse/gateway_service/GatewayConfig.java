package pl.trinity.warehouse.gateway_service;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // PRODUCT-SERVICE
                .route("product-route", r -> r.path("/api/products/**")
                        .uri("lb://product-service"))

                // WAREHOUSE-SERVICE
                .route("warehouse-route", r -> r.path("/api/warehouse/**", "/api/stocks/**")
                        .uri("lb://warehouse-service"))
                .build();
    }
}

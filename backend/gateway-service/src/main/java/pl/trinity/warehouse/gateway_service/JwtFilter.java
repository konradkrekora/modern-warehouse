package pl.trinity.warehouse.gateway_service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.getClaims(token).get("role", String.class);

                // 1. Obiekt uwierzytelnienia dla Spring Security w Gatewayu
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, List.of(new SimpleGrantedAuthority(role))
                );

                // 2. NOWOŚĆ: Doklejenie nagłówków HTTP dla mikroserwisów docelowych
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Name", username)
                        .header("X-User-Role", role)
                        .build();

                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(modifiedRequest)
                        .build();

                // W WebFlux Security Context przekazuje się w strumieniu (ReactiveSecurityContextHolder)
                return chain.filter(modifiedExchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            }
        }

        return chain.filter(exchange);
    }
}
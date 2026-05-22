package pl.trinity.warehouse.gateway_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // Prosta walidacja na start (potem wepniemy tu prawdziwą bazę użytkowników)
        if ("konrad".equals(username) && "magazyn2026".equals(password)) {
            String token = jwtUtil.generateToken(username, "ROLE_PRACOWNIK");
            return Mono.just(ResponseEntity.ok(Map.of("token", token)));
        }

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Błędny login lub hasło!")));
    }
}

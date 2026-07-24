package pl.trinity.warehouse.user_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.trinity.warehouse.user_service.user.UserEntity;
import pl.trinity.warehouse.user_service.user.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        if (userRepository.findByUsername(request.get("username")).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Użytkownik już istnieje!"));
        }

        UserEntity newUser = new UserEntity(
                request.get("username"),
                passwordEncoder.encode(request.get("password")), // Hashujemy hasło przed zapisem!
                request.getOrDefault("role", "ROLE_PRACOWNIK")
        );
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Pracownik zarejestrowany pomyślnie"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return userRepository.findByUsername(request.get("username"))
                .filter(user -> passwordEncoder.matches(request.get("password"), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                    return ResponseEntity.ok((Object) Map.of("token", token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Błędne dane!")));
    }
}

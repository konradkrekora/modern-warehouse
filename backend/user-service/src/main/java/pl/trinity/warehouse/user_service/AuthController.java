package pl.trinity.warehouse.user_service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.trinity.warehouse.user_service.dto.AuthResponse;
import pl.trinity.warehouse.user_service.dto.LoginRequest;
import pl.trinity.warehouse.user_service.dto.MessageResponse;
import pl.trinity.warehouse.user_service.dto.RegisterRequest;
import pl.trinity.warehouse.user_service.user.UserEntity;
import pl.trinity.warehouse.user_service.user.UserRepository;

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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Użytkownik o podanej nazwie już istnieje!"));
        }

        String role = (request.role() != null && !request.role().isBlank())
                ? request.role()
                : "ROLE_PRACOWNIK";

        UserEntity newUser = new UserEntity(
                request.username(),
                passwordEncoder.encode(request.password()),
                role
        );
        userRepository.save(newUser);
        return ResponseEntity.ok(new MessageResponse("Użytkownik zarejestrowany pomyślnie"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.username())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Błędna nazwa użytkownika lub hasło!"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

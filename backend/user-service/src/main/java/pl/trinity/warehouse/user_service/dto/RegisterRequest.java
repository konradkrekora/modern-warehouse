package pl.trinity.warehouse.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Nazwa użytkownika nie może być pusta")
        @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków")
        String username,

        @NotBlank(message = "Hasło nie może być puste")
        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        String password,

        String role
) {}
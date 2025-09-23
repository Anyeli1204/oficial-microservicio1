package com.scrapetok.accounts.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "Contraseña es requerida")
    private String password;
}

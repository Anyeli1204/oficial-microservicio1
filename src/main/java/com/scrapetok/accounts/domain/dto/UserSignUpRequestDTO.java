package com.scrapetok.accounts.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpRequestDTO {
    
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "Contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    @NotBlank(message = "Nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String firstname;
    
    @NotBlank(message = "Apellido es requerido")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String lastname;
    
    @NotBlank(message = "Username es requerido")
    @Size(max = 100, message = "El username no puede exceder 100 caracteres")
    private String username;
}

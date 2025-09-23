package com.scrapetok.accounts.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstname;
    
    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String lastname;
    
    @NotBlank(message = "El username es requerido")
    @Size(min = 3, max = 100, message = "El username debe tener entre 3 y 100 caracteres")
    private String username;
    
    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es requerido")
    private String email;
}

package com.scrapetok.accounts.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeactivateUserRequestDTO {
    
    @NotNull(message = "ID del usuario es requerido")
    private Long userId;
    
    @NotNull(message = "ID del administrador es requerido")
    private Long adminId;
    
    private String reason;
}

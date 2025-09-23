package com.scrapetok.accounts.domain.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    private String token;
    private String message;
}

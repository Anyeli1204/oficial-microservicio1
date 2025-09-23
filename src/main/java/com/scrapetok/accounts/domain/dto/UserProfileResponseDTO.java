package com.scrapetok.accounts.domain.dto;

import lombok.Data;

@Data
public class UserProfileResponseDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    private String creationDate;
    private AdminProfileResponseDTO adminProfile;
}

package com.scrapetok.accounts.domain.dto;

import lombok.Data;

@Data
public class UpgradeToAdminResponseDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    private String admisionToAdminDate;
    private String admisionToAdminTime;
    private Integer totalQuestionsAnswered;
    private Boolean isActive;
    private String message;
}

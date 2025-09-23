package com.scrapetok.accounts.domain.dto;

import lombok.Data;

@Data
public class AdminProfileResponseDTO {
    private Long id;
    private String admisionToAdminDate;
    private String admisionToAdminTime;
    private Integer totalQuestionsAnswered;
    private Boolean isActive;
}

package com.scrapetok.accounts.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_profiles")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class AdminProfile {
    
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    
    @Column(nullable = false, name = "admision_to_admin_date")
    private LocalDate admisionToAdminDate;
    
    @Column(nullable = false, name = "admision_to_admin_time")
    private LocalTime admisionToAdminTime;
    
    @Column(nullable = false, name = "total_questions_answered")
    private Integer totalQuestionsAnswered = 0;
    
    @Column(nullable = false, name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public void incrementQuestionsAnswered() {
        this.totalQuestionsAnswered++;
    }
    
    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}

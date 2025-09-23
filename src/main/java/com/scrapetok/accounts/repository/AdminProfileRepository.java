package com.scrapetok.accounts.repository;

import com.scrapetok.accounts.domain.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {
    
    Optional<AdminProfile> findByUserEmail(String email);
    
    List<AdminProfile> findByIsActive(Boolean isActive);
    
    @Query("SELECT ap FROM AdminProfile ap WHERE ap.admisionToAdminDate >= :startDate AND ap.admisionToAdminDate <= :endDate")
    List<AdminProfile> findAdminsAdmittedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(ap) FROM AdminProfile ap WHERE ap.isActive = :isActive")
    long countByIsActive(@Param("isActive") Boolean isActive);
    
    @Query("SELECT ap FROM AdminProfile ap ORDER BY ap.totalQuestionsAnswered DESC")
    List<AdminProfile> findTopAdminsByQuestionsAnswered();
}

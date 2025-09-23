package com.scrapetok.accounts.controller;

import com.scrapetok.accounts.domain.dto.*;
import com.scrapetok.accounts.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDTO> userRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        UserSignUpResponseDTO response = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/signupadmin")
    public ResponseEntity<UserSignUpResponseDTO> adminRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        UserSignUpResponseDTO response = authService.createAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserProfileResponseDTO>> getAllUsers() {
        List<UserProfileResponseDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDTO profile = authService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/upgrade-to-admin")
    public ResponseEntity<UpgradeToAdminResponseDTO> upgradeUserToAdmin(@RequestBody @Valid UpgradeToAdminRequestDTO request) {
        UpgradeToAdminResponseDTO response = authService.upgradeUserToAdmin(request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponseDTO> updateUserProfile(@PathVariable Long userId, @RequestBody @Valid UpdateProfileRequestDTO request) {
        UserProfileResponseDTO response = authService.updateUserProfile(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody @Valid ChangePasswordRequestDTO request) {
        authService.changePassword(userId, request);
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/deactivate-user")
    public ResponseEntity<String> deactivateUser(@RequestBody @Valid DeactivateUserRequestDTO request) {
        authService.deactivateUser(request);
        return ResponseEntity.ok("Usuario desactivado exitosamente");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/activate-user/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Long userId, @RequestParam Long adminId) {
        authService.activateUser(userId, adminId);
        return ResponseEntity.ok("Usuario activado exitosamente");
    }
}

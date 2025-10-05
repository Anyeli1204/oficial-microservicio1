package com.scrapetok.accounts.service;

import com.scrapetok.accounts.domain.AdminProfile;
import com.scrapetok.accounts.domain.User;
import com.scrapetok.accounts.domain.dto.*;
import com.scrapetok.accounts.domain.enums.Role;
import com.scrapetok.accounts.exception.EmailAlreadyInUseException;
import com.scrapetok.accounts.exception.ResourceNotFoundException;
import com.scrapetok.accounts.repository.AdminProfileRepository;
import com.scrapetok.accounts.repository.UserRepository;
import com.scrapetok.accounts.security.JwtUtil;
import com.scrapetok.accounts.service.email.EmailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminProfileRepository adminProfileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailService emailService;
    
    public UserSignUpResponseDTO createUser(UserSignUpRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("El email ya está en uso");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EmailAlreadyInUseException("El username ya está en uso");
        }
        
        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.USER);
        
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        
        try {
            User saved = userRepository.save(newUser);
            
            try {
                emailService.sendWelcomeEmail(saved.getEmail(), saved.getFirstname());
            } catch (Exception e) {
                System.out.println("Email de bienvenida no enviado (perfil de test): " + e.getMessage());
            }
            
            UserSignUpResponseDTO response = modelMapper.map(saved, UserSignUpResponseDTO.class);
            response.setRole("USER");
            response.setMessage("Usuario creado exitosamente");
            
            return response;
            
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyInUseException("Error al crear el usuario: " + e.getMessage());
        }
    }
    
    public UserSignUpResponseDTO createAdmin(UserSignUpRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("El email ya está en uso");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EmailAlreadyInUseException("El username ya está en uso");
        }
        
        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.ADMIN);
        
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        
        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setUser(newUser);
        adminProfile.setAdmisionToAdminDate(zonedDateTime.toLocalDate());
        adminProfile.setAdmisionToAdminTime(zonedDateTime.toLocalTime());
        adminProfile.setIsActive(true);
        adminProfile.setTotalQuestionsAnswered(0);
        
        try {
            User savedUser = userRepository.save(newUser);
            AdminProfile savedAdmin = adminProfileRepository.save(adminProfile);
            
            UserSignUpResponseDTO response = modelMapper.map(savedUser, UserSignUpResponseDTO.class);
            response.setRole("ADMIN");
            response.setMessage("Administrador creado exitosamente");
            
            return response;
            
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyInUseException("Error al crear el administrador: " + e.getMessage());
        }
    }
    
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            User usuario = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + request.getEmail()));
            
            if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
                throw new BadCredentialsException("Credenciales inválidas");
            }
            
            if (!usuario.getIsActive()) {
                throw new BadCredentialsException("Usuario desactivado");
            }
            
            String role = usuario.getRole().name();
            String token = jwtUtil.generateToken(usuario.getEmail(), role);
            
            LoginResponseDTO response = modelMapper.map(usuario, LoginResponseDTO.class);
            response.setToken(token);
            response.setRole(role);
            response.setMessage("Login exitoso");
            
            return response;
            
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        } catch (Exception e) {
            throw new RuntimeException("Error interno al autenticar: " + e.getMessage());
        }
    }
    
    public UserProfileResponseDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
        
        UserProfileResponseDTO response = modelMapper.map(user, UserProfileResponseDTO.class);
        
        if (user.isAdmin() && user.getAdminProfile() != null) {
            AdminProfileResponseDTO adminProfile = modelMapper.map(user.getAdminProfile(), AdminProfileResponseDTO.class);
            response.setAdminProfile(adminProfile);
        }
        
        return response;
    }
    
    public UserProfileResponseDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        
        UserProfileResponseDTO response = modelMapper.map(user, UserProfileResponseDTO.class);
        
        if (user.isAdmin() && user.getAdminProfile() != null) {
            AdminProfileResponseDTO adminProfile = modelMapper.map(user.getAdminProfile(), AdminProfileResponseDTO.class);
            response.setAdminProfile(adminProfile);
        }
        
        return response;
    }
    
    public List<UserProfileResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserProfileResponseDTO> dtos = new ArrayList<>();
        
        for (User user : users) {
            UserProfileResponseDTO dto = modelMapper.map(user, UserProfileResponseDTO.class);
            
            if (user.isAdmin() && user.getAdminProfile() != null) {
                AdminProfileResponseDTO adminProfile = modelMapper.map(user.getAdminProfile(), AdminProfileResponseDTO.class);
                dto.setAdminProfile(adminProfile);
            }
            
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    public UpgradeToAdminResponseDTO upgradeUserToAdmin(UpgradeToAdminRequestDTO request) {
        AdminProfile admin = adminProfileRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin con ID " + request.getAdminId() + " no encontrado"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalStateException("⚠️ Este usuario ya es administrador.");
        }

        AdminProfile nuevoAdmin = new AdminProfile();
        nuevoAdmin.setUser(user);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        nuevoAdmin.setAdmisionToAdminDate(zonedDateTime.toLocalDate());
        nuevoAdmin.setAdmisionToAdminTime(zonedDateTime.toLocalTime());
        nuevoAdmin.setIsActive(true);
        nuevoAdmin.setTotalQuestionsAnswered(0);
        
        user.setRole(Role.ADMIN);
        
        adminProfileRepository.save(nuevoAdmin);
        userRepository.save(user);
        
        UpgradeToAdminResponseDTO responseDTO = new UpgradeToAdminResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setFirstname(user.getFirstname());
        responseDTO.setLastname(user.getLastname());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setRole(user.getRole().name());
        responseDTO.setAdmisionToAdminDate(nuevoAdmin.getAdmisionToAdminDate().toString());
        responseDTO.setAdmisionToAdminTime(nuevoAdmin.getAdmisionToAdminTime().toString());
        responseDTO.setTotalQuestionsAnswered(nuevoAdmin.getTotalQuestionsAnswered());
        responseDTO.setIsActive(nuevoAdmin.getIsActive());
        responseDTO.setMessage("Usuario promovido a administrador exitosamente");
        
        return responseDTO;
    }

    public UserProfileResponseDTO updateUserProfile(Long userId, UpdateProfileRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado"));

        // Validar email si se está cambiando
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyInUseException("El email ya está en uso por otro usuario");
            }
        }

        // Validar username si se está cambiando
        if (request.getUsername() != null && !user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalStateException("El username ya está en uso por otro usuario");
            }
        }

        // Actualizar solo los campos que no son null
        if (request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        user.setEmail(request.getEmail()); // Email siempre se actualiza

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserProfileResponseDTO.class);
    }

    public void changePassword(Long userId, ChangePasswordRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas nuevas no coinciden");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void deactivateUser(DeactivateUserRequestDTO request) {
        AdminProfile admin = adminProfileRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin con ID " + request.getAdminId() + " no encontrado"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalStateException("No se puede desactivar a un administrador");
        }

        if (!user.getIsActive()) {
            throw new IllegalStateException("El usuario ya está desactivado");
        }

        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId, Long adminId) {
        AdminProfile admin = adminProfileRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin con ID " + adminId + " no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado"));

        if (user.getIsActive()) {
            throw new IllegalStateException("El usuario ya está activo");
        }

        user.setIsActive(true);
        userRepository.save(user);
    }

    private ZonedDateTime obtenerFechaPeru() {
        return ZonedDateTime.now(ZoneId.of("America/Lima"));
    }
}

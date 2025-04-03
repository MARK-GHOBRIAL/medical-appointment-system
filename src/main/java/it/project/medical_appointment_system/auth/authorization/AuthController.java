package it.project.medical_appointment_system.auth.authorization;

import it.project.medical_appointment_system.auth.app_user.AppUser;
import it.project.medical_appointment_system.auth.app_user.AppUserService;
import it.project.medical_appointment_system.auth.app_user.Role;
import it.project.medical_appointment_system.dto.JwtAuthResponse;
import it.project.medical_appointment_system.dto.UserDTO;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AppUserService appUserService;
    private final ModelMapper modelMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-user")
    public AppUser getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        appUserService.registerUser(
                registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getRole()
        );
        return ResponseEntity.ok("{\"Benvenuto\": \"Registrazione avvenuta con successo\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request:"+ loginRequest.getUsername());

        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        AppUser user = appUserService.loadUserByUsername(loginRequest.getUsername());
        UserDTO userDto = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", userDto));
    }
}

package it.project.medical_appointment_system.auth.authorization;

import it.project.medical_appointment_system.auth.app_user.AppUser;
import it.project.medical_appointment_system.auth.app_user.AppUserService;
import it.project.medical_appointment_system.auth.app_user.Role;
import it.project.medical_appointment_system.dto.JwtAuthResponse;
import it.project.medical_appointment_system.dto.RegistrationResponse;
import it.project.medical_appointment_system.dto.UserDTO;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-user")
    public AppUser getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest registerRequest) {
        AppUser registeredUser = appUserService.registerUser(
                registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getRole()
        );

        // Genera il token dopo la registrazione
        String token = appUserService.authenticateUser(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                authenticationManager
        );

        RegistrationResponse response = new RegistrationResponse(
                "Registrazione avvenuta con successo",
                registeredUser.getEmail(),
                registeredUser.getName(),
                registeredUser.getRole().name(),
                token
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = appUserService.authenticateUser(
                request.getUsername(),
                request.getPassword(),
                authenticationManager
        );
        return ResponseEntity.ok(token);
    }
}



/*
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
        String token = appUserService.authenticateUser(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );
        AppUser user = appUserService.loadUserByUsername(loginRequest.getUsername());
        UserDTO userDto = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", userDto));
    } catch (AuthenticationException e) {
        // Restituisce un JSON di errore invece di lanciare un'eccezione
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of(
                "error", "Login failed",
                "message", "Invalid username or password"
            ));
    }
}
 */

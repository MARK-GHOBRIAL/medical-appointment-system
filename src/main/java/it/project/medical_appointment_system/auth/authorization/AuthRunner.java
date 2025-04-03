package it.project.medical_appointment_system.auth.authorization;


import it.project.medical_appointment_system.auth.app_user.AppUser;
import it.project.medical_appointment_system.auth.app_user.AppUserService;
import it.project.medical_appointment_system.auth.app_user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUserIfNotExists(
                "Admin User",
                "admin@clinica.com",
                "adminpassword",
                Role.ROLE_ADMIN
        );

        createUserIfNotExists(
                "Regular User",
                "user@clinica.com",
                "userpassword",
                Role.ROLE_USER
        );

        createUserIfNotExists(
                "Dr. Mario Rossi",
                "doctor@clinica.com",
                "doctorpassword",
                Role.ROLE_DOCTOR
        );
    }

    private void createUserIfNotExists(String name, String email, String password, Role role) {
        try {
            Optional<AppUser> existingUser = appUserService.findByUsername(email);
            if (existingUser.isEmpty()) {
                appUserService.registerUser(
                        name,
                        email,
                        passwordEncoder.encode(password),
                        role
                );
                log.info("Creato utente {} con ruolo {}", email, role);
            } else {
                log.info("Utente {} già esistente, saltato", email);
            }
        } catch (Exception e) {
            log.error("Errore creazione utente {}: {}", email, e.getMessage());
        }
    }
}


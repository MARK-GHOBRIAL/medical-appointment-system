package it.project.medical_appointment_system.auth.authorization;


import it.project.medical_appointment_system.auth.app_user.AppUser;
import it.project.medical_appointment_system.auth.app_user.AppUserService;
import it.project.medical_appointment_system.auth.app_user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<AppUser> adminUser = appUserService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            appUserService.registerUser("admin", "adminpwd", Role.ROLE_ADMIN);
        }

        // Creazione dell'utente user se non esiste
        Optional<AppUser> normalUser = appUserService.findByUsername("user");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("user", "userpwd", Role.ROLE_USER);
        }

        // Creazione dell'utente doctor se non esiste
        Optional<AppUser> doctorUser = appUserService.findByUsername("doctor");
        if (doctorUser.isEmpty()) {
            appUserService.registerUser("doctor", "doctorpwd", Role.ROLE_DOCTOR);
        }
    }
}


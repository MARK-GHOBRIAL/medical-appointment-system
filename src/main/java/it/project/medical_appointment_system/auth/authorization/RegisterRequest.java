package it.project.medical_appointment_system.auth.authorization;

import it.project.medical_appointment_system.auth.app_user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String password;
    private String email;
    private Role role;

}

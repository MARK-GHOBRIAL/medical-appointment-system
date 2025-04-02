package it.project.medical_appointment_system.dto;


import it.project.medical_appointment_system.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}

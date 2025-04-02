package it.project.medical_appointment_system.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
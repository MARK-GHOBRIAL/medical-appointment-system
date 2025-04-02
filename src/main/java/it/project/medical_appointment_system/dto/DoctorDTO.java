package it.project.medical_appointment_system.dto;


import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialty;
    private String bio;
    private String imageUrl;
    private UserDTO user;
}

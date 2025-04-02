package it.project.medical_appointment_system.dto;


import it.project.medical_appointment_system.model.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Appointment.AppointmentStatus status;
    private UserDTO patient;
    private DoctorDTO doctor;
}

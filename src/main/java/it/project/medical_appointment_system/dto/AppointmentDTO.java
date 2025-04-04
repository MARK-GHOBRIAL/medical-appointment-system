package it.project.medical_appointment_system.dto;


import it.project.medical_appointment_system.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Appointment.AppointmentStatus status;
    private UserDTO patient;
    private DoctorDTO doctor;
}

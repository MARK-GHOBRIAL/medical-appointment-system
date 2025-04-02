package it.project.medical_appointment_system.service;


import it.project.medical_appointment_system.dto.AppointmentDTO;
import it.project.medical_appointment_system.exception.AppointmentConflictException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO bookAppointment(Long doctorId, LocalDate date, LocalTime time)
            throws AppointmentConflictException;
    List<AppointmentDTO> getUserAppointments();
    List<AppointmentDTO> getDoctorAppointments();
    List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date);
    void cancelAppointment(Long appointmentId);
    void completeAppointment(Long appointmentId);
    List<AppointmentDTO> getAllAppointmentsForAdmin();
}
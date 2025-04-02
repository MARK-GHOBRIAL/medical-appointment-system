package it.project.medical_appointment_system.service;


import it.project.medical_appointment_system.model.Appointment;

public interface EmailService {
    void sendAppointmentConfirmation(String to, Appointment appointment);
    void sendAppointmentCancellation(String to, Appointment appointment);
}

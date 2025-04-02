package it.project.medical_appointment_system.service.impl;

import it.project.medical_appointment_system.model.Appointment;
import it.project.medical_appointment_system.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAppointmentConfirmation(String to, Appointment appointment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Confirmation");
        message.setText(String.format(
                "Dear %s,\n\n" +
                        "Your appointment with Dr. %s has been confirmed.\n" +
                        "Date: %s\n" +
                        "Time: %s\n\n" +
                        "Thank you for choosing our service.",
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getDate(),
                appointment.getTime()
        ));
        mailSender.send(message);
    }

    @Override
    public void sendAppointmentCancellation(String to, Appointment appointment) {  // Nota la correzione del nome
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Cancellation");
        message.setText(String.format(
                "Dear %s,\n\n" +
                        "Your appointment with Dr. %s on %s at %s has been cancelled.\n\n" +
                        "Please contact us if you need to reschedule.",
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getDate(),
                appointment.getTime()
        ));
        mailSender.send(message);
    }
}
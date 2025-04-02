package it.project.medical_appointment_system.service.impl;


import it.project.medical_appointment_system.dto.AppointmentDTO;
import it.project.medical_appointment_system.exception.AppointmentConflictException;
import it.project.medical_appointment_system.exception.ResourceNotFoundException;
import it.project.medical_appointment_system.exception.UnauthorizedAccessException;
import it.project.medical_appointment_system.model.Appointment;
import it.project.medical_appointment_system.model.Doctor;
import it.project.medical_appointment_system.model.Role;
import it.project.medical_appointment_system.model.User;
import it.project.medical_appointment_system.repository.AppointmentRepository;
import it.project.medical_appointment_system.repository.DoctorRepository;
import it.project.medical_appointment_system.service.AppointmentService;
import it.project.medical_appointment_system.service.DoctorService;
import it.project.medical_appointment_system.service.EmailService;
import it.project.medical_appointment_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    private static final int APPOINTMENT_DURATION = 30;

    @Override
    public AppointmentDTO bookAppointment(Long doctorId, LocalDate date, LocalTime time)
            throws AppointmentConflictException {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot book appointment in the past");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        if (appointmentRepository.existsByDoctorAndDateAndTime(doctor, date, time)) {
            throw new AppointmentConflictException("This time slot is already booked");
        }

        User patient = userService.getCurrentUser();

        Appointment appointment = new Appointment(date, time, patient, doctor);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send confirmation email
        emailService.sendAppointmentConfirmation(patient.getEmail(), savedAppointment);

        return modelMapper.map(savedAppointment, AppointmentDTO.class);
    }

    @Override
    public List<AppointmentDTO> getUserAppointments() {
        User patient = userService.getCurrentUser();
        return appointmentRepository.findByPatient(patient).stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getDoctorAppointments() {
        User currentUser = userService.getCurrentUser();
        Doctor doctor = doctorRepository.findByUser(currentUser)
                .orElseThrow(() -> new UnauthorizedAccessException("Only doctors can view their appointments"));

        return appointmentRepository.findByDoctor(doctor).stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndDate(doctor, date);
        List<LocalTime> bookedSlots = existingAppointments.stream()
                .map(Appointment::getTime)
                .collect(Collectors.toList());

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime slot = START_TIME;

        while (slot.isBefore(END_TIME)) {
            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
            slot = slot.plusMinutes(APPOINTMENT_DURATION);
        }

        return availableSlots;
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        User currentUser = userService.getCurrentUser();
        if (!currentUser.equals(appointment.getPatient()) &&
                !currentUser.equals(appointment.getDoctor().getUser()) &&
                currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You are not authorized to cancel this appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Send cancellation email
        emailService.sendAppointmentCancellation(appointment.getPatient().getEmail(), appointment);
    }

    @Override
    public void completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        User currentUser = userService.getCurrentUser();
        if (!currentUser.equals(appointment.getDoctor().getUser())) {
            throw new UnauthorizedAccessException("Only the assigned doctor can complete appointments");
        }

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsForAdmin() {
        return appointmentRepository.findAll().stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }
}

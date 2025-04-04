package it.project.medical_appointment_system.controller;


import it.project.medical_appointment_system.dto.AppointmentDTO;
import it.project.medical_appointment_system.dto.DoctorDTO;
import it.project.medical_appointment_system.dto.UserDTO;
import it.project.medical_appointment_system.service.AppointmentService;
import it.project.medical_appointment_system.service.DoctorService;
import it.project.medical_appointment_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersForAdmin() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctorsForAdmin() {
        return ResponseEntity.ok(doctorService.getAllDoctorsForAdmin());
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsForAdmin() {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForAdmin());
    }
}

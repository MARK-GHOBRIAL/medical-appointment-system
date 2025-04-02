package it.project.medical_appointment_system.service;

import it.project.medical_appointment_system.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    List<DoctorDTO> getAllDoctors();
    DoctorDTO getDoctorById(Long id);
    List<DoctorDTO> getAllDoctorsForAdmin();
    void deleteDoctor(Long id);
}

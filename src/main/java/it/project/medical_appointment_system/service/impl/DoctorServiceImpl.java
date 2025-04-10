package it.project.medical_appointment_system.service.impl;

import it.project.medical_appointment_system.auth.app_user.Role;
import it.project.medical_appointment_system.dto.DoctorDTO;
import it.project.medical_appointment_system.exception.ResourceNotFoundException;
import it.project.medical_appointment_system.model.Doctor;
import it.project.medical_appointment_system.model.User;
import it.project.medical_appointment_system.repository.DoctorRepository;
import it.project.medical_appointment_system.repository.UserRepository;
import it.project.medical_appointment_system.service.DoctorService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Logger log = Logger.getLogger(DoctorServiceImpl.class.getName());

    @Override
    @PreAuthorize("hasRole('ADMIN')") // Ensure only admins can access this method
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        User user = userRepository.findById(doctorDTO.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", doctorDTO.getUser().getId()));
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setSpecialty(doctorDTO.getSpecialty());
        doctor.setBio(doctorDTO.getBio());
        doctor.setImageUrl(doctorDTO.getImageUrl());
        doctor.setUser(user);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorDTO.class);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findByUser_Role(Role.DOCTOR).stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        DoctorDTO doctorDTO = modelMapper.map(doctor, DoctorDTO.class);
        log.info("Mapped DoctorDTO: " + doctorDTO);
        return doctorDTO;
    }

    @Override
    public List<DoctorDTO> getAllDoctorsForAdmin() {
        return doctorRepository.findAll().stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        doctorRepository.delete(doctor);
    }
}

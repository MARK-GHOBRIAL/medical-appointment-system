package it.project.medical_appointment_system.repository;


import it.project.medical_appointment_system.model.Doctor;
import it.project.medical_appointment_system.model.Role;
import it.project.medical_appointment_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByUser_Role(Role role);
    Optional<Doctor> findByUser(User user);
}
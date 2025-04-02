package it.project.medical_appointment_system.repository;



import it.project.medical_appointment_system.model.Appointment;
import it.project.medical_appointment_system.model.Doctor;
import it.project.medical_appointment_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByDoctor(Doctor doctor);
    boolean existsByDoctorAndDateAndTime(Doctor doctor, LocalDate date, LocalTime time);
    List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date);
}

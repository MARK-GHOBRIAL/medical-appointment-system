package it.project.medical_appointment_system.service;


import it.project.medical_appointment_system.dto.RegisterDTO;
import it.project.medical_appointment_system.dto.UserDTO;
import it.project.medical_appointment_system.model.User;

import java.util.List;

public interface UserService {
    User registerUser(RegisterDTO registerDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    void deleteUser(Long id);
    User getCurrentUser();
    UserDTO getCurrentUserDTO();
}
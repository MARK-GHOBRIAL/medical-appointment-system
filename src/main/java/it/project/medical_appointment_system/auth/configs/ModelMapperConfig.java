package it.project.medical_appointment_system.auth.configs;

import it.project.medical_appointment_system.dto.DoctorDTO;
import it.project.medical_appointment_system.dto.UserDTO;
import it.project.medical_appointment_system.model.Doctor;
import it.project.medical_appointment_system.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();


        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);


        modelMapper.createTypeMap(Doctor.class, DoctorDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Doctor::getId, DoctorDTO::setId);
                    mapper.map(Doctor::getName, DoctorDTO::setName);
                    mapper.map(Doctor::getSpecialty, DoctorDTO::setSpecialty);
                    mapper.map(Doctor::getBio, DoctorDTO::setBio);
                    mapper.map(Doctor::getImageUrl, DoctorDTO::setImageUrl);

                });


        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.map(User::getId, UserDTO::setId);
                    mapper.map(User::getName, UserDTO::setName);
                    mapper.map(User::getEmail, UserDTO::setEmail);
                    mapper.map(User::getRole, UserDTO::setRole);
                });


        return modelMapper;
    }
}

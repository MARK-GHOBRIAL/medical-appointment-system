package it.project.medical_appointment_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String Token;
    private String tokenType = "Bearer";
    private UserDTO user;

    /* Costruttore alternativo senza tokenType
    public JwtAuthResponse(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }*/
}

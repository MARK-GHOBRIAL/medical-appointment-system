package it.project.medical_appointment_system.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private final Key secretKey;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationInMs;

    // Constructor to initialize secretKey from the application's secret
    public JwtTokenUtil(@Value("${app.jwt.secret}") String secret) {
        // Generate a secure key using the 'secret' from application properties
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Estrae tutti i claims dal token JWT
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // Use the secure key for parsing
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se il token JWT è scaduto
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        try {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> "ROLE_" + role)
                    .collect(Collectors.toList());

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("roles", roles) // Add roles as claim
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                    .signWith(secretKey, SignatureAlgorithm.HS256) // Sign with the secure key
                    .compact();
        } catch (Exception e) {
            System.err.println("Errore durante la generazione del token JWT: " + e.getMessage());
            throw new RuntimeException("Errore nella generazione del token", e);
        }
    }

    // Estrae i ruoli dal token JWT
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("roles", List.class);
    }

    // Valida il token JWT
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

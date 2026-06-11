package com.example.WorkforceIQ.security;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.WorkforceIQ.dto.LoginResponse;
import com.example.WorkforceIQ.dto.UserSummary;
import com.example.WorkforceIQ.entity.Employee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
            "WorkforceIQ-JWT-Secret-Key-Change-In-Production-256bits!!";

    private static final long EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public LoginResponse buildLoginResponse(Employee employee) {
        long expiresAt = System.currentTimeMillis() + EXPIRATION_MS;
        String token = generateToken(employee, expiresAt);

        UserSummary user = new UserSummary();
        user.setId(employee.getId());
        user.setName(employee.getName());
        user.setEmail(employee.getEmail());
        user.setRole(employee.getRole());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresAt(expiresAt);
        response.setUser(user);
        return response;
    }

    public String generateToken(Employee employee, long expiresAtMillis) {
        return Jwts.builder()
                .subject(employee.getEmail())
                .claim("role", employee.getRole())
                .claim("id", employee.getId())
                .issuedAt(new Date())
                .expiration(new Date(expiresAtMillis))
                .signWith(signingKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> rolesFromClaims(Claims claims) {
        String role = claims.get("role", String.class);
        if (role == null || role.isBlank()) {
            return List.of();
        }
        return List.of("ROLE_" + role);
    }
}

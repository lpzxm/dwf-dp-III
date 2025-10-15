package sv.edu.udb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.AuthRequest;
import sv.edu.udb.dto.AuthResponse;
import sv.edu.udb.dto.RegisterRequest;
import sv.edu.udb.model.User;
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest
                                                             authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            var userDetails = (User) authentication.getPrincipal();
            var jwtToken = jwtService.generateToken(userDetails);
            var refreshToken = jwtService.generateRefreshToken(userDetails);
            return ResponseEntity.ok(
                    new AuthResponse(jwtToken, refreshToken)
            );
        }
        throw new UsernameNotFoundException("Credenciales inválidas");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        // Puede venir con comillas si se envía como string JSON
        String tokenValue = refreshToken.trim();
        if (tokenValue.startsWith("\"") && tokenValue.endsWith("\"")) {
            tokenValue = tokenValue.substring(1, tokenValue.length() - 1);
        }

        try {
            String username = jwtService.extractUsername(tokenValue);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // ✅ Llamamos con los dos parámetros requeridos
            if (jwtService.isTokenValid(tokenValue, user)) {
                String newAccessToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest
                                                 registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));//La contraseña se codificará automáticamente
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setAge(registerRequest.getAge());
        return ResponseEntity.ok(userRepository.save(user));
    }
}
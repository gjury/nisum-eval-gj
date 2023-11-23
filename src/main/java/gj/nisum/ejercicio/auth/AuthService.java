package gj.nisum.ejercicio.auth;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gj.nisum.ejercicio.auth.jwt.JwtService;
import gj.nisum.ejercicio.user.Role;
import gj.nisum.ejercicio.user.User;
import gj.nisum.ejercicio.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token=jwtService.getToken(user);
        
        return AuthResponse.builder().token(token).build();
        
    }

    public AuthResponse register(RegisterRequest request) {
        
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode( request.getPassword()))
            .name(request.getName())
            .role(Role.USER)
            .phones(request.getPhones())
            .created(LocalDateTime.now())
            .last_login(LocalDateTime.now())
            .isActive(true)
            .build();

        
            String token=jwtService.getToken(user);

            user.setToken(token);
        
            userRepository.save(user);

        return AuthResponse.builder()
            .token(token)
            .build();
        
    }

}

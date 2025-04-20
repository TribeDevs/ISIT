package ru.isit.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isit.dto.request.JwtRequest;
import ru.isit.dto.request.SignUpRequest;
import ru.isit.dto.response.JwtResponse;
import ru.isit.exception.Exception;
import ru.isit.exception.GlobalExceptionHandler;
import ru.isit.models.ConfirmationToken;
import ru.isit.models.Role;
import ru.isit.repository.UserRepository;
import ru.isit.security.JwtAuthentication;
import ru.isit.models.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("This is username is busy");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("This is email is busy");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setEmail(request.getEmail());
        user.getRoles().add(Role.USER);

        return userRepository.save(user);
    }

    public JwtResponse signIn(@NonNull JwtRequest request) {
        User user = userRepository.findByUsername(request.getLogin())
                .orElseThrow(() -> new Exception(
                        "User with login '" + request.getLogin() + "' not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid login or password!");
        }

        if (!user.isEnable()) {
            throw new Exception("Confirmed your email address!");
        }

        String accessToken  = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(user.getUsername(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }


    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(login)
                        .orElseThrow(() -> new Exception("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(login)
                        .orElseThrow(() -> new Exception("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new Exception("Invalid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
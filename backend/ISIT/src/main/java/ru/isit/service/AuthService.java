package ru.isit.service;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import ru.isit.dto.request.ChangePasswordRequest;
import ru.isit.dto.request.JwtRequest;
import ru.isit.dto.request.SignUpRequest;
import ru.isit.dto.response.JwtResponse;
import ru.isit.exception.Exception;
import ru.isit.models.Role;
import ru.isit.repository.UserRepository;
import ru.isit.security.JwtAuthentication;
import ru.isit.models.User;
import ru.isit.security.JwtProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final ConfirmationCodeService confirmationCodeService;
    private final EmailService emailService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    public JwtResponse signIn(@NonNull JwtRequest request) {
        User user = userRepository.findByEmail(request.getLogin())
                .orElseThrow(() -> new Exception(
                        "User with login '" + request.getLogin() + "' not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid login or password!");
        }

        String accessToken  = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(user.getEmail(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    public User signUp(@NonNull SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("Имя пользователя уже занято!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Почта уже занята!");
        }

        if (!confirmationCodeService.validateCode(request.getEmail(), request.getCode())) {
            throw new Exception("Ошибка проверки кода!");
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

    public void sendConfirmationEmail(String email, String textTitle) throws MessagingException, IOException {
        String confirmationCode = confirmationCodeService.saveConfirmationCode(email);

        String title = "[ISIT] - " + textTitle;

        ClassPathResource resource = new ClassPathResource("templates/email-confirmation.html");
        String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        htmlTemplate = htmlTemplate.replace("[CODE]", confirmationCode);

        emailService.sendMessage(email, title, htmlTemplate);
    }

    public Boolean changePassword(@NonNull ChangePasswordRequest request) {


        return true;
    }


    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(login)
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
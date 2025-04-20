package ru.isit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.models.ConfirmationToken;
import ru.isit.repository.ConfirmationTokenRepository;
import ru.isit.repository.UserRepository;
import ru.isit.service.ConfirmationTokenService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirm")
public class ConfirmationTokenController {
    private final ConfirmationTokenService tokenService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> confirm(@RequestParam String token) {
        ConfirmationToken confirmationToken = tokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Токен не найден"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email уже подтвержден");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenService.deleteConfirmationToken(confirmationToken);
            throw new IllegalStateException("Токен истек");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenService.saveConfirmationToken(confirmationToken);

        confirmationToken.getUser().setEnable(true);
        userRepository.save(confirmationToken.getUser());


        return ResponseEntity.ok("Аккаунт успешно подтвержден");
    }
}
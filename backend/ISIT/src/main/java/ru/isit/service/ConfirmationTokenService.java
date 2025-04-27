package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.models.ConfirmationToken;
import ru.isit.models.User;
import ru.isit.repository.ConfirmationTokenRepository;
import ru.isit.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Токен не найден!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Почта уже подтверждена!");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            confirmationTokenRepository.delete(confirmationToken);
            throw new IllegalStateException("Токен истек");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        User user = confirmationToken.getUser();
        user.setEnable(true);
        userRepository.save(user);
    }
}
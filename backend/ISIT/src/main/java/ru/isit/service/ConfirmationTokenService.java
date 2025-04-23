package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isit.models.ConfirmationToken;
import ru.isit.repository.ConfirmationTokenRepository;
import ru.isit.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;
    private final UserRepository userRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        repository.save(token);
    }

    public void deleteConfirmationToken(ConfirmationToken token) {
        repository.delete(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return repository.findByToken(token);
    }

    public void setConfirmedAtConfirmationToken(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now());
    }

    public void updateUserConfirmationToken(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now());
        token.getUser().setEnable(true);
        userRepository.save(token.getUser());
    }
}
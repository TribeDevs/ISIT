package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isit.models.ConfirmationToken;
import ru.isit.repository.ConfirmationTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;

    public void saveConfirmationToken(ConfirmationToken token) {
        repository.save(token);
    }

    public void deleteConfirmationToken(ConfirmationToken token) {
        repository.delete(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return repository.findByToken(token);
    }
}
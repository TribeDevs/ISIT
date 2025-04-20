package ru.isit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isit.models.ConfirmationToken;
import ru.isit.repository.ConfirmationTokenRepository;
import ru.isit.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;
    private final UserRepository userRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        repository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return repository.findByToken(token);
    }
}
package ru.isit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isit.models.ConfirmationToken;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {
    Optional<ConfirmationToken> findByToken(String token);
}
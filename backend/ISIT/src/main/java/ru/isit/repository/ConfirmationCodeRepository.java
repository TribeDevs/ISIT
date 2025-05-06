package ru.isit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isit.models.ConfirmationCode;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, UUID> {

    Optional<ConfirmationCode> findByCodeAndEmail(String code, String email);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);

}
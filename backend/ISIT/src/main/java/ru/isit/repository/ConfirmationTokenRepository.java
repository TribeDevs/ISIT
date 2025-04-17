package ru.isit.repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    public Option<ConfirmationToken> findByToken;

    public Option<ConfirmationToken> findByUsername;

}

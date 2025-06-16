package ru.isit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Table(name = "codes")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String email;
    private boolean used;

    public ConfirmationCode(
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            String email,
            String code
    ) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.email = email;
        this.code = code;
    }

}
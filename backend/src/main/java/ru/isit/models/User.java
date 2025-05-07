package ru.isit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.isit.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 2, max = 30)
    private String username;

    @Size(min = 8, max = 100)
    private String password;

    @Column(name = "role")
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String avatarUrl;
    private boolean verified = false;

    public UserResponse toResponse() {
        return new UserResponse(
                this.id,
                this.email,
                this.username,
                this.roles,
                this.avatarUrl,
                this.verified,
                this.createdAt
        );
    }

}
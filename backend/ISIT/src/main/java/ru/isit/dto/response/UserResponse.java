package ru.isit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.isit.models.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String username;
    private Set<Role> roles;
    private String avatarUrl;
    private boolean verified;
    private LocalDateTime createdAt;
}
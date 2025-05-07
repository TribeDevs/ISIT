package ru.isit.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static CustomUserDetails generateUserDetails(Claims claims) {
        return CustomUserDetails.builder()
                .id(UUID.fromString(claims.get("id", String.class)))
                .username(claims.getSubject())
                .roles(getRoles(claims))
                .build();
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
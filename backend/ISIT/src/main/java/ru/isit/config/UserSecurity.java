package ru.isit.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.isit.models.CustomUserDetails;

import java.util.UUID;

@Component
public class UserSecurity {
    public boolean checkUserId(Authentication authentication, UUID targetUserId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId().equals(targetUserId);
    }
}
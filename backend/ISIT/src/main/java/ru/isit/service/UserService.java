package ru.isit.service;


import org.springframework.transaction.annotation.Transactional;
import ru.isit.dto.request.SignUpRequest;
import ru.isit.exception.AuthException;
import ru.isit.models.User;
import ru.isit.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isit.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** Grant role */
    @Transactional
    public void grantRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    /** Revoke role */
    @Transactional
    public void revokeRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        boolean removed = user.getRoles().remove(role);
        if (!removed) {
            throw new AuthException("User  does not have role: " + role);
        }

        userRepository.save(user);
    }

    @Transactional
    public boolean verifyUser(UUID userId) { return false; } // TODO

}
package ru.isit.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isit.exception.Exception;
import ru.isit.models.Role;
import ru.isit.models.User;
import ru.isit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void grantRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void revokeRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        boolean removed = user.getRoles().remove(role);
        if (!removed) {
            throw new Exception("User  does not have role: " + role);
        }

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public void setAvatar(UUID userId, String filePath) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatarUrl(filePath);
        userRepository.save(user);
    }

    @Transactional
    public Boolean deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean verifyUser(UUID userId) {
        return false;
    }

}
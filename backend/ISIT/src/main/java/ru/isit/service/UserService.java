package ru.isit.service;


import org.springframework.transaction.annotation.Transactional;
import ru.isit.exception.Exception;
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

    @Transactional
    public User grantRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Transactional
    public User revokeRole(UUID userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        boolean removed = user.getRoles().remove(role);
        if (!removed) {
            throw new Exception("User  does not have role: " + role);
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User updateUser(UUID userId, User userDetails) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setUsername(userDetails.getUsername());
            updatedUser.setEmail(userDetails.getEmail());
            return userRepository.save(updatedUser);
        }
        return null;
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
    public boolean verifyUser(UUID userId) { return false; }

}
package ru.isit.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.isit.config.UserSecurity;
import ru.isit.dto.response.UserResponse;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Role;
import ru.isit.models.User;
import ru.isit.repository.UserRepository;
import ru.isit.security.JwtAuthentication;
import ru.isit.service.UserService;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/${api.version}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserSecurity userSecurity;
    @Value("${jwt.secret.access}")
    private String secret;


    @GetMapping("/me")
    public ResponseEntity<?> getProfileDetails(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<User> user = userRepository.findById(userDetails.getId());

        return ResponseEntity.ok(user.get().toResponse());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user.get().toResponse());

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        System.out.println(id);
        boolean isDeleted = userService.deleteUser(id);
        System.out.println(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/giveRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> grantRole(@PathVariable UUID id, @RequestParam String role) {
        userService.grantRole(id, Role.valueOf(role));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/revokeRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> takeRole(@PathVariable UUID id, @RequestParam String role) {
        userService.revokeRole(id, Role.valueOf(role));
        return ResponseEntity.noContent().build();
    }
}

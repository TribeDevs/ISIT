package ru.isit.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isit.dto.request.LoginRequest;
import ru.isit.dto.response.UserResponse;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Role;
import ru.isit.models.User;
import ru.isit.service.FileStorageService;
import ru.isit.service.TokenBlacklistService;
import ru.isit.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/${api.version}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenBlacklistService blacklistService;
    private final FileStorageService fileStorageService;

    @GetMapping("/check-auth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkAuthorization(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfileDetails(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/upload-avatar")
    @PreAuthorize("@userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<String> uploadAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file) {
        try {
            String filePath = fileStorageService.storeFile(file, id);
            userService.setAvatar(id, filePath);
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload avatar: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/check-sfu")
    @PreAuthorize("@userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<String> checkVerify(@PathVariable UUID id, @RequestBody LoginRequest request) {
        if (userService.verifyUser(id, request)) {
            return ResponseEntity.ok("Аккаунт верифицирован!");
        }
        return ResponseEntity.status(500).body("Не получилось верифицировать аккаунт");
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.badRequest().body("Токен не может быть пустым!");
        }
        blacklistService.addToBlacklist(token);
        return ResponseEntity.ok("Выход прошел успешно!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

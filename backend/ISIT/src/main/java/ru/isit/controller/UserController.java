package ru.isit.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isit.dto.request.ChangePasswordRequest;
import ru.isit.dto.request.ChangeUsernameRequest;
import ru.isit.dto.request.LoginRequest;
import ru.isit.dto.response.EmptyResponse;
import ru.isit.dto.response.UserResponse;
import ru.isit.models.CustomUserDetails;
import ru.isit.models.Role;
import ru.isit.models.User;
import ru.isit.service.AuthService;
import ru.isit.service.FileStorageService;
import ru.isit.service.TokenBlacklistService;
import ru.isit.service.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/${api.version}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenBlacklistService blacklistService;
    private final FileStorageService fileStorageService;
    private final AuthService authService;

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
    public ResponseEntity<?> uploadAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file) {
        try {
            String filePath = fileStorageService.storeFile(file, id);
            userService.setAvatar(id, filePath);
            return ResponseEntity.ok(Collections.emptyMap());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Collections.emptyMap());
        }
    }

    @PostMapping("/{id}/check-sfu")
    @PreAuthorize("@userSecurity.checkUserId(authentication, #id)")
    public ResponseEntity<?> checkVerify(@PathVariable UUID id, @RequestBody LoginRequest request) {
        if (userService.verifyUser(id, request)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body(Collections.emptyMap());
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
            return ResponseEntity.badRequest().body(Collections.emptyMap());
        }
        blacklistService.addToBlacklist(token);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);

        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeUsernameRequest request) {
        userService.changeUsername(userDetails, request);
        return ResponseEntity.ok(Collections.emptyMap());
    }


    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

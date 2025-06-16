package ru.isit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.dto.request.ChangePasswordRequest;
import ru.isit.dto.request.JwtRequest;
import ru.isit.dto.request.RefreshJwtRequest;
import ru.isit.dto.request.SignUpRequest;
import ru.isit.dto.response.EmptyResponse;
import ru.isit.dto.response.JwtResponse;
import ru.isit.service.AuthService;

import java.util.Collections;


@RestController
@RequestMapping("/api/${api.version}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(201).body(Collections.emptyMap());
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid JwtRequest request) {
        final JwtResponse token = authService.signIn(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody @Valid RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody @Valid RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
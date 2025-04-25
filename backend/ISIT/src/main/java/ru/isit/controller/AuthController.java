package ru.isit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.dto.request.JwtRequest;
import ru.isit.dto.request.RefreshJwtRequest;
import ru.isit.dto.request.SignUpRequest;
import ru.isit.dto.response.JwtResponse;
import ru.isit.models.ConfirmationToken;
import ru.isit.models.User;
import ru.isit.service.AuthService;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/${api.version}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        User user = authService.signUp(request);
        authService.sendConfirmationEmail(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid JwtRequest request) {
        final JwtResponse token = authService.signIn(request);

        return ResponseEntity.ok(token);
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
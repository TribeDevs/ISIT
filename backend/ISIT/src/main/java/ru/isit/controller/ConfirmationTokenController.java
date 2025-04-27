package ru.isit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.service.ConfirmationTokenService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/auth")
public class ConfirmationTokenController {
    private final ConfirmationTokenService tokenService;

    @GetMapping
    public ResponseEntity<String> confirm(@RequestParam String token) {
        tokenService.confirmToken(token);
        return ResponseEntity.ok("Аккаунт успешно подтвержден!");
    }

}
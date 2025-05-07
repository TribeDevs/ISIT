package ru.isit.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isit.dto.request.SendCodeRequest;
import ru.isit.service.AuthService;

import java.io.IOException;


@RestController
@RequestMapping("/api/${api.version}/send-code")
@RequiredArgsConstructor
public class SendCodeController {

    private final AuthService authService;

    @PostMapping()
    public ResponseEntity<?> sendCode(@RequestBody @Valid SendCodeRequest request) throws MessagingException, IOException {
        authService.sendConfirmationEmail(request.getEmail(), "Подтверждение почты");
        return ResponseEntity.ok().body("На вашу почту отправлено письмо для подтверждения!");
    }

}
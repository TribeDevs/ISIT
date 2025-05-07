package ru.isit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
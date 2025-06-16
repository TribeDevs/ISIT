package ru.isit.dto.response;

import lombok.Data;

@Data
public class ChangeUsernameResponse {
    private String message;
    private String token;
}

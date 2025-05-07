package ru.isit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String code;

    @Size(min = 8, max = 100)
    private String newPassword;



}
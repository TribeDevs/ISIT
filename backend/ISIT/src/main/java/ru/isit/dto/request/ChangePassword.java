package ru.isit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassword {

    @NotBlank
    private String email;

    @NotBlank
    private String oldPassword;

    @Size(min = 8, max = 100)
    private String newPassword;

}
package ru.isit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendCodeRequest {

    @NotBlank
    private String email;

}
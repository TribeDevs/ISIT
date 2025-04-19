package ru.isit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ExceptionModel {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;
}

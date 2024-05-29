package com.zadziarnouski.habitordie.habit.exception;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zadziarnouski.common.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    private final ObjectMapper objectMapper;

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(buildMessage(ex))
                .path(request.getRequestURI())
                .build();

        log.error("Error response: {}", error);

        return error;
    }

    @SneakyThrows
    private String buildMessage(MethodArgumentNotValidException ex) {
        Map<String, String> messageMap = ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));

        return objectMapper.writeValueAsString(messageMap);
    }
}

package com.zadziarnouski.habitordie.habit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@Validated
@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
    private final ObjectMapper objectMapper;

    @GetMapping
    List<HabitDto> getAllHabits() {
        return habitService.getAllHabits();
    }

    @GetMapping("/{id}")
    public HabitDto getHabitById(@PathVariable Long id) {
        return habitService.getHabitById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public HabitDto createHabit(@Valid @RequestBody HabitDto habitDto) {
        return habitService.createHabit(habitDto);
    }

    @PutMapping("/{id}")
    public HabitDto updateHabit(@PathVariable Long id, @Valid @RequestBody HabitDto habitDto) {
        return habitService.updateHabit(id, habitDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                          HttpServletRequest request) {
        Map<String, Object> errorResponse = buildErrorResponse(buildMessage(ex), request);

        log.error("Error response: {}", errorResponse);

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    private Map<String, Object> buildErrorResponse(String message, HttpServletRequest request) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", BAD_REQUEST.value());
        errorResponse.put("error", BAD_REQUEST.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        return errorResponse;
    }

    @SneakyThrows
    private String buildMessage(MethodArgumentNotValidException ex) {
        Map<String, String> messageMap = ex.getBindingResult().getFieldErrors()
                .stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));

        return objectMapper.writeValueAsString(messageMap);
    }
}

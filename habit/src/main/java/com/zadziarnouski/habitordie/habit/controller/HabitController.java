package com.zadziarnouski.habitordie.habit.controller;

import com.zadziarnouski.habitordie.habit.dto.HabitDto;
import com.zadziarnouski.habitordie.habit.exception.ErrorResponse;
import com.zadziarnouski.habitordie.habit.service.HabitService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@Validated
@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
@Tag(name = "Habits", description = "Operations related to habit resource")
public class HabitController {

    private final HabitService habitService;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Found the habits", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = HabitDto.class)))}
            ),
            @ApiResponse(responseCode = "200", description = "Habits not found", content = @Content)

    })
    @GetMapping
    List<HabitDto> getAllHabits() {
        return habitService.getAllHabits();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Found the habit", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HabitDto.class))),
            @ApiResponse(
                    responseCode = "404", description = "Habit not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public HabitDto getHabitById(@PathVariable Long id) {
        return habitService.getHabitById(id);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Habit created", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HabitDto.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public HabitDto createHabit(@Valid @RequestBody HabitDto habitDto) {
        return habitService.createHabit(habitDto);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Habit updated", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HabitDto.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404", description = "Habit not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public HabitDto updateHabit(@PathVariable Long id, @Valid @RequestBody HabitDto habitDto) {
        return habitService.updateHabit(id, habitDto);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Habit deleted", content = @Content),
            @ApiResponse(
                    responseCode = "404", description = "Habit not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }
}

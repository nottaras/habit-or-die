package com.zadziarnouski.habitordie.habit.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record HabitDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Frequency is required")
        String frequency)
{}

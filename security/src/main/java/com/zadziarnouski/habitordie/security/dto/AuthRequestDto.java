package com.zadziarnouski.habitordie.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequestDto(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password) {}

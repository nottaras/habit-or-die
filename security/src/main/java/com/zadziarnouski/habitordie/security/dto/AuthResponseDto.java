package com.zadziarnouski.habitordie.security.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponseDto(
        @NotBlank(message = "UserId is required") Long userId, @NotBlank(message = "Token is required") String token) {}

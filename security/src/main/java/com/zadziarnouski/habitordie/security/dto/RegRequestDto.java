package com.zadziarnouski.habitordie.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegRequestDto(

        @NotBlank(message = "Firstname is required")
        String firstname,

        @NotBlank(message = "Lastname is required")
        String lastname,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {
}

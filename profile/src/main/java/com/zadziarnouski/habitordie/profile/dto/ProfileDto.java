package com.zadziarnouski.habitordie.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfileDto(
        @NotBlank(message = "Firstname is required") String firstname,
        @NotBlank(message = "Lastname is required") String lastname,
        String avatarFileId) {}

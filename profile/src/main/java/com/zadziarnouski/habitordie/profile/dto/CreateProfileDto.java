package com.zadziarnouski.habitordie.profile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateProfileDto(@NotNull Long userId, @NotNull ProfileDto profileDto) {}

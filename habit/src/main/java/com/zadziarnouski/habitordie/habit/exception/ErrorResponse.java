package com.zadziarnouski.habitordie.habit.exception;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private int status;

    @NotNull
    private String error;

    @NotNull
    private String message;

    @NotNull
    private String path;
}

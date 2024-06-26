package com.zadziarnouski.habitordie.security.controller;

import com.zadziarnouski.common.dto.ErrorResponseDto;
import com.zadziarnouski.habitordie.security.dto.AuthRequestDto;
import com.zadziarnouski.habitordie.security.dto.AuthResponseDto;
import com.zadziarnouski.habitordie.security.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user registration and authentication")
public class AuthController {

    private final AuthService authService;

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody AuthRequestDto request) {
        return authService.register(request);
    }

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @PostMapping("/authenticate")
    public AuthResponseDto authenticate(@Valid @RequestBody AuthRequestDto request) {
        return authService.authenticate(request);
    }
}

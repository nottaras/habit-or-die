package com.zadziarnouski.habitordie.profile.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.zadziarnouski.common.dto.ErrorResponseDto;
import com.zadziarnouski.habitordie.profile.dto.CreateProfileDto;
import com.zadziarnouski.habitordie.profile.dto.ProfileDto;
import com.zadziarnouski.habitordie.profile.service.ProfileService;
import com.zadziarnouski.habitordie.profile.validation.ValidMultipartFile;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Operations related to profile resource")
public class ProfileController {

    private final ProfileService profileService;

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Found the profiles", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProfileDto.class)))}
            ),
            @ApiResponse(responseCode = "200", description = "Profiles not found", content = @Content)

    })
    //@spotless:on
    @GetMapping
    List<ProfileDto> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Found the profile", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileDto.class))),
            @ApiResponse(
                    responseCode = "404", description = "Profile not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @GetMapping("/{id}")
    public ProfileDto getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Profile created", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileDto.class))),
        @ApiResponse(
                    responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @PostMapping
    @ResponseStatus(CREATED)
    public ProfileDto createProfile(@Valid @RequestBody CreateProfileDto createProfileDto) {
        return profileService.createProfile(createProfileDto);
    }

    // @spotless:off
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", description = "Avatar successfully uploaded", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ProfileDto.class))),
        @ApiResponse(
            responseCode = "404", description = "Profile not found", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(
            responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @PostMapping("/{id}/avatar")
    @ResponseStatus(CREATED)
    public void uploadAvatar(@PathVariable Long id, @ValidMultipartFile @RequestParam("file") MultipartFile file) {
        profileService.uploadAvatar(id, file);
    }

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Profile updated", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileDto.class))),
            @ApiResponse(
                    responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "404", description = "Profile not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @PutMapping("/{id}")
    public ProfileDto updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileDto profileDto) {
        return profileService.updateProfile(id, profileDto);
    }

    // @spotless:off
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Profile deleted", content = @Content),
            @ApiResponse(
                    responseCode = "404", description = "Profile not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    //@spotless:on
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}

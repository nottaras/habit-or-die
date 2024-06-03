package com.zadziarnouski.habitordie.profile.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.zadziarnouski.habitordie.profile.dto.CreateProfileDto;
import com.zadziarnouski.habitordie.profile.dto.ProfileDto;
import com.zadziarnouski.habitordie.profile.entity.Profile;
import com.zadziarnouski.habitordie.profile.mapper.ProfileMapper;
import com.zadziarnouski.habitordie.profile.repository.ProfileRepository;
import com.zadziarnouski.habitordie.security.entity.Role;
import com.zadziarnouski.habitordie.security.entity.User;
import com.zadziarnouski.habitordie.security.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    public static final Long PROFILE_ID = 1L;
    public static final Long USER_ID = 1L;

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProfileMapper profileMapper;

    @Test
    void givenProfileInRepository_whenGetAllProfiles_thenReturnsListOfProfileDtos() {
        // Given
        var profile = Profile.builder()
                .id(PROFILE_ID)
                .firstname("Foo")
                .lastname("Bar")
                .build();
        var profileDto = ProfileDto.builder().firstname("Foo").lastname("Bar").build();

        when(profileRepository.findAll()).thenReturn(List.of(profile));
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        // When
        List<ProfileDto> allProfiles = profileService.getAllProfiles();

        // Then
        assertEquals(1, allProfiles.size());
        assertEquals(profileDto, allProfiles.get(0));
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void givenProfileExists_whenGetProfileById_thenReturnProfileDto() {
        // Given
        var profile = Profile.builder()
                .id(PROFILE_ID)
                .firstname("Foo")
                .lastname("Bar")
                .build();
        var profileDto = ProfileDto.builder().firstname("Foo").lastname("Bar").build();

        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        // When
        ProfileDto result = profileService.getProfileById(PROFILE_ID);

        // Then
        assertEquals(profileDto, result);
        verify(profileRepository, times(1)).findById(PROFILE_ID);
        verify(profileMapper, times(1)).toDto(profile);
    }

    @Test
    void givenProfileNotFound_whenGetProfileById_thenThrowResponseStatusException() {
        // Given
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> profileService.getProfileById(PROFILE_ID));
        verify(profileRepository, times(1)).findById(PROFILE_ID);
        verifyNoInteractions(profileMapper);
    }

    @Test
    void givenCreateProfileDto_whenCreateProfile_thenSaveProfileAndReturnProfileDto() {
        // Given
        var user = new User("email", "password", Role.USER);
        var profile = Profile.builder()
                .id(PROFILE_ID)
                .firstname("Foo")
                .lastname("Bar")
                .build();
        var profileDto =
                ProfileDto.builder().firstname("firstname").lastname("lastname").build();
        var createProfileDto = CreateProfileDto.builder()
                .userId(USER_ID)
                .profileDto(profileDto)
                .build();

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(profileMapper.toEntity(profileDto)).thenReturn(profile);
        when(profileRepository.save(profile)).thenReturn(profile);
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        // When
        ProfileDto result = profileService.createProfile(createProfileDto);

        // Then
        assertEquals(profileDto, result);
        verify(userService, times(1)).getUserById(USER_ID);
        verify(profileMapper, times(1)).toEntity(profileDto);
        verify(profileRepository, times(1)).save(profile);
        verify(profileMapper, times(1)).toDto(profile);
    }

    @Test
    void givenCreateProfileDtoWithNonExistentUserId_whenCreateProfile_thenThrowResponseStatusException() {
        // Given
        var profileDto =
                ProfileDto.builder().firstname("firstname").lastname("lastname").build();
        var createProfileDto = CreateProfileDto.builder()
                .userId(USER_ID)
                .profileDto(profileDto)
                .build();

        when(userService.getUserById(any())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> profileService.createProfile(createProfileDto));
        verify(userService, times(1)).getUserById(any());
        verifyNoInteractions(profileMapper, profileRepository);
    }

    @Test
    void givenExistingHabit_whenUpdateHabit_thenUpdateAndReturnProfileDto() {
        // Given
        var existingProfile = Profile.builder()
                .id(PROFILE_ID)
                .firstname("Foo")
                .lastname("Bar")
                .build();
        var updatedProfile = Profile.builder()
                .id(PROFILE_ID)
                .firstname("New")
                .lastname("New")
                .build();
        var updatedProfileDto =
                ProfileDto.builder().firstname("New").lastname("New").build();

        updatedProfile.setId(existingProfile.getId());

        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(updatedProfile)).thenReturn(updatedProfile);
        when(profileMapper.toDto(updatedProfile)).thenReturn(updatedProfileDto);

        // When
        ProfileDto result = profileService.updateProfile(PROFILE_ID, updatedProfileDto);

        // Then
        assertEquals(updatedProfileDto, result);
        verify(profileRepository, times(1)).findById(PROFILE_ID);
        verify(profileRepository, times(1)).save(updatedProfile);
        verify(profileMapper, times(1)).toDto(updatedProfile);
    }

    @Test
    void givenNonExistingProfile_whenUpdateProfile_thenThrowResponseStatusException() {
        // Given
        var profileDto = ProfileDto.builder().firstname("Foo").lastname("Bar").build();
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> profileService.updateProfile(PROFILE_ID, profileDto));
        verify(profileRepository, times(1)).findById(PROFILE_ID);
        verifyNoInteractions(profileMapper);
    }
}

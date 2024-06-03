package com.zadziarnouski.habitordie.profile.service;

import com.zadziarnouski.habitordie.profile.dto.CreateProfileDto;
import com.zadziarnouski.habitordie.profile.dto.ProfileDto;
import com.zadziarnouski.habitordie.profile.entity.Profile;
import com.zadziarnouski.habitordie.profile.exception.NotFoundException;
import com.zadziarnouski.habitordie.profile.mapper.ProfileMapper;
import com.zadziarnouski.habitordie.profile.repository.ProfileRepository;
import com.zadziarnouski.habitordie.security.service.UserService;
import com.zadziarnouski.habitordie.storage.service.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    public static final String PROFILE_NOT_FOUND_MESSAGE = "Profile with id %d not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final StorageService storageService;

    public List<ProfileDto> getAllProfiles() {
        var profiles = profileRepository.findAll();
        log.info("Retrieved {} profiles from the database", profiles.size());
        return profiles.stream().map(profileMapper::toDto).toList();
    }

    public ProfileDto getProfileById(Long id) {
        return profileRepository.findById(id).map(profileMapper::toDto).orElseThrow(() -> handleProfileNotFound(id));
    }

    @Transactional
    public ProfileDto createProfile(CreateProfileDto createProfileDto) {
        return userService
                .getUserById(createProfileDto.userId())
                .map(user -> {
                    var profile = profileMapper.toEntity(createProfileDto.profileDto());
                    profile.setUser(user);
                    var savedProfile = profileRepository.save(profile);
                    log.info("Profile with id {} created", savedProfile.getId());
                    return profileMapper.toDto(savedProfile);
                })
                .orElseThrow(() -> handleUserNotFound(createProfileDto.userId()));
    }

    @Transactional
    public ProfileDto updateProfile(Long id, ProfileDto profileDto) {
        return profileRepository
                .findById(id)
                .map(existingProfile -> {
                    existingProfile.setFirstname(profileDto.firstname());
                    existingProfile.setLastname(profileDto.lastname());

                    return profileRepository.save(existingProfile);
                })
                .map(profileMapper::toDto)
                .orElseThrow(() -> handleProfileNotFound(id));
    }

    @Transactional
    public void deleteProfile(Long id) {
        var profile = profileRepository.findById(id).orElseThrow(() -> handleProfileNotFound(id));

        profileRepository.delete(profile);
        log.info("Profile with id {} deleted successfully", id);
    }

    public void uploadAvatar(Long id, MultipartFile file) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> handleProfileNotFound(id));

        String avatarId = storageService.store(file);
        profile.setAvatarFileId(avatarId);
        profileRepository.save(profile);
    }

    private ResponseStatusException handleProfileNotFound(Long id) {
        var errorMessage = PROFILE_NOT_FOUND_MESSAGE.formatted(id);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }

    private ResponseStatusException handleUserNotFound(Long id) {
        var errorMessage = USER_NOT_FOUND_MESSAGE.formatted(id);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}

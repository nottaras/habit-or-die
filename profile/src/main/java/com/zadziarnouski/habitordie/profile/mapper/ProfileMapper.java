package com.zadziarnouski.habitordie.profile.mapper;

import com.zadziarnouski.habitordie.profile.dto.ProfileDto;
import com.zadziarnouski.habitordie.profile.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "avatarFileId", source = "avatarFileId")
    Profile toEntity(ProfileDto profileDto);
}

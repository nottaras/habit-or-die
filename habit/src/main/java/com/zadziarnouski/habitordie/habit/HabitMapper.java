package com.zadziarnouski.habitordie.habit;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitMapper {

    HabitDto toDto(Habit habit);

    @Mapping(target = "id", ignore = true)
    Habit toEntity(HabitDto habitDto);
}

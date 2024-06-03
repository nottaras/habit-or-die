package com.zadziarnouski.habitordie.habit.mapper;

import com.zadziarnouski.habitordie.habit.dto.HabitDto;
import com.zadziarnouski.habitordie.habit.entity.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitMapper {

    HabitDto toDto(Habit habit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Habit toEntity(HabitDto habitDto);
}

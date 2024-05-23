package com.zadziarnouski.habitordie.habit.service;

import com.zadziarnouski.habitordie.habit.dto.HabitDto;
import com.zadziarnouski.habitordie.habit.exception.NotFoundException;
import com.zadziarnouski.habitordie.habit.mapper.HabitMapper;
import com.zadziarnouski.habitordie.habit.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitService {

    public static final String HABIT_NOT_FOUND_MESSAGE = "Habit with id %d not found";

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    public List<HabitDto> getAllHabits() {
        var habits = habitRepository.findAll();
        log.info("Retrieved {} habits from the database", habits.size());
        return habits.stream()
                .map(habitMapper::toDto)
                .toList();
    }

    public HabitDto getHabitById(Long id) {
        return habitRepository.findById(id)
                .map(habitMapper::toDto)
                .orElseThrow(() -> handleHabitNotFound(id));
    }

    @Transactional
    public HabitDto createHabit(HabitDto habitDto) {
        var savedHabit = habitRepository.save(habitMapper.toEntity(habitDto));
        log.info("Habit with id {} created", savedHabit.getId());
        return habitMapper.toDto(savedHabit);
    }

    @Transactional
    public HabitDto updateHabit(Long id, HabitDto habitDto) {
        return habitRepository.findById(id)
                .map(existingHabit -> {
                    existingHabit.setName(habitDto.name());
                    existingHabit.setDescription(habitDto.description());
                    existingHabit.setFrequency(habitDto.frequency());

                    return habitRepository.save(existingHabit);
                })
                .map(habitMapper::toDto)
                .orElseThrow(() -> handleHabitNotFound(id));
    }

    @Transactional
    public void deleteHabit(Long id) {
        var habit = habitRepository.findById(id)
                .orElseThrow(() -> handleHabitNotFound(id));

        habitRepository.delete(habit);
        log.info("Habit with id {} deleted successfully", id);
    }

    private ResponseStatusException handleHabitNotFound(Long id) {
        var errorMessage = HABIT_NOT_FOUND_MESSAGE.formatted(id);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}

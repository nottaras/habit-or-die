package com.zadziarnouski.habitordie.habit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitService {

    public static final String HABIT_NOT_FOUND_MESSAGE = "Habit with id %d not found";

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    List<HabitDto> getAllHabits() {
        return habitRepository.findAll().stream()
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
        Habit savedHabit = habitRepository.save(habitMapper.toEntity(habitDto));
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
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> handleHabitNotFound(id));

        habitRepository.delete(habit);
    }

    private ResponseStatusException handleHabitNotFound(Long id) {
        log.error(HABIT_NOT_FOUND_MESSAGE.formatted(id));
        return new ResponseStatusException(NOT_FOUND, HABIT_NOT_FOUND_MESSAGE.formatted(id));
    }
}

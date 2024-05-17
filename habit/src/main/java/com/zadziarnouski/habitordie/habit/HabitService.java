package com.zadziarnouski.habitordie.habit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HabitService {

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
                .orElse(null);
    }

    @Transactional
    public HabitDto createHabit(HabitDto habitDto) {
        Habit saved = habitRepository.save(habitMapper.toEntity(habitDto));
        return habitMapper.toDto(saved);
    }

    @Transactional
    public HabitDto updateHabit(Long id, HabitDto habitDto) {
        Optional<Habit> optionalHabit = habitRepository.findById(id);

        if (optionalHabit.isPresent()) {
            Habit habit = optionalHabit.get();
            habit.setName(habitDto.name());
            habit.setDescription(habitDto.description());
            habit.setFrequency(habitDto.frequency());

            return habitMapper.toDto(habitRepository.save(habit));
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteHabit(Long id) {
        habitRepository.findById(id)
                .ifPresent(habitRepository::delete);
    }
}

package com.zadziarnouski.habitordie.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    public static final Long HABIT_ID = 1L;
    @InjectMocks
    private HabitService habitService;

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitMapper habitMapper;

    @Test
    void givenHabitsInRepository_whenGetAllHabits_thenReturnsListOfHabitDtos() {
        // Given
        List<Habit> habits = createHabits();
        List<HabitDto> habitDtos = createHabitDtos();

        when(habitRepository.findAll()).thenReturn(habits);
        when(habitMapper.toDto(habits.getFirst())).thenReturn(habitDtos.getFirst());
        when(habitMapper.toDto(habits.getLast())).thenReturn(habitDtos.getLast());

        // When
        List<HabitDto> allHabits = habitService.getAllHabits();

        // Then
        assertEquals(2, habits.size());
        assertEquals(habitDtos.getFirst(), allHabits.getFirst());
        assertEquals(habitDtos.getLast(), allHabits.getLast());
        verify(habitRepository, times(1)).findAll();
    }

    @Test
    void givenHabitExists_whenGetHabitById_thenReturnHabitDto() {
        // Given
        Habit habit = createHabits().getFirst();
        HabitDto habitDto = createHabitDtos().getFirst();

        when(habitRepository.findById(HABIT_ID)).thenReturn(Optional.of(habit));
        when(habitMapper.toDto(habit)).thenReturn(habitDto);

        // When
        HabitDto result = habitService.getHabitById(HABIT_ID);

        // Then
        assertEquals(habitDto, result);
        verify(habitRepository, times(1)).findById(HABIT_ID);
        verify(habitMapper, times(1)).toDto(habit);
    }

    @Test
    void givenHabitNotFound_whenGetHabitById_thenThrowResponseStatusException() {
        // Given
        when(habitRepository.findById(HABIT_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> habitService.getHabitById(HABIT_ID));
        verify(habitRepository, times(1)).findById(HABIT_ID);
        verifyNoInteractions(habitMapper);
    }

    @Test
    void givenHabitDto_whenCreateHabit_thenSaveHabitAndReturnHabitDto() {
        // Given
        Habit habit = createHabits().getFirst();
        HabitDto habitDto = createHabitDtos().getFirst();

        when(habitMapper.toEntity(habitDto)).thenReturn(habit);
        when(habitRepository.save(habit)).thenReturn(habit);
        when(habitMapper.toDto(habit)).thenReturn(habitDto);

        // When
        HabitDto result = habitService.createHabit(habitDto);

        // Then
        assertEquals(habitDto, result);
        verify(habitMapper, times(1)).toEntity(habitDto);
        verify(habitRepository, times(1)).save(habit);
        verify(habitMapper, times(1)).toDto(habit);
    }

//    ?????
    @Test
    void givenExistingHabit_whenUpdateHabit_thenUpdateAndReturnHabitDto() {
        // Given
        HabitDto updatedHabitDto = createHabitDtos().getLast();
        Habit existingHabit = createHabits().getFirst();
        Habit updatedHabit = createHabits().getLast();
        updatedHabit.setId(existingHabit.getId());

        when(habitRepository.findById(HABIT_ID)).thenReturn(Optional.of(existingHabit));
        when(habitRepository.save(updatedHabit)).thenReturn(updatedHabit);
        when(habitMapper.toDto(updatedHabit)).thenReturn(updatedHabitDto);

        // When
        HabitDto result = habitService.updateHabit(HABIT_ID, updatedHabitDto);

        // Then
        assertEquals(updatedHabitDto, result);
        verify(habitRepository, times(1)).findById(HABIT_ID);
        verify(habitRepository, times(1)).save(updatedHabit);
        verify(habitMapper, times(1)).toDto(updatedHabit);
    }

    @Test
    void givenNonExistingHabit_whenUpdateHabit_thenThrowResponseStatusException() {
        // Given
        when(habitRepository.findById(HABIT_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResponseStatusException.class, () -> habitService.updateHabit(HABIT_ID, createHabitDtos().getLast()));
        verify(habitRepository, times(1)).findById(HABIT_ID);
        verifyNoInteractions(habitMapper);
    }

    private List<Habit> createHabits() {
        Habit habit1 = Habit.builder()
                .id(1L)
                .name("Habit 1")
                .description("Description 1")
                .frequency("Daily")
                .build();

        Habit habit2 = Habit.builder()
                .id(2L)
                .name("Habit 2")
                .description("Description 2")
                .frequency("Weekly")
                .build();

        return Arrays.asList(habit1, habit2);
    }

    private List<HabitDto> createHabitDtos() {
        HabitDto habitDto1 = new HabitDto.HabitDtoBuilder()
                .name("Habit 1")
                .description("Description 1")
                .frequency("Daily")
                .build();

        HabitDto habitDto2 = new HabitDto.HabitDtoBuilder()
                .name("Habit 2")
                .description("Description 2")
                .frequency("Weekly")
                .build();

        return Arrays.asList(habitDto1, habitDto2);
    }
}
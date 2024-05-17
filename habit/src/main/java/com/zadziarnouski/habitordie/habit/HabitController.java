package com.zadziarnouski.habitordie.habit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @GetMapping
    List<HabitDto> getAllHabits() {
        return habitService.getAllHabits();
    }

    @GetMapping("/{id}")
    public HabitDto getHabitById(@PathVariable Long id) {
        return habitService.getHabitById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitDto createHabit(@RequestBody HabitDto habitDto) {
        return habitService.createHabit(habitDto);
    }

    @PutMapping("/{id}")
    public HabitDto updateHabit(@PathVariable Long id, @RequestBody HabitDto habitDto) {
        return habitService.updateHabit(id, habitDto);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }
}

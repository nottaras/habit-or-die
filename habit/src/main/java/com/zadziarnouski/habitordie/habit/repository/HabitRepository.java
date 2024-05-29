package com.zadziarnouski.habitordie.habit.repository;

import com.zadziarnouski.habitordie.habit.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {}

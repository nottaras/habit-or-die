package com.zadziarnouski.habitordie.security.service;

import com.zadziarnouski.habitordie.security.entity.User;
import com.zadziarnouski.habitordie.security.exception.NotFoundException;
import com.zadziarnouski.habitordie.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User with email %s not found";

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> handleHabitNotFound(email));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    private ResponseStatusException handleHabitNotFound(String email) {
        var errorMessage = USER_NOT_FOUND_MESSAGE.formatted(email);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}

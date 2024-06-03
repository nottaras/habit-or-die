package com.zadziarnouski.habitordie.profile.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(String reason) {
        super(NOT_FOUND, reason);
    }
}

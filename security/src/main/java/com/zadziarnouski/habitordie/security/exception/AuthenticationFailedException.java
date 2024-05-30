package com.zadziarnouski.habitordie.security.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.server.ResponseStatusException;

public class AuthenticationFailedException extends ResponseStatusException {

    public AuthenticationFailedException() {
        super(NOT_FOUND, "User not found");
    }
}

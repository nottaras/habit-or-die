package com.zadziarnouski.habitordie.security.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AuthenticationFailedException extends ResponseStatusException {

    public AuthenticationFailedException() {
        super(NOT_FOUND, "User not found");
    }
}

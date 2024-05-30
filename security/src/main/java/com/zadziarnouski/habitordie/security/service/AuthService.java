package com.zadziarnouski.habitordie.security.service;

import static com.zadziarnouski.habitordie.security.entity.Role.USER;

import com.zadziarnouski.habitordie.security.dto.AuthRequestDto;
import com.zadziarnouski.habitordie.security.dto.AuthResponseDto;
import com.zadziarnouski.habitordie.security.dto.RegRequestDto;
import com.zadziarnouski.habitordie.security.entity.User;
import com.zadziarnouski.habitordie.security.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegRequestDto request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(USER)
                .build();
        userService.save(user);

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponseDto(jwtToken);
    }

    public AuthResponseDto authenticate(AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            throw new AuthenticationFailedException();
        }

        var user = userService.getUserByEmail(request.email());
        var jwtToken = jwtService.generateToken(user);

        return new AuthResponseDto(jwtToken);
    }
}

package com.itteam.estatesapi.rest;

import com.itteam.estatesapi.config.ActualUIDomainConfig;
import com.itteam.estatesapi.exception.DuplicatedUserInfoException;
import com.itteam.estatesapi.model.User;
import com.itteam.estatesapi.rest.dto.AuthResponse;
import com.itteam.estatesapi.rest.dto.LoginRequest;
import com.itteam.estatesapi.rest.dto.SignUpRequest;
import com.itteam.estatesapi.security.TokenProvider;
import com.itteam.estatesapi.security.WebSecurityConfig;
import com.itteam.estatesapi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final SignUpService signUpService;
    private final ActualUIDomainConfig actualUIDomainConfig;

    @PostMapping("/authenticate")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = signUpService.authenticateAndGetToken(loginRequest.getUsername(), loginRequest.getPassword());
        return new AuthResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return new AuthResponse(signUpService.signUpUser(signUpRequest));
    }

    @GetMapping(path = "/confirm")
    public void confirm(@RequestParam("emailToken") String emailToken, HttpServletResponse response) throws IOException {
        try {
            signUpService.confirmToken(emailToken);
            String redirectUrl = actualUIDomainConfig.getACTUAL_UI_DOMAIN() + "/emailconfirm";
            response.sendRedirect(redirectUrl);
        } catch (IllegalStateException e) {
            // Handle the exceptions and redirect accordingly
            String errorRedirectUrl = actualUIDomainConfig.getACTUAL_UI_DOMAIN() + "/emailconfirm/error";
            response.sendRedirect(errorRedirectUrl);
        }
    }
}

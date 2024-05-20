package com.itteam.estatesapi.rest;

import com.itteam.estatesapi.config.ActualAPIDomainConfig;
import com.itteam.estatesapi.config.ActualUIDomainConfig;
import com.itteam.estatesapi.email.EmailSender;
import com.itteam.estatesapi.email.EmailService;
import com.itteam.estatesapi.exception.DuplicatedUserInfoException;
import com.itteam.estatesapi.model.User;
import com.itteam.estatesapi.repository.UserRepository;
import com.itteam.estatesapi.rest.dto.SignUpRequest;
import com.itteam.estatesapi.security.TokenProvider;
import com.itteam.estatesapi.security.WebSecurityConfig;
import com.itteam.estatesapi.security.email.EmailConfirmationToken;
import com.itteam.estatesapi.security.email.EmailConfirmationTokenService;
import com.itteam.estatesapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SignUpService {

    private final ActualAPIDomainConfig actualAPIDomainConfig;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ActualUIDomainConfig actualUIDomainConfig;

    public String signUpUser(SignUpRequest signUpRequest){
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.getUsername()));
        }
        if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.getEmail()));
        }

        User user = mapSignUpRequestToUser(signUpRequest);

        userService.saveUser(user);

        String emailToken = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
                emailToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        emailConfirmationTokenService.saveEmailConfirmationToken(emailConfirmationToken);

        sendConfirmationEmail(signUpRequest.getEmail(), emailToken);

        String token = authenticateAndGetToken(signUpRequest.getUsername(), signUpRequest.getPassword());
        return token;
    }

    private void sendConfirmationEmail(String email, String token) {
        String confirmationLink = "http://"+ actualAPIDomainConfig.getACTUAL_API_DOMAIN() + "/auth/confirm?emailToken=" + token;
        String emailBody = emailService.buildEmail(email, confirmationLink);
        emailSender.send(email, emailBody);
    }




    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }

    public User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(WebSecurityConfig.USER);
        user.setIsEmailConfirmed(false);
        return user;
    }

    @Transactional
    public void confirmToken(String emailToken) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService
                .getToken(emailToken)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (emailConfirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = emailConfirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        emailConfirmationTokenService.setConfirmedAt(emailToken);
        confirmUser(emailConfirmationToken.getUser().getEmail());

    }

    @Transactional
    public void confirmUser(String email) {
        int updatedRows = userRepository.confirmUserByEmail(email);
        if (updatedRows != 1) {
            throw new IllegalStateException("Failed to confirm user email");
        }
    }
}

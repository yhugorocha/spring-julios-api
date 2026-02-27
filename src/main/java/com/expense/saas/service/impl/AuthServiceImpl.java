package com.expense.saas.service.impl;

import com.expense.saas.domain.User;
import com.expense.saas.domain.enums.UserRole;
import com.expense.saas.dto.auth.AuthResponse;
import com.expense.saas.dto.auth.LoginRequest;
import com.expense.saas.dto.auth.SignupRequest;
import com.expense.saas.exception.BusinessException;
import com.expense.saas.exception.UnauthorizedException;
import com.expense.saas.repository.UserRepository;
import com.expense.saas.security.AppUserPrincipal;
import com.expense.saas.security.JwtTokenService;
import com.expense.saas.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        var normalizedEmail = request.email().strip().toLowerCase(Locale.ROOT);
        if (this.userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("Email already registered.");
        }

        var user = User.builder()
                .name(request.name().strip())
                .email(normalizedEmail)
                .password(this.passwordEncoder.encode(request.password()))
                .role(UserRole.USER)
                .active(Boolean.TRUE)
                .build();
        this.userRepository.save(user);

        return this.buildAuthResponse(user.getId(), user.getRole(), user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            var authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email().strip(), request.password())
            );
            var principal = (AppUserPrincipal) authentication.getPrincipal();
            return this.buildAuthResponse(principal.getUserId(), principal.getRole(), principal.getUsername());
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid credentials.");
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Authentication failed.");
        }
    }

    private AuthResponse buildAuthResponse(
            java.util.UUID userId,
            UserRole role,
            String email
    ) {
        var accessToken = this.jwtTokenService.generateToken(userId, role, email);
        var expiresAt = Instant.now().plusMillis(this.jwtTokenService.getExpirationMs());
        return new AuthResponse(accessToken, "Bearer", expiresAt, userId, role);
    }
}

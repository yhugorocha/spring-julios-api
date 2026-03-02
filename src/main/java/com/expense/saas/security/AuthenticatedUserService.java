package com.expense.saas.security;

import com.expense.saas.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticatedUserService {

    public AuthenticatedUser requireAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Autenticação obrigatória.");
        }

        var principal = authentication.getPrincipal();
        if (!(principal instanceof AuthenticatedUser authenticatedUser)) {
            throw new UnauthorizedException("Autenticação obrigatória.");
        }
        return authenticatedUser;
    }

    public UUID requireUserId() {
        return this.requireAuthenticatedUser().userId();
    }
}

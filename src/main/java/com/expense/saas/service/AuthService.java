package com.expense.saas.service;

import com.expense.saas.dto.auth.AuthResponse;
import com.expense.saas.dto.auth.LoginRequest;
import com.expense.saas.dto.auth.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}

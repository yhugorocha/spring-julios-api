package com.expense.saas.service.impl;

import com.expense.saas.domain.User;
import com.expense.saas.dto.user.UserCreateRequest;
import com.expense.saas.dto.user.UserResponse;
import com.expense.saas.exception.BusinessException;
import com.expense.saas.repository.UserRepository;
import com.expense.saas.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        var normalizedEmail = request.email().strip().toLowerCase(Locale.ROOT);
        if (this.userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        var user = User.builder()
                .name(request.name().strip())
                .email(normalizedEmail)
                .password(this.passwordEncoder.encode(request.password()))
                .role(request.role())
                .active(Boolean.TRUE)
                .build();

        this.userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return this.userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}

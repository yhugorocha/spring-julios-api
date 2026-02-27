package com.expense.saas.service;

import com.expense.saas.dto.user.UserCreateRequest;
import com.expense.saas.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    List<UserResponse> list();
}

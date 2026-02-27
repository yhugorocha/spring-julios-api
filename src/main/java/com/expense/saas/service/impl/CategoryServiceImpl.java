package com.expense.saas.service.impl;

import com.expense.saas.domain.Category;
import com.expense.saas.dto.category.CategoryCreateRequest;
import com.expense.saas.dto.category.CategoryResponse;
import com.expense.saas.exception.BusinessException;
import com.expense.saas.exception.ResourceNotFoundException;
import com.expense.saas.repository.CategoryRepository;
import com.expense.saas.repository.UserRepository;
import com.expense.saas.security.AuthenticatedUserService;
import com.expense.saas.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        var userId = this.authenticatedUserService.requireUserId();
        var categoryName = request.name().strip();
        if (this.categoryRepository.existsByNameIgnoreCaseAndTypeAndCreatedBy_Id(categoryName, request.type(), userId)) {
            throw new BusinessException("A category with the same name and type already exists.");
        }

        var currentUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        var category = Category.builder()
                .name(categoryName)
                .type(request.type())
                .createdBy(currentUser)
                .build();

        this.categoryRepository.save(category);
        return CategoryResponse.fromEntity(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        var userId = this.authenticatedUserService.requireUserId();
        return this.categoryRepository.findAllByCreatedBy_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }
}

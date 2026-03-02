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
import java.util.UUID;

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
            throw new BusinessException("Já existe uma categoria com o mesmo nome e tipo.");
        }

        var currentUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));

        var category = Category.builder()
                .name(categoryName)
                .type(request.type())
                .active(true)
                .createdBy(currentUser)
                .build();

        this.categoryRepository.save(category);
        return CategoryResponse.fromEntity(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        var userId = this.authenticatedUserService.requireUserId();
        return this.categoryRepository.findAllByCreatedBy_IdAndActiveTrueOrderByCreatedAtDesc(userId)
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void deactivate(UUID categoryId) {
        var userId = this.authenticatedUserService.requireUserId();
        var category = this.categoryRepository.findByIdAndCreatedBy_Id(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        if (!category.isActive()) {
            return;
        }

        category.setActive(false);
        this.categoryRepository.save(category);
    }
}

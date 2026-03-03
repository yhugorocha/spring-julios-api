package com.expense.saas.service;

import com.expense.saas.dto.category.CategoryCreateRequest;
import com.expense.saas.dto.category.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse create(CategoryCreateRequest request);

    List<CategoryResponse> list();

    void activate(UUID categoryId);

    void deactivate(UUID categoryId);
}

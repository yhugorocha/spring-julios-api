package com.expense.saas.service;

import com.expense.saas.dto.category.CategoryCreateRequest;
import com.expense.saas.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryCreateRequest request);

    List<CategoryResponse> list();
}

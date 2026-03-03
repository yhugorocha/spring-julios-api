package com.expense.saas.controller;

import com.expense.saas.dto.category.CategoryCreateRequest;
import com.expense.saas.dto.category.CategoryResponse;
import com.expense.saas.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(this.categoryService.list());
    }

    @PatchMapping("/{categoryId}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID categoryId) {
        this.categoryService.activate(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{categoryId}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID categoryId) {
        this.categoryService.deactivate(categoryId);
        return ResponseEntity.noContent().build();
    }
}

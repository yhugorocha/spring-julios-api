package com.expense.saas.repository;

import com.expense.saas.domain.Category;
import com.expense.saas.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCaseAndTypeAndCreatedBy_Id(String name, CategoryType type, UUID createdBy);

    Optional<Category> findByIdAndCreatedBy_Id(UUID id, UUID createdBy);

    Optional<Category> findByIdAndCreatedBy_IdAndActiveTrue(UUID id, UUID createdBy);

    List<Category> findAllByCreatedBy_IdAndActiveTrueOrderByCreatedAtDesc(UUID createdBy);
}

package com.expense.saas.repository;

import com.expense.saas.domain.ExpenseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseTransactionRepository extends JpaRepository<ExpenseTransaction, UUID> {

    List<ExpenseTransaction> findAllByCreatedBy_IdAndDateBetweenOrderByDateDescCreatedAtDesc(
            UUID createdBy,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<ExpenseTransaction> findByIdAndCreatedBy_Id(UUID id, UUID createdBy);
}

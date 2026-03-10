package com.expense.saas.service.impl;

import com.expense.saas.domain.ExpenseTransaction;
import com.expense.saas.dto.transaction.TransactionAmountUpdateRequest;
import com.expense.saas.dto.transaction.TransactionCreateRequest;
import com.expense.saas.dto.transaction.TransactionPaidUpdateRequest;
import com.expense.saas.dto.transaction.TransactionResponse;
import com.expense.saas.exception.BusinessException;
import com.expense.saas.exception.ResourceNotFoundException;
import com.expense.saas.repository.CategoryRepository;
import com.expense.saas.repository.ExpenseTransactionRepository;
import com.expense.saas.repository.UserRepository;
import com.expense.saas.security.AuthenticatedUserService;
import com.expense.saas.service.ExpenseTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExpenseTransactionServiceImpl implements ExpenseTransactionService {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final ExpenseTransactionRepository expenseTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    @Transactional
    public TransactionResponse create(TransactionCreateRequest request) {
        var userId = this.authenticatedUserService.requireUserId();
        var category = this.categoryRepository.findByIdAndCreatedBy_IdAndActiveTrue(request.categoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada ou inativa."));

        var currentUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));

        var transaction = ExpenseTransaction.builder()
                .description(request.description().strip())
                .amount(request.amount())
                .date(request.date())
                .paid(false)
                .category(category)
                .createdBy(currentUser)
                .build();

        this.expenseTransactionRepository.save(transaction);
        return TransactionResponse.fromEntity(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> list(String month) {
        var userId = this.authenticatedUserService.requireUserId();
        var referenceMonth = this.resolveReferenceMonth(month);
        var startDate = referenceMonth.atDay(1);
        var endDate = referenceMonth.atEndOfMonth();

        return this.expenseTransactionRepository
                .findAllByCreatedBy_IdAndDateBetweenOrderByDateDescCreatedAtDesc(userId, startDate, endDate)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public TransactionResponse updateAmount(UUID transactionId, TransactionAmountUpdateRequest request) {
        var transaction = this.findOwnedTransaction(transactionId);
        transaction.setAmount(request.amount());
        this.expenseTransactionRepository.save(transaction);
        return TransactionResponse.fromEntity(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse updatePaid(UUID transactionId, TransactionPaidUpdateRequest request) {
        var transaction = this.findOwnedTransaction(transactionId);
        transaction.setPaid(request.paid());
        this.expenseTransactionRepository.save(transaction);
        return TransactionResponse.fromEntity(transaction);
    }

    @Override
    @Transactional
    public void delete(UUID transactionId) {
        var transaction = this.findOwnedTransaction(transactionId);
        this.expenseTransactionRepository.delete(transaction);
    }

    private ExpenseTransaction findOwnedTransaction(UUID transactionId) {
        var userId = this.authenticatedUserService.requireUserId();
        return this.expenseTransactionRepository.findByIdAndCreatedBy_Id(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada."));
    }

    private YearMonth resolveReferenceMonth(String month) {
        if (month == null || month.isBlank()) {
            return YearMonth.now();
        }

        try {
            return YearMonth.parse(month.strip(), MONTH_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new BusinessException("Mês inválido. Use o formato yyyy-MM.");
        }
    }
}

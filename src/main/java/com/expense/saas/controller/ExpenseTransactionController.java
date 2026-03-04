package com.expense.saas.controller;

import com.expense.saas.dto.transaction.TransactionCreateRequest;
import com.expense.saas.dto.transaction.TransactionAmountUpdateRequest;
import com.expense.saas.dto.transaction.TransactionResponse;
import com.expense.saas.service.ExpenseTransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class ExpenseTransactionController {

    private final ExpenseTransactionService expenseTransactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.expenseTransactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(@RequestParam(required = false) String month) {
        return ResponseEntity.ok(this.expenseTransactionService.list(month));
    }

    @PatchMapping("/{transactionId}/amount")
    public ResponseEntity<TransactionResponse> updateAmount(
            @PathVariable UUID transactionId,
            @Valid @RequestBody TransactionAmountUpdateRequest request
    ) {
        return ResponseEntity.ok(this.expenseTransactionService.updateAmount(transactionId, request));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> delete(@PathVariable UUID transactionId) {
        this.expenseTransactionService.delete(transactionId);
        return ResponseEntity.noContent().build();
    }
}

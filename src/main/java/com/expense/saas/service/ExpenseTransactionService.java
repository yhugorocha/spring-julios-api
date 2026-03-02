package com.expense.saas.service;

import com.expense.saas.dto.transaction.TransactionCreateRequest;
import com.expense.saas.dto.transaction.TransactionResponse;
import com.expense.saas.dto.transaction.TransactionAmountUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseTransactionService {

    TransactionResponse create(TransactionCreateRequest request);

    List<TransactionResponse> list();

    TransactionResponse updateAmount(UUID transactionId, TransactionAmountUpdateRequest request);

    void delete(UUID transactionId);
}

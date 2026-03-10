package com.expense.saas.service;

import com.expense.saas.dto.transaction.TransactionCreateRequest;
import com.expense.saas.dto.transaction.TransactionResponse;
import com.expense.saas.dto.transaction.TransactionAmountUpdateRequest;
import com.expense.saas.dto.transaction.TransactionPaidUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseTransactionService {

    TransactionResponse create(TransactionCreateRequest request);

    List<TransactionResponse> list(String month);

    TransactionResponse updateAmount(UUID transactionId, TransactionAmountUpdateRequest request);

    TransactionResponse updatePaid(UUID transactionId, TransactionPaidUpdateRequest request);

    void delete(UUID transactionId);
}

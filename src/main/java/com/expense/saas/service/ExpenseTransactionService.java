package com.expense.saas.service;

import com.expense.saas.dto.transaction.TransactionCreateRequest;
import com.expense.saas.dto.transaction.TransactionResponse;

import java.util.List;

public interface ExpenseTransactionService {

    TransactionResponse create(TransactionCreateRequest request);

    List<TransactionResponse> list();
}

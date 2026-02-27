package com.expense.saas.exception;

public record ApiFieldError(
        String field,
        String message
) {
}

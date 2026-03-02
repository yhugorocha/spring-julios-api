package com.expense.saas.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toApiFieldError)
                .toList();

        var response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                this.getStatusMessage(HttpStatus.BAD_REQUEST),
                "Falha na validação da requisição.",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        var fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new ApiFieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        var response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                this.getStatusMessage(HttpStatus.BAD_REQUEST),
                "Falha na validação da requisição.",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(
            BusinessException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class})
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        if (exception instanceof BadCredentialsException) {
            return buildError(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", request.getRequestURI());
        }
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.", request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildError(HttpStatus status, String message, String path) {
        var response = ApiErrorResponse.of(
                status.value(),
                this.getStatusMessage(status),
                message,
                path,
                List.of()
        );
        return ResponseEntity.status(status).body(response);
    }

    private String getStatusMessage(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "Requisição inválida";
            case UNAUTHORIZED -> "Não autorizado";
            case FORBIDDEN -> "Acesso negado";
            case NOT_FOUND -> "Recurso não encontrado";
            case UNPROCESSABLE_ENTITY -> "Regra de negócio";
            case INTERNAL_SERVER_ERROR -> "Erro interno do servidor";
            default -> "Erro";
        };
    }

    private ApiFieldError toApiFieldError(FieldError fieldError) {
        return new ApiFieldError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}

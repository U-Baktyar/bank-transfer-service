package com.aiylbank.backend.exception.handler;

import com.aiylbank.backend.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request) {
        log.warn("AccountNotFoundException на {}: {}", request.getRequestURI(), ex.getMessage());
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "Account Not Found", request);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ProblemDetail handleInvalidDate(InvalidDateException ex, HttpServletRequest request) {
        log.warn("InvalidDateException на {}: {}", request.getRequestURI(), ex.getMessage());
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid Date Range", request);
    }

    @ExceptionHandler(SelfTransferException.class)
    public ProblemDetail handleSelfTransfer(SelfTransferException ex, HttpServletRequest request){
        log.warn("SelfTransferException на {}: {}", request.getRequestURI(), ex.getMessage());
        return createProblemDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), "Self Transfer Error", request);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ProblemDetail handleSelfTransfer(AccountStatusException ex, HttpServletRequest request){
        log.warn("AccountStatusException на {}: {}", request.getRequestURI(), ex.getMessage());
        return createProblemDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), "Self Transfer Error", request);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ProblemDetail handleSelfTransfer(NotEnoughMoneyException ex, HttpServletRequest request){
        log.warn("NotEnoughMoneyException на {}: {}", request.getRequestURI(), ex.getMessage());
        return createProblemDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), "Self Transfer Error", request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Необработанная ошибка на {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Проблема с подключением к серверу, пожалуйста, повторите попытку позже.",
                "Server Error",
                request
        );
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String title, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("time", Instant.now());
        return pd;
    }
}
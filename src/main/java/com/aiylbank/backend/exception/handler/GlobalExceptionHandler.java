package com.aiylbank.backend.exception.handler;

import com.aiylbank.backend.exception.AccountNotFoundException;
import com.aiylbank.backend.exception.InvalidDateException;
import com.aiylbank.backend.exception.SelfTransferException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "Account Not Found", request);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ProblemDetail handleInvalidDate(InvalidDateException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid Date Range", request);
    }

    @ExceptionHandler(SelfTransferException.class)
    public ProblemDetail handleSelfTransfer(SelfTransferException ex, HttpServletRequest request){
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "Self Transfer Error", request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();

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

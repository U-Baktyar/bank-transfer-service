package com.aiylbank.backend.dto;


import java.util.List;

public record AccountStatementDto(
        String accountNumber,
        List<ViewTransactionDto> transactions,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages
) {}

package com.aiylbank.backend.dto;

import com.aiylbank.backend.entity.OperationType;
import com.aiylbank.backend.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ViewTransactionDto(
        OffsetDateTime operationDate,
        TransactionStatus status,
        OperationType operationType,
        BigDecimal amount,
        BigDecimal balanceAfter) {}

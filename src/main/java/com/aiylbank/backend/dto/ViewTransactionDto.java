package com.aiylbank.backend.dto;

import com.aiylbank.backend.entity.OperationType;
import com.aiylbank.backend.entity.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(
        name = "ViewTransactionDto",
        description = "Информация о транзакции счета"
)
public record ViewTransactionDto(

        @Schema(description = "Дата и время операции", example = "2026-03-11T15:30:00+06:00")
        OffsetDateTime operationDate,

        @Schema(description = "Статус транзакции", example = "SUCCESS")
        TransactionStatus status,

        @Schema(description = "Тип операции", example = "DEPOSIT")
        OperationType operationType,

        @Schema(description = "Сумма операции", example = "1500.00")
        BigDecimal amount,

        @Schema(description = "Баланс после операции", example = "5000.50")
        BigDecimal balanceAfter

) {}
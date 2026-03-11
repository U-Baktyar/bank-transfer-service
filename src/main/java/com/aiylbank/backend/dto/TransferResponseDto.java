package com.aiylbank.backend.dto;

import com.aiylbank.backend.entity.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(
        name = "TransferResponseDto",
        description = "Ответ после выполнения перевода"
)
public record TransferResponseDto(

        @Schema(description = "Статус перевода", example = "SUCCESS")
        TransactionStatus status,

        @Schema(description = "Сумма перевода", example = "1000.50")
        BigDecimal amount,

        @Schema(description = "Дата и время выполнения перевода", example = "2026-03-11T17:00:00+06:00")
        OffsetDateTime executedAt

) {}
package com.aiylbank.backend.dto;

import com.aiylbank.backend.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransferResponseDto(TransactionStatus status, BigDecimal amount, OffsetDateTime executedAt) {}

package com.aiylbank.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateTransferDto(
        @NotBlank(message = "Номер счета отправителя обязателен")
        @Size(min = 20, max = 20, message = "Номер счета должен содержать 20 символов")
        String senderAccountNumber,

        @NotBlank(message = "Номер счета получателя обязателен")
        @Size(min = 20, max = 20, message = "Номер счета должен содержать 20 символов")
        String receiverAccountNumber,

        @NotNull(message = "Сумма перевода обязательна")
        @Positive(message = "Сумма перевода должна быть больше нуля")
        BigDecimal amount
) {}
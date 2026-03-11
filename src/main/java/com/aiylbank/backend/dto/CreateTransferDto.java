package com.aiylbank.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(
        name = "CreateTransferDto",
        description = "Данные для создания перевода между счетами"
)
public record CreateTransferDto(

        @Schema(description = "Счет отправителя", example = "ACC00000000000000003")
        @NotBlank(message = "Номер счета отправителя обязателен")
        @Size(min = 20, max = 20, message = "Номер счета должен содержать 20 символов")
        String senderAccountNumber,

        @Schema(description = "Счет получателя", example = "ACC00000000000000002")
        @NotBlank(message = "Номер счета получателя обязателен")
        @Size(min = 20, max = 20, message = "Номер счета должен содержать 20 символов")
        String receiverAccountNumber,

        @Schema(description = "Сумма перевода", example = "100.00")
        @NotNull(message = "Сумма перевода обязательна")
        @Positive(message = "Сумма перевода должна быть больше нуля")
        BigDecimal amount

) {}
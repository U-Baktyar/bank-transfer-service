package com.aiylbank.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
        name = "AccountStatementDto",
        description = "Выписка по счету с информацией о транзакциях и пагинацией"
)
public record AccountStatementDto(
        @Schema(description = "Номер счета", example = "ACC00000000000000003")
        String accountNumber,

        @Schema(description = "Список транзакций", implementation = ViewTransactionDto.class)
        List<ViewTransactionDto> transactions,

        @Schema(description = "Номер страницы", example = "0")
        Integer page,

        @Schema(description = "Размер страницы", example = "10")
        Integer size,

        @Schema(description = "Общее количество элементов", example = "25")
        Long totalElements,

        @Schema(description = "Общее количество страниц", example = "3")
        Integer totalPages
) {}
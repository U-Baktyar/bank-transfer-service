package com.aiylbank.backend.mapper;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.CreateTransferDto;
import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.entity.Account;
import com.aiylbank.backend.entity.Transaction;
import com.aiylbank.backend.entity.TransactionStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;


public interface TransferMapper {
    Transaction toEntity(
            Account sender,
            Account receiver,
            TransactionStatus status,
            BigDecimal amount,
            BigDecimal senderBalanceAfter,
            BigDecimal receiverBalanceAfter);

    TransferResponseDto toResponseDto(Transaction transactional);

    AccountStatementDto toAccountStatementDto(Page<Transaction> page, String accountNo);


}

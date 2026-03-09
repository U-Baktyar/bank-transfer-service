package com.aiylbank.backend.mapper.impl;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.dto.ViewTransactionDto;
import com.aiylbank.backend.entity.Account;
import com.aiylbank.backend.entity.OperationType;
import com.aiylbank.backend.entity.Transaction;
import com.aiylbank.backend.entity.TransactionStatus;
import com.aiylbank.backend.mapper.TransferMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferMapperImpl implements TransferMapper {
    @Override
    public Transaction toEntity(
            Account sender,
            Account receiver,
            TransactionStatus status,
            BigDecimal amount,
            BigDecimal senderBalanceAfter,
            BigDecimal receiverBalanceAfter
    ) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setStatus(status);
        transaction.setAmount(amount);
        transaction.setSenderBalanceAfter(senderBalanceAfter);
        transaction.setReceiverBalanceAfter(receiverBalanceAfter);
        return transaction;
    }

    @Override
    public TransferResponseDto toResponseDto(Transaction transaction) {

        return new TransferResponseDto(
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );    }

    @Override
    public AccountStatementDto toAccountStatementDto(Page<Transaction> page, String accountNumber) {
        List<ViewTransactionDto> transactionDto = toListViewTransactionDto(page.getContent(), accountNumber);

        return new AccountStatementDto(
                accountNumber,
                transactionDto,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private List<ViewTransactionDto> toListViewTransactionDto(List<Transaction> transactionList, String accountNumber){
        List<ViewTransactionDto> transactionDto = new ArrayList<>();
        for (Transaction transaction : transactionList){
            OperationType operationType;
            if (transaction.getSender().getAccountNo().equals(accountNumber)) {
                operationType = OperationType.TRANSFER;
            } else {
                operationType = OperationType.DEPOSIT;
            }
            transactionDto.add(new ViewTransactionDto(
                    transaction.getCreatedAt(),
                    transaction.getStatus(),
                    operationType,
                    transaction.getAmount(),
                    transaction.getSenderBalanceAfter()));
        }
        return transactionDto;
    };


}

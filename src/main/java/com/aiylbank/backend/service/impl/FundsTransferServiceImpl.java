package com.aiylbank.backend.service.impl;

import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.entity.Account;
import com.aiylbank.backend.entity.AccountStatus;
import com.aiylbank.backend.entity.Transaction;
import com.aiylbank.backend.entity.TransactionStatus;
import com.aiylbank.backend.exception.AccountNotFoundException;
import com.aiylbank.backend.mapper.TransferMapper;

import com.aiylbank.backend.repository.AccountRepository;
import com.aiylbank.backend.repository.TransactionRepository;
import com.aiylbank.backend.service.FundsTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FundsTransferServiceImpl implements FundsTransferService {

    private final TransferMapper transferMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public FundsTransferServiceImpl(
            AccountRepository accountRepository,
            TransferMapper transferMapper,
            TransactionRepository transactionRepository
    ){
        this.accountRepository = accountRepository;
        this.transferMapper = transferMapper;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransferResponseDto transferFunds(String senderAccountNumber,
                                             String receiverAccountNumber,
                                             BigDecimal amount) {

        Account sender = accountRepository.findByAccountNo(senderAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Счёт отправителя не найден"));

        Account receiver = accountRepository.findByAccountNo(receiverAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Счёт получателя не найден"));

        TransactionStatus transactionStatus = isTransferAllowed(sender, receiver, amount)
                ? TransactionStatus.SUCCESS
                : TransactionStatus.FAILED;

        BigDecimal senderBalanceAfter = sender.getBalance();
        BigDecimal receiverBalanceAfter = receiver.getBalance();

        if (transactionStatus != TransactionStatus.FAILED){
            senderBalanceAfter =  senderBalanceAfter.subtract(amount);
            receiverBalanceAfter =  receiverBalanceAfter.add(amount);
        }

        Transaction transaction = transferMapper.toEntity(
                sender,
                receiver,
                transactionStatus,
                amount,
                senderBalanceAfter,
                receiverBalanceAfter
        );

        sender.setBalance(senderBalanceAfter);
        receiver.setBalance(receiverBalanceAfter);
        transactionRepository.save(transaction);
        return transferMapper.toResponseDto(transaction);
    }

    private boolean isTransferAllowed(Account sender, Account receiver, BigDecimal amount) {
        return sender.getStatus() == AccountStatus.ACTIVE &&
                receiver.getStatus() == AccountStatus.ACTIVE &&
                sender.getBalance().compareTo(amount) >= 0;
    }
}
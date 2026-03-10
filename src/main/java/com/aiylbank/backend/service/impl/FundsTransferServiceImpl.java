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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FundsTransferServiceImpl implements FundsTransferService {

    private static final Logger log = LoggerFactory.getLogger(FundsTransferServiceImpl.class);

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

        log.info("Начало перевода {} с {} на {}", amount, senderAccountNumber, receiverAccountNumber);

        Account sender = accountRepository.findByAccountNo(senderAccountNumber)
                .orElseThrow(() -> {
                    log.warn("Счёт отправителя {} не найден", senderAccountNumber);
                    return new AccountNotFoundException("Счёт отправителя не найден");
                });

        Account receiver = accountRepository.findByAccountNo(receiverAccountNumber)
                .orElseThrow(() -> {
                    log.warn("Счёт получателя {} не найден", receiverAccountNumber);
                    return new AccountNotFoundException("Счёт получателя не найден");
                });

        TransactionStatus transactionStatus = isTransferAllowed(sender, receiver, amount)
                ? TransactionStatus.SUCCESS
                : TransactionStatus.FAILED;
    
            log.info("Статус транзакции: {}", transactionStatus);
    
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
    
            log.info("Транзакция {} завершена. Балансы после: отправитель={}, получатель={}",
                    transaction.getId(), senderBalanceAfter, receiverBalanceAfter);
    
            return transferMapper.toResponseDto(transaction);
        }

    private boolean isTransferAllowed(Account sender, Account receiver, BigDecimal amount) {
        return sender.getStatus() == AccountStatus.ACTIVE &&
                receiver.getStatus() == AccountStatus.ACTIVE &&
                sender.getBalance().compareTo(amount) >= 0;
    }
}
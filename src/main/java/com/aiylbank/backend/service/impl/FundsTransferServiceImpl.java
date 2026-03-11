package com.aiylbank.backend.service.impl;

import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.entity.Account;
import com.aiylbank.backend.entity.AccountStatus;
import com.aiylbank.backend.entity.Transaction;
import com.aiylbank.backend.entity.TransactionStatus;
import com.aiylbank.backend.exception.AccountNotFoundException;
import com.aiylbank.backend.exception.AccountStatusException;
import com.aiylbank.backend.exception.NotEnoughMoneyException;
import com.aiylbank.backend.exception.BusinessException;
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
    @Transactional(
            propagation = Propagation.REQUIRED,
            noRollbackFor = {AccountStatusException.class, NotEnoughMoneyException.class}
    )
    public TransferResponseDto transferFunds(String senderAccountNumber,
                                             String receiverAccountNumber,
                                             BigDecimal amount) {

        log.info("Начало перевода {} с {} на {}", amount, senderAccountNumber, receiverAccountNumber);

        Account sender = accountRepository.findByAccountNo(senderAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Счёт отправителя не найден"));

        Account receiver = accountRepository.findByAccountNo(receiverAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Счёт получателя не найден"));

        if (!isTransferAllowed(sender, receiver, amount)) {

            Transaction failedTransaction = transferMapper.toEntity(
                    sender, receiver, TransactionStatus.FAILED, amount,
                    sender.getBalance(), receiver.getBalance()
            );
            transactionRepository.save(failedTransaction);

            if (sender.getStatus() != AccountStatus.ACTIVE) {
                log.warn("Перевод отклонен: счет отправителя {} заблокирован", sender.getAccountNo());
                throw new AccountStatusException("Счет отправителя заблокирован");
            }
            if (receiver.getStatus() != AccountStatus.ACTIVE) {
                log.warn("Перевод отклонен: счет получателя {} заблокирован", receiver.getAccountNo());
                throw new AccountStatusException("Счет получателя заблокирован");
            }
            if (sender.getBalance().compareTo(amount) < 0) {
                log.warn("Перевод отклонен: недостаточно средств на счету {}. Баланс: {}, запрашиваемая сумма: {}",
                        sender.getAccountNo(), sender.getBalance(), amount);
                throw new NotEnoughMoneyException("Недостаточно денег для перевода");
            }

            throw new BusinessException("Перевод отклонен по правилам банка");
        }

        BigDecimal senderBalanceAfter = sender.getBalance().subtract(amount);
        BigDecimal receiverBalanceAfter = receiver.getBalance().add(amount);

        sender.setBalance(senderBalanceAfter);
        receiver.setBalance(receiverBalanceAfter);

        Transaction successTransaction = transferMapper.toEntity(
                sender, receiver, TransactionStatus.SUCCESS, amount,
                senderBalanceAfter, receiverBalanceAfter
        );

        transactionRepository.save(successTransaction);
        log.info("Транзакция завершена успешно. ID: {}", successTransaction.getId());
        return transferMapper.toResponseDto(successTransaction);
    }

    private boolean isTransferAllowed(Account sender, Account receiver, BigDecimal amount) {
        return sender.getStatus() == AccountStatus.ACTIVE &&
                receiver.getStatus() == AccountStatus.ACTIVE &&
                sender.getBalance().compareTo(amount) >= 0;
    }
}
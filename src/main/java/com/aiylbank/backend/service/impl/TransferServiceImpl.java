package com.aiylbank.backend.service.impl;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.CreateTransferDto;
import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.entity.Transaction;
import com.aiylbank.backend.exception.*;
import com.aiylbank.backend.mapper.TransferMapper;
import com.aiylbank.backend.repository.AccountRepository;
import com.aiylbank.backend.repository.TransactionRepository;
import com.aiylbank.backend.service.TransferService;
import com.aiylbank.backend.service.FundsTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Service
public class TransferServiceImpl implements TransferService {

    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Bishkek");

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FundsTransferService fundsTransferService;
    private final TransferMapper transferMapper;

    @Autowired
    public TransferServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            FundsTransferService fundsTransferService,
            TransferMapper transferMapper
    ){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.fundsTransferService = fundsTransferService;
        this.transferMapper = transferMapper;
    }

    @Override
    @Transactional
    public TransferResponseDto execute(CreateTransferDto createTransferDto) {
        String senderAccountNumber = createTransferDto.senderAccountNumber();
        String receiverAccountNumber = createTransferDto.receiverAccountNumber();

        if(senderAccountNumber.equals(receiverAccountNumber)){
            throw new SelfTransferException("Невозможно выполнить перевод на собственный счет");
        }

        if (!accountRepository.existsByAccountNo(senderAccountNumber)) {
            throw new AccountNotFoundException("Счёт отправителя не найден");
        }
        if (!accountRepository.existsByAccountNo(receiverAccountNumber)){
            throw new AccountNotFoundException("ОСчёт получателя не найден");

        }

        return fundsTransferService.transferFunds(senderAccountNumber, receiverAccountNumber, createTransferDto.amount());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountStatementDto getAccountStatement(String accountNumber, LocalDate from, LocalDate to, Integer page, Integer size){

        validateAccountAndDates(accountNumber, from, to);

        OffsetDateTime fromDateTime = from.atStartOfDay(DEFAULT_ZONE).toOffsetDateTime();
        OffsetDateTime toDateTime = to.atTime(LocalTime.MAX).atZone(DEFAULT_ZONE).toOffsetDateTime();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Transaction> transactionPage = transactionRepository.findStatement(accountNumber, fromDateTime, toDateTime, pageable);
        return transferMapper.toAccountStatementDto(transactionPage, accountNumber);
    }


    private void validateAccountAndDates(String accountNumber, LocalDate from, LocalDate to){
        if(!accountRepository.existsByAccountNo(accountNumber)){
            throw new AccountNotFoundException("Счет не найден");
        }
        if (from.isAfter(to)) {
            throw new InvalidDateException("Дата начала не может быть позже даты окончания");
        }
        if (from.isAfter(LocalDate.now()) || to.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Даты не могут быть в будущем");
        }
        if (from.isBefore(MIN_DATE)) {
            throw new InvalidDateException("Дата начала слишком ранняя");
        }
    }
}
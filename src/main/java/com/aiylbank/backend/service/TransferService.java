package com.aiylbank.backend.service;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.CreateTransferDto;
import com.aiylbank.backend.dto.TransferResponseDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface TransferService {
    TransferResponseDto execute(CreateTransferDto createTransferDto);
    AccountStatementDto getAccountStatement(String accountNo, LocalDate from, LocalDate to, Integer page, Integer size);

}

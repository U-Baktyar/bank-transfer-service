package com.aiylbank.backend.service;

import com.aiylbank.backend.dto.TransferResponseDto;


import java.math.BigDecimal;


public interface FundsTransferService {
    TransferResponseDto transferFunds(String senderAccountNumber,
                                         String receiverAccountNumber,
                                         BigDecimal amount);
}

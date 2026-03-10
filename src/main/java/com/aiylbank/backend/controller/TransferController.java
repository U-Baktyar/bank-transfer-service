package com.aiylbank.backend.controller;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.CreateTransferDto;
import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.service.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@Validated
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponseDto> execute(@RequestBody @Valid CreateTransferDto createTransferDto){
        log.info("Запрос на перевод с счета {} на счет {} сумма {}",
                createTransferDto.senderAccountNumber(),
                createTransferDto.receiverAccountNumber(),
                createTransferDto.amount()
        );
        return ResponseEntity.ok(transferService.execute(createTransferDto));
    }

    @GetMapping("/accounts/{accountNumber}/statement")
    public ResponseEntity<AccountStatementDto> getAccountStatement(
            @PathVariable("accountNumber") @Size(min = 20, max = 20) String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size
    ){
        log.info("Запрос на получение выписки по счету {} с {} по {}, страница {}, размер {}",
                accountNumber, from, to, page, size);
        return ResponseEntity.ok(
                transferService.getAccountStatement(accountNumber, from, to, page, size)
        );
    }

}
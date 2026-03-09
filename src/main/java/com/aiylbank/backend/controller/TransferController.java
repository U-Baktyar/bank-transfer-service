package com.aiylbank.backend.controller;

import com.aiylbank.backend.dto.AccountStatementDto;
import com.aiylbank.backend.dto.CreateTransferDto;
import com.aiylbank.backend.dto.TransferResponseDto;
import com.aiylbank.backend.service.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Validated
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponseDto> execute(@RequestBody @Valid CreateTransferDto createTransferDto){
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
        return ResponseEntity.ok(
                transferService.getAccountStatement(accountNumber, from, to, page, size)
        );
    }

}

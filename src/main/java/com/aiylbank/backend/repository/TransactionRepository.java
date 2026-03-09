package com.aiylbank.backend.repository;

import com.aiylbank.backend.entity.Transaction;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :from  AND :to AND (t.sender.accountNo = :accountNumber OR t.receiver.accountNo = :accountNumber)")
    Page<Transaction> findStatement(@Param("accountNumber") String accountNumber , @Param("from") OffsetDateTime from,@Param("to") OffsetDateTime to, Pageable pageable);
}

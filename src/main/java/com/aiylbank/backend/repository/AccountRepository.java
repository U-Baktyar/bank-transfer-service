package com.aiylbank.backend.repository;

import com.aiylbank.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(String accountNumber);
    Boolean existsByAccountNo(String accountNumber);
}
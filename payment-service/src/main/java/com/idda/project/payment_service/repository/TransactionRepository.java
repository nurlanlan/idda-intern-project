package com.idda.project.payment_service.repository;

import com.idda.project.payment_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByTransactionTimestampDesc(Long userId);

}

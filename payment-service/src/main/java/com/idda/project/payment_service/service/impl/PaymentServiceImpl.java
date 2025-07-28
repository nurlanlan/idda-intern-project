package com.idda.project.payment_service.service.impl;

import com.idda.project.payment_service.dto.response.TransactionResponse;
import com.idda.project.payment_service.entity.Transaction;
import com.idda.project.payment_service.repository.TransactionRepository;
import com.idda.project.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionResponse> getTransactionHistory(Long userId) {
    List<Transaction> transactions = transactionRepository.findByUserIdOrderByTransactionTimestampDesc(userId);

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponse convertToDTO(Transaction transaction) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(transaction.getId());
        dto.setProductId(transaction.getProductId());
        dto.setProductName(transaction.getProductName());
        dto.setMaskedCardNumber(transaction.getMaskedCardNumber());
        dto.setQuantity(transaction.getQuantity());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionTimestamp(transaction.getTransactionTimestamp());
        return dto;
    }
}

package com.idda.project.payment_service.service.impl;

import com.idda.project.payment_service.dto.client.CardDTO;
import com.idda.project.payment_service.dto.client.DebitRequest;
import com.idda.project.payment_service.dto.client.DecreaseStockRequest;
import com.idda.project.payment_service.dto.client.ProductDTO;
import com.idda.project.payment_service.dto.request.PurchaseRequestDTO;
import com.idda.project.payment_service.dto.response.PurchaseResponseDTO;
import com.idda.project.payment_service.dto.response.TransactionResponse;
import com.idda.project.payment_service.entity.Transaction;
import com.idda.project.payment_service.repository.TransactionRepository;
import com.idda.project.payment_service.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTransactionTimestampDesc(userId);

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseResponseDTO processPurchase(PurchaseRequestDTO request) {
        ProductDTO product = webClientBuilder.build().get()
                .uri("http://localhost:8084/api/products/{id}", request.getProductId())
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
        }

        CardDTO card = webClientBuilder.build().get()
                .uri("http://localhost:8083/api/cards/{id}?userId={userId}", request.getCardId(), request.getUserId())
                .retrieve()
                .bodyToMono(CardDTO.class)
                .block();
        if (card == null || !card.isActive()) {
            throw new RuntimeException("Card not found or inactive");
        }

        float totalAmount = product.getPrice() * request.getQuantity();
        if (card.getBalance() < totalAmount) {
            throw new RuntimeException("Insufficient funds on the card!");
        }

        webClientBuilder.build().post()
                .uri("http://localhost:8083/api/cards/debit")
                .bodyValue(new DebitRequest(request.getCardId(), totalAmount))
                .retrieve()
                .toBodilessEntity()
                .block();

        webClientBuilder.build().post()
                .uri("http://localhost:8084/api/products/decrease-stock")
                .bodyValue(new DecreaseStockRequest(request.getProductId(), request.getQuantity()))
                .retrieve()
                .toBodilessEntity()
                .block();

        Transaction transaction = new Transaction();
        transaction.setUserId(request.getUserId());
        transaction.setProductId(request.getProductId());
        transaction.setProductName(product.getProductName());
        transaction.setMaskedCardNumber(card.getMaskedCardNumber());
        transaction.setQuantity(request.getQuantity());
        transaction.setAmount(totalAmount);
        transaction.setTransactionTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new PurchaseResponseDTO(
                savedTransaction.getId(),
                "SUCCESS",
                "Payment successful!",
                savedTransaction.getTransactionTimestamp()
        );
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

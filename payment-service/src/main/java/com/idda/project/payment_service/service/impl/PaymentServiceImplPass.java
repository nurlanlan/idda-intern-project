//package com.idda.project.payment_service.service.impl;
//
//import com.idda.project.payment_service.dto.client.CardDTO;
//import com.idda.project.payment_service.dto.client.DebitRequest;
//import com.idda.project.payment_service.dto.client.DecreaseStockRequest;
//import com.idda.project.payment_service.dto.client.ProductDTO;
//import com.idda.project.payment_service.dto.request.PurchaseRequestDTO;
//import com.idda.project.payment_service.dto.response.PurchaseResponseDTO;
//import com.idda.project.payment_service.dto.response.TransactionResponse;
//import com.idda.project.payment_service.entity.Transaction;
//import com.idda.project.payment_service.repository.TransactionRepository;
//import com.idda.project.payment_service.service.PaymentService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PaymentServiceImpl implements PaymentService {
//
//    private final TransactionRepository transactionRepository;
//    private final WebClient.Builder webClientBuilder;
//
//    @Override
//    public List<TransactionResponse> getTransactionHistory(Long userId) {
//        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTransactionTimestampDesc(userId);
//
//        return transactions.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public PurchaseResponseDTO processPurchase(PurchaseRequestDTO request) {
//        ProductDTO product;
//        try {
//            product = webClientBuilder.build().get()
//                    .uri("http://localhost:8084/api/products/{id}", request.getProductId())
//                    .retrieve()
//                    .bodyToMono(ProductDTO.class)
//                    .block();
//        } catch (WebClientResponseException ex) {
//            log.error("Error fetching product data. Status: {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
//            throw new ResponseStatusException(ex.getStatusCode(), "Product not found with entered id ", ex);
//        }
//
//        if (product.getStock() < request.getQuantity()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for product: " + product.getProductName());
//        }
//
//        CardDTO card;
//        try {
//            card = webClientBuilder.build().get()
//                    .uri("http://localhost:8083/api/cards/{id}?userId={userId}", request.getCardId(), request.getUserId())
//                    .retrieve()
//                    .bodyToMono(CardDTO.class)
//                    .block();
//        } catch (WebClientResponseException ex) {
//            log.error("Error fetching card data. Status: {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
//            throw new ResponseStatusException(ex.getStatusCode(), "Card not found or error in Card Service.", ex);
//        }
//
//        if (!card.isActive()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is not active.");
//        }
//
//        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
//        if (card.getBalance().compareTo(totalAmount) < 0) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds on the card!");
//        }
//
//
//        webClientBuilder.build().post()
//                .uri("http://localhost:8083/api/cards/debit")
//                .bodyValue(new DebitRequest(request.getCardId(), totalAmount))
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//
//        webClientBuilder.build().post()
//                .uri("http://localhost:8084/api/products/decrease-stock")
//                .bodyValue(new DecreaseStockRequest(request.getProductId(), request.getQuantity()))
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//
//        Transaction transaction = new Transaction();
//        transaction.setUserId(request.getUserId());
//        transaction.setProductId(request.getProductId());
//        transaction.setProductName(product.getProductName());
//        transaction.setMaskedCardNumber(card.getMaskedCardNumber());
//        transaction.setQuantity(request.getQuantity());
//        transaction.setAmount(totalAmount);
//        transaction.setTransactionTimestamp(LocalDateTime.now());
//
//        Transaction savedTransaction = transactionRepository.save(transaction);
//
//        return new PurchaseResponseDTO(
//                savedTransaction.getId(),
//                "SUCCESS",
//                "Payment successful!",
//                savedTransaction.getTransactionTimestamp()
//        );
//    }
//
//    private TransactionResponse convertToDTO(Transaction transaction) {
//        TransactionResponse dto = new TransactionResponse();
//        dto.setId(transaction.getId());
//        dto.setProductId(transaction.getProductId());
//        dto.setProductName(transaction.getProductName());
//        dto.setMaskedCardNumber(transaction.getMaskedCardNumber());
//        dto.setQuantity(transaction.getQuantity());
//        dto.setAmount(transaction.getAmount());
//        dto.setTransactionTimestamp(transaction.getTransactionTimestamp());
//        return dto;
//    }
//}

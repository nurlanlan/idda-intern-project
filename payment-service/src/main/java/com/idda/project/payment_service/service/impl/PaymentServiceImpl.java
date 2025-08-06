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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTransactionTimestampDesc(userId);

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Bu metod bir çox addımdan ibarət olduğu üçün transactional olmalıdır
    public PurchaseResponseDTO processPurchase(PurchaseRequestDTO request) {
        // TRY-CATCH BLOKLARI ARTIQ YOXDUR!
        // Əgər aşağıdakı WebClient çağırışlarından hər hansı biri 4xx və ya 5xx xətası alsa,
        // WebClientResponseException atılacaq və bu, birbaşa bizim GlobalExceptionHandler tərəfindən tutulacaq.

        // --- ADDIM 1: MƏLUMATLARIN TOPLANMASI VƏ YOXLANIŞI ---

        // 1.1. Product Service-dən məhsul məlumatlarını alırıq.
        ProductDTO product = webClientBuilder.build().get()
                .uri("http://localhost:8084/api/products/{id}", request.getProductId())
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();

        // 1.2. Stok yoxlanışı (bu, biznes məntiqidir və burada qalmalıdır).
        if (product.getStock() < request.getQuantity()) {
            // Stok yoxdursa, dərhal 409 Conflict statusu ilə xəta atırıq.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for product: " + product.getProductName());
        }

        // 1.3. Card Service-dən kart məlumatlarını alırıq.
        CardDTO card = webClientBuilder.build().get()
                .uri("http://localhost:8083/api/cards/{id}", uriBuilder -> uriBuilder
                        .queryParam("userId", request.getUserId()).build(request.getCardId()))
                .retrieve()
                .bodyToMono(CardDTO.class)
                .block();

        if (!card.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is not active.");
        }

        // 1.4. Balans yoxlanışı.
        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        if (card.getBalance().compareTo(totalAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds on the card!");
        }

        // --- ADDIM 2: ƏMRLƏRİN İCRASI (SAGA-nın sadə versiyası) ---
        // Bu addımda xəta baş verərsə, GlobalExceptionHandler onu tutacaq.
        // Daha mürəkkəb sistemlərdə burada kompensasiya edici tranzaksiyalar olmalıdır.

        // Balansı azaltmaq üçün Card Service-i çağırırıq.
        webClientBuilder.build().post()
                .uri("http://localhost:8083/api/cards/debit")
                .bodyValue(new DebitRequest(request.getCardId(), totalAmount))
                .retrieve()
                .toBodilessEntity()
                .block();

        // Stoku azaltmaq üçün Product Service-i çağırırıq.
        webClientBuilder.build().post()
                .uri("http://localhost:8084/api/products/decrease-stock")
                .bodyValue(new DecreaseStockRequest(request.getProductId(), request.getQuantity()))
                .retrieve()
                .toBodilessEntity()
                .block();

        // --- ADDIM 3: TRANZAKSİYANIN QEYDİ ---
        Transaction transaction = new Transaction();
        transaction.setUserId(request.getUserId());
        transaction.setProductId(request.getProductId());
        transaction.setProductName(product.getProductName());
        transaction.setMaskedCardNumber(card.getMaskedCardNumber());
        transaction.setQuantity(request.getQuantity());
        transaction.setAmount(totalAmount);
        transaction.setTransactionTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        // --- ADDIM 4: UĞURLU CAVABIN QAYTARILMASI ---
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
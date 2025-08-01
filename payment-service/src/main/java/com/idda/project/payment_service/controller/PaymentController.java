package com.idda.project.payment_service.controller;


import com.idda.project.payment_service.dto.request.PurchaseRequestDTO;
import com.idda.project.payment_service.dto.response.PurchaseResponseDTO;
import com.idda.project.payment_service.dto.response.TransactionResponse;
import com.idda.project.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistoryForUser(@RequestParam Long userId) {
        List<TransactionResponse> history = paymentService.getTransactionHistory(userId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponseDTO> purchaseProduct(@Valid @RequestBody PurchaseRequestDTO request) {
        PurchaseResponseDTO response = paymentService.processPurchase(request);
        return ResponseEntity.ok(response);
    }

}

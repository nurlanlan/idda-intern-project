package com.idda.project.payment_service.controller;


import com.idda.project.payment_service.dto.response.TransactionResponse;
import com.idda.project.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.idda.project.payment_service.service;

import com.idda.project.payment_service.dto.request.PurchaseRequestDTO;
import com.idda.project.payment_service.dto.response.PurchaseResponseDTO;
import com.idda.project.payment_service.dto.response.TransactionResponse;
import java.util.List;

public interface PaymentService {
    List<TransactionResponse> getTransactionHistory(Long userId);
    PurchaseResponseDTO processPurchase(PurchaseRequestDTO request);
}

package com.idda.project.payment_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String productName;
    private int stock;
    private BigDecimal price;
}

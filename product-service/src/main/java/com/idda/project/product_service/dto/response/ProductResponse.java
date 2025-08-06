package com.idda.project.product_service.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private long id;
    private String productName;
    private int stock;
    private int price;
}

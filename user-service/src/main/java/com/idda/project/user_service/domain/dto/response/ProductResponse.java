package com.idda.project.user_service.domain.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private long id;
    private String productName;
    private int stock;
    private float price;
}

package com.hexagonal.ecommerce.model;

import lombok.Data;

@Data
public class OrderDetail {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}

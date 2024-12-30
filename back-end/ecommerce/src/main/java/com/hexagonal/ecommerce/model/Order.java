package com.hexagonal.ecommerce.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private Long customerId;

    private LocalDate date;

    private Double total;

    private List<OrderDetail> details;

}

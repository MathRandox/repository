package com.hexagonal.ecommerce.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private String id;
    private Long customerId;
    private List<OrderDetailDTO> details;
    private Double total;
    private LocalDate date;
}

package com.hexagonal.ecommerce.repository;

import com.hexagonal.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}

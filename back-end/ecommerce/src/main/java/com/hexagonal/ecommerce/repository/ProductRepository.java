package com.hexagonal.ecommerce.repository;

import com.hexagonal.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

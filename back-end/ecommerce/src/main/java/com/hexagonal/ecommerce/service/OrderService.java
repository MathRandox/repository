package com.hexagonal.ecommerce.service;

import com.hexagonal.ecommerce.dto.OrderRequestDTO;
import com.hexagonal.ecommerce.dto.OrderResponseDTO;
import com.hexagonal.ecommerce.dto.OrderDetailDTO;
import com.hexagonal.ecommerce.model.Order;
import com.hexagonal.ecommerce.model.OrderDetail;
import com.hexagonal.ecommerce.repository.OrderRepository;
import com.hexagonal.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        List<OrderDetail> orderDetails = orderRequestDTO.getDetails().stream().map(detailDTO -> {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(detailDTO.getProductId());
            detail.setQuantity(detailDTO.getQuantity());

            detail.setUnitPrice(productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"))
                    .getPrice());

            return detail;
        }).collect(Collectors.toList());

        Double total = orderDetails.stream().mapToDouble(detail -> detail.getUnitPrice() * detail.getQuantity()).sum();

        order.setCustomerId(orderRequestDTO.getCustomerId());
        order.setDetails(orderDetails);
        order.setTotal(total);
        order.setDate(orderRequestDTO.getDate());

        Order savedOrder = orderRepository.save(order);

        return convertToOrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrder(String id, OrderRequestDTO orderRequestDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetail> updatedDetails = orderRequestDTO.getDetails().stream().map(detailDTO -> {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(detailDTO.getProductId());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"))
                    .getPrice());
            return detail;
        }).collect(Collectors.toList());

        Double total = updatedDetails.stream().mapToDouble(detail -> detail.getUnitPrice() * detail.getQuantity()).sum();

        existingOrder.setDetails(updatedDetails);
        existingOrder.setTotal(total);
        existingOrder.setCustomerId(orderRequestDTO.getCustomerId());
        existingOrder.setDate(orderRequestDTO.getDate());

        Order updatedOrder = orderRepository.save(existingOrder);

        return convertToOrderResponseDTO(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToOrderResponseDTO(order);
    }

    @Transactional
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setCustomerId(order.getCustomerId());
        orderResponseDTO.setDate(order.getDate());
        orderResponseDTO.setTotal(order.getTotal());

        List<OrderDetailDTO> details = order.getDetails().stream()
                .map(detail -> {
                    OrderDetailDTO detailDTO = new OrderDetailDTO();
                    detailDTO.setProductId(detail.getProductId());
                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setUnitPrice(detail.getUnitPrice());
                    return detailDTO;
                })
                .collect(Collectors.toList());


        orderResponseDTO.setDetails(details);
        return orderResponseDTO;
    }
}

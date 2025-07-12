package telran.java57.farmmarket.service;

import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderDto orderDto, String userLogin);
    List<OrderResponseDto> getOrdersBySupplierLogin(String supplierLogin);
    OrderResponseDto markOrderAsPaid(String orderId, String userLogin);
    List<OrderResponseDto> getAllOrders();
}

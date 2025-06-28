package telran.java57.farmmarket.service;

import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;
import telran.java57.farmmarket.model.Order;

public interface OrderService {
    OrderResponseDto createOrder(OrderDto orderDto, String userLogin);
}

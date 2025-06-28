package telran.java57.farmmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;
import telran.java57.farmmarket.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderDto orderDto,Authentication authentication) {
        String userLogin = authentication.getName();
        OrderResponseDto response = orderService.createOrder(orderDto, userLogin);
        return ResponseEntity.ok(response);
    }
}

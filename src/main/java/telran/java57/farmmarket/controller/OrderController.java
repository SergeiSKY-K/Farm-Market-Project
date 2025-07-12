package telran.java57.farmmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;
import telran.java57.farmmarket.service.OrderService;

import java.util.List;

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
    @GetMapping("/supplier")
    public List<OrderResponseDto> getOrdersForSupplier() {
        String supplierLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.getOrdersBySupplierLogin(supplierLogin);
    }
    @PostMapping("/{id}/pay")
    public OrderResponseDto payForOrder(@PathVariable String id, Authentication authentication) {
        String userLogin = authentication.getName();
        return orderService.markOrderAsPaid(id, userLogin);
    }
    @GetMapping("/moderator")
    public List<OrderResponseDto> getAllOrdersForModerator() {
        return orderService.getAllOrders(); // нужен метод ниже
    }
}

package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.OrderRepository;
import telran.java57.farmmarket.dao.ProductRepository;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;
import telran.java57.farmmarket.dto.exceptions.NotEnoughQuantityOfProductException;
import telran.java57.farmmarket.dto.exceptions.OrderNotFoundException;
import telran.java57.farmmarket.dto.exceptions.ProductNotFoundException;
import telran.java57.farmmarket.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final ModelMapper modelMapper;

    @Override
    public OrderResponseDto createOrder(OrderDto orderDto, String userLogin) {
        Map<String, Integer> productQuantities = orderDto.getProductQuantities();
        if (productQuantities == null || productQuantities.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Set<String> requestedIds = productQuantities.keySet();
        List<Product> products = productRepository.findAllById(requestedIds);


        Set<String> foundIds = products.stream().map(Product::getId).collect(Collectors.toSet());
        for (String id : requestedIds) {
            if (!foundIds.contains(id)) throw new ProductNotFoundException(id);
        }

        List<OrderItem> items = new ArrayList<>();
        double total = 0d;

        for (Product p : products) {
            int reqQty = productQuantities.getOrDefault(p.getId(), 0);
            if (reqQty <= 0) {
                throw new IllegalArgumentException("Invalid quantity for product " + p.getId());
            }
            if (p.getStatus() == ProductStatus.BLOCKED) {
                throw new IllegalStateException("Product is blocked: " + p.getName());
            }
            if (p.getQuantity() < reqQty) {
                throw new NotEnoughQuantityOfProductException(p.getName());
            }


            p.setQuantity(p.getQuantity() - reqQty);

            items.add(OrderItem.builder()
                    .productId(p.getId())
                    .name(p.getName())
                    .supplierLogin(p.getSupplierLogin())
                    .quantity(reqQty)
                    .priceAtPurchase(p.getPrice())
                    .build());

            total += p.getPrice() * reqQty;
        }

        productRepository.saveAll(products);

        Order order = Order.builder()
                .userLogin(userLogin)
                .items(items)
                .totalPrice(total)
                .status(OrderStatus.CREATED)
                .createdAt(Instant.now())
                .build();

        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto markOrderAsPaid(String orderId, String userLogin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUserLogin().equals(userLogin)) {
            throw new AccessDeniedException("You can only pay your own order");
        }
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Order is not payable");
        }

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(Instant.now());
        orderRepository.save(order);

        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public List<OrderResponseDto> getMyOrders(String userLogin) {
        return orderRepository.findAllByUserLoginOrderByCreatedAtDesc(userLogin)
                .stream()
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersBySupplierLogin(String supplierLogin) {
        return orderRepository.findByItemsSupplierLogin(supplierLogin, Pageable.unpaged())
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .getContent();
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(o -> modelMapper.map(o, OrderResponseDto.class));
    }
}



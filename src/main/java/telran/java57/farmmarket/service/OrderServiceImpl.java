package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.OrderRepository;
import telran.java57.farmmarket.dao.ProductRepository;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.dto.OrderResponseDto;
import telran.java57.farmmarket.dto.exceptions.NotEnoughQuantityOfProductException;
import telran.java57.farmmarket.dto.exceptions.ProductNotFoundException;
import telran.java57.farmmarket.model.Order;
import telran.java57.farmmarket.model.OrderStatus;
import telran.java57.farmmarket.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final ModelMapper modelMapper;

    @Override
    public OrderResponseDto createOrder(OrderDto orderDto, String userLogin) {
        Map<String, Integer> productQuantities = orderDto.getProductQuantities();

        Set<String> requestedIds = productQuantities.keySet();

        List<Product> products = productRepository.findAllById(requestedIds);

        for (String id : requestedIds) {
            boolean exists = products.stream().anyMatch(p -> p.getId().equals(id));
            if (!exists) {
                throw new ProductNotFoundException(id);
            }
        }

        double totalPrice = 0;

        for (Product product : products) {
            int requestedQuantity = productQuantities.get(product.getId());

            if (product.getQuantity() < requestedQuantity) {
                throw new NotEnoughQuantityOfProductException(product.getName());
            }

            product.setQuantity(product.getQuantity() - requestedQuantity);
            totalPrice += product.getPrice() * requestedQuantity;
        }

        productRepository.saveAll(products);

        Order order = new Order();
        order.setUserLogin(userLogin);
        order.setProductsId(new ArrayList<>(productQuantities.keySet()));
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }
}



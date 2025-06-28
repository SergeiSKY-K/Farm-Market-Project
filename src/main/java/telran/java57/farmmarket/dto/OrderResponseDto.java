package telran.java57.farmmarket.dto;


import lombok.*;
import telran.java57.farmmarket.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private String id;
    private List<String> productsId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private Double totalPrice;
}
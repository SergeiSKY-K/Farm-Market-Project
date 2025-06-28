package telran.java57.farmmarket.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private String id;

    private String userLogin;

    private List<String> productsId;

    private LocalDateTime createdAt;

    private OrderStatus status;

    private Double totalPrice;
}

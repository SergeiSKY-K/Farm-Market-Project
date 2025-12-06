package telran.java57.farmmarket.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
    private String productId;
    private String name;
    private int quantity;
    private double price;
    private String supplierLogin;
    private int version = 1;
}

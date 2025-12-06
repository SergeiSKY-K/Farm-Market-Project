package telran.java57.farmmarket.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeletedEvent {
    private String productId;
    private int version = 1;
}

package telran.java57.farmmarket.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "products")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {
    @Id
    private String id;
    @Setter
    private String name;
    @Setter
    private int price;
    @Setter
    private int quantity;
    @Setter
    private String category;
}

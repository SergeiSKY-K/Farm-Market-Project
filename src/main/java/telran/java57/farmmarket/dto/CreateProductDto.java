package telran.java57.farmmarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {
    private String name;
    private int price;
    private int quantity;
    private String category;
}

package telran.java57.farmmarket.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProductDto {
    private String id;
    private String name;
    private int price;
    private int quantity;
    private String category;
}

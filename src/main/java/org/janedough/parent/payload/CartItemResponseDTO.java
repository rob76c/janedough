package org.janedough.parent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private Integer stock;
    private Double price;
    private Double discount;
    private Double specialPrice;
    private Integer quantity;
}

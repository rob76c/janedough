package org.janedough.parent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private String phoneNumber;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime orderDateTime;
    private PaymentDTO payment;
    private Double totalPrice;
    private String orderStatus;
    private Long addressId;
}

package org.janedough.parent.service;

import jakarta.transaction.Transactional;
import org.janedough.parent.payload.OrderDTO;
import org.janedough.parent.payload.OrderResponse;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String email, String phoneNumber, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage, Long shippingAddressId, Long billingAddressId);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderResponse getAllUserOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}

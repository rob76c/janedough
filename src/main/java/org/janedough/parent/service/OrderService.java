package org.janedough.parent.service;

import jakarta.transaction.Transactional;
import org.janedough.parent.payload.OrderDTO;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String email, String phoneNumber, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage, Long addressId);
}

package org.janedough.parent.controller;

import org.janedough.parent.payload.OrderDTO;
import org.janedough.parent.payload.OrderRequestDTO;
import org.janedough.parent.service.OrderService;
import org.janedough.parent.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String email = authUtil.loggedInEmail();
        String phoneNumber = authUtil.loggedInPhoneNumber();
        OrderDTO order = orderService.placeOrder(
                email,
                phoneNumber,
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage(),
                orderRequestDTO.getAddressId()

        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}

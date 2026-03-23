package org.janedough.parent.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.janedough.parent.config.AppConstants;
import org.janedough.parent.payload.OrderDTO;
import org.janedough.parent.payload.OrderRequestDTO;
import org.janedough.parent.payload.OrderResponse;
import org.janedough.parent.payload.StripePaymentDto;
import org.janedough.parent.service.OrderService;
import org.janedough.parent.service.StripeService;
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
    private StripeService stripeService;

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

    @PostMapping("/order/stripe-client-secret")
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentDto stripePaymentDTO) throws StripeException {
        System.out.println("StripePaymentDto: " + stripePaymentDTO);
        PaymentIntent paymentIntent = stripeService.paymentIntent(stripePaymentDTO);
        return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }
    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

}

package org.janedough.parent.service;

import jakarta.transaction.Transactional;
import org.janedough.parent.config.AppConstants;
import org.janedough.parent.exceptions.APIException;
import org.janedough.parent.exceptions.ResourceNotFoundException;
import org.janedough.parent.model.*;
import org.janedough.parent.payload.OrderDTO;
import org.janedough.parent.payload.OrderItemDTO;
import org.janedough.parent.payload.OrderResponse;
import org.janedough.parent.repositories.*;
import org.janedough.parent.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
     AuthUtil authUtil;

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, String phoneNumber, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage, Long shippingAddressId, Long billingAddressId) {

        Cart cart = cartRepository.findCartByEmail(email);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", email);
        }
        Address shippingAddress = addressRepository.findById(shippingAddressId).orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", shippingAddressId));
        Address billingAddress = addressRepository.findById(billingAddressId).orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", billingAddressId));
        Order order = new Order();
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setOrderDateTime(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice() + (AppConstants.TAX *cart.getTotalPrice()) );
        order.setOrderStatus("Order Accepted!");
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        Payment payment = new Payment(pgPaymentId, paymentMethod, pgStatus, pgName, pgResponseMessage);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setShippingAddressId(shippingAddressId);
        orderDTO.setBillingAddressId(billingAddressId);

        return orderDTO;
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Order> pageOrders = orderRepository.findAll(pageDetails);
        List<Order> orders = pageOrders.getContent();
        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setLastPage(pageOrders.isLast());


        return orderResponse;
    }

    @Override
    public OrderResponse getAllUserOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        String user = authUtil.loggedInEmail();
        Page<Order> pageOrders = orderRepository.findAll(pageDetails);

        List<Order> userOrders = pageOrders.getContent().stream()
                .filter(order -> order.getEmail().equals(user)
                ).toList();
        List<OrderDTO> orderDTOs = userOrders.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setLastPage(pageOrders.isLast());


        return orderResponse;
    }
}

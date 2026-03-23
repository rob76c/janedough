package org.janedough.parent.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.janedough.parent.payload.StripePaymentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentIntent paymentIntent(StripePaymentDto stripePaymentDTO) throws StripeException {
        Customer customer;
        CustomerSearchParams searchParams =
                CustomerSearchParams.builder()
                        .setQuery("email:'" + stripePaymentDTO.getEmail() + "'") //add OR phoneNumber:''
                        .build();
        CustomerSearchResult customers = Customer.search(searchParams);
        if(customers.getData().isEmpty()){
            CustomerCreateParams parameters =
                    CustomerCreateParams.builder()
                            .setName(stripePaymentDTO.getName())
                            .setEmail(stripePaymentDTO.getEmail())
                            .setPhone(stripePaymentDTO.getPhoneNumber())
                            .setAddress(CustomerCreateParams.Address.builder()
                                    .setLine1(stripePaymentDTO.getAddress().getStreet())
                                    .setCity(stripePaymentDTO.getAddress().getCity())
                                    .setState(stripePaymentDTO.getAddress().getState())
                                    .setPostalCode(stripePaymentDTO.getAddress().getZip())
                                    .setCountry(stripePaymentDTO.getAddress().getCountry())
                                    .build())
                            .build();
            customer = Customer.create(parameters);

        }else{
            customer = customers.getData().get(0);
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(stripePaymentDTO.getAmount())
                        .setCurrency(stripePaymentDTO.getCurrency())
                        .setCustomer(customer.getId())
                        .setDescription(stripePaymentDTO.getDescription())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        return PaymentIntent.create(params);
    }
}

package org.janedough.parent.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.janedough.parent.payload.StripePaymentDto;

public interface StripeService {
    PaymentIntent paymentIntent(StripePaymentDto stripePaymentDTO) throws StripeException;
}

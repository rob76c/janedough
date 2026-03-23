package org.janedough.parent.payload;

import lombok.Data;
import org.janedough.parent.model.Address;

import java.util.Map;

@Data
public class StripePaymentDto {
    private Long amount;
    private String currency;
    private String email;
    private String phoneNumber;
    private String name;
    private Address address;
    private String description;
    private Map<String, String> metadata;
}

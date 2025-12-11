package org.janedough.parent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message= "Street name must be at least 2 characters")
    private String street;

    private String addressLine2;

    @NotBlank
    @Size(min = 2, message= "City name must be at least 2 characters")
    private String city;
    @NotBlank
    @Size(min = 2, message= "State name must be at least 2 characters")
    private String state;
    @NotBlank
    @Size(min = 5, message= "Zipcode name must be at least 5 characters")
    private String zip;
    @NotBlank
    @Size(min = 2, message= "Country name must be at least 2 characters")
    private String country;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String addressLine2, String city, String state, String zip, String country) {
        this.street = street;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }
}

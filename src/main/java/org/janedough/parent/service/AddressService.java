package org.janedough.parent.service;

import jakarta.validation.Valid;
import org.janedough.parent.model.User;
import org.janedough.parent.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(@Valid AddressDTO addressDTO, Long addressId);

    String deleteAddress(Long addressId);
}

package org.janedough.parent.service;

import org.janedough.parent.model.User;
import org.janedough.parent.payload.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);
}

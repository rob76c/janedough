package org.janedough.parent.controller;

import jakarta.validation.Valid;
import org.janedough.parent.model.User;
import org.janedough.parent.payload.AddressDTO;
import org.janedough.parent.service.AddressService;
import org.janedough.parent.service.CategoryService;
import org.janedough.parent.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);

        return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
    }
}

package org.janedough.parent.service;

import org.janedough.parent.exceptions.APIException;
import org.janedough.parent.exceptions.ResourceNotFoundException;
import org.janedough.parent.model.Address;
import org.janedough.parent.model.User;
import org.janedough.parent.payload.AddressDTO;
import org.janedough.parent.repositories.AddressRepository;
import org.janedough.parent.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO,  User user) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new APIException("There are no addresses yet!!");
        }
        List<AddressDTO> addressDTOS = addresses.stream().map(address ->
                modelMapper.map(address, AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        if (addresses.isEmpty()) {
            throw new APIException("There are no addresses for this user!!");
        }
        List<AddressDTO> addressesDTOS = addresses.stream().map(address ->
                modelMapper.map(address, AddressDTO.class)).toList();
        return addressesDTOS;
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        address.setStreet(addressDTO.getStreet());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZip(addressDTO.getZip());
        address.setCountry(addressDTO.getCountry());

        Address updatedAddress = addressRepository.save(address);
        User user = address.getUser();
        user.getAddresses().removeIf(userAddress -> userAddress.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        User user = address.getUser();
        user.getAddresses().removeIf(userAddress -> userAddress.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(address);
        return "Address with address ID " +addressId+ " has been deleted!";
    }
}

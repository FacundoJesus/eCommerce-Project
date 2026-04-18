package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.iAddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService implements iAddressService{

    @Autowired
    iAddressRepository addressRepository;

    @Autowired
    ModelMapper modelmapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {

        Address address = modelmapper.map(addressDTO,Address.class);

        Optional<Address> isAddressExist = addressRepository
                .findByStreetAndNumber(address.getStreet(), address.getNumber());
        if (isAddressExist.isPresent())
            throw new APIException("The Address already exists.");

        Address savedAdress = addressRepository.save(isAddressExist);

        AddressDTO createdAddressDTO = modelmapper.map(isAddressExist,AddressDTO.class);

        return createdAddressDTO;
    }
}

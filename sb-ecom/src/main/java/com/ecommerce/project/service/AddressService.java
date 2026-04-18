package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.iAddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements iAddressService{

    @Autowired
    iAddressRepository addressRepository;

    @Autowired
    ModelMapper modelmapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelmapper.map(addressDTO,Address.class);

        //Obtengo la lista de direcciones del usuario, la añado a la lista y la guardo.
        List<Address> addressList = user.getAdresses();
        addressList.add(address);
        user.setAdresses(addressList);

        //Guardo el usuario en direcciones
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        AddressDTO savedAdressDTO = modelmapper.map(savedAddress, AddressDTO.class);

        return savedAdressDTO;

    }

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> listAddresses = addressRepository.findAll();
        if(listAddresses.isEmpty()) {
            throw new APIException("No Address created till now.");
        }

        List<AddressDTO> listAddressDTO = listAddresses.stream()
                .map(address -> modelmapper.map(address,AddressDTO.class))
                .toList();

        return listAddressDTO;
    }
}

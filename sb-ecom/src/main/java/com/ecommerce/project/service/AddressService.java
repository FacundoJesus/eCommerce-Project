package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.iAddressRepository;
import com.ecommerce.project.repositories.iUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService implements iAddressService{

    @Autowired
    iAddressRepository addressRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    iUserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO,Address.class);

        //Obtengo la lista de direcciones del usuario, la añado a la lista y la guardo.
        List<Address> addressList = user.getAdresses();
        addressList.add(address);
        user.setAdresses(addressList);

        //Guardo el usuario en direcciones
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        AddressDTO savedAdressDTO = modelMapper.map(savedAddress, AddressDTO.class);

        return savedAdressDTO;

    }

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> listAddresses = addressRepository.findAll();
        if(listAddresses.isEmpty()) {
            throw new APIException("No Address created till now.");
        }

        List<AddressDTO> listAddressDTO = listAddresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .toList();

        return listAddressDTO;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);

        return addressDTO;
    }

    @Override
    public List<AddressDTO> getAddressByUser(User user) {

        List<Address> listAddressByUser = user.getAdresses();

        if (listAddressByUser.isEmpty())
            throw new APIException("The User don't have any address.");

        List<AddressDTO> listAddressDTObyUser = listAddressByUser.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .toList();

        return listAddressDTObyUser;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {

        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));

        addressFromDb.setCity(addressDTO.getCity());
        addressFromDb.setState(addressDTO.getState());
        addressFromDb.setPincode(addressDTO.getPincode());
        addressFromDb.setBuildingName(addressDTO.getBuildingName());
        addressFromDb.setStreet(addressDTO.getStreet());
        addressFromDb.setCountry(addressDTO.getCountry());

        Address updatedAddress = addressRepository.save(addressFromDb);

        //Importante actualizar el usuario
        User user = addressFromDb.getUser();
        user.getAdresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAdresses().add(updatedAddress);
        userRepository.save(user);

        AddressDTO updatedAddressDTO = modelMapper.map(updatedAddress,AddressDTO.class);

        return updatedAddressDTO;
    }

    @Override
    public String deleteAddressById(Long addressId) {

        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));

        //Importante borrarlo del usuario
        User user = addressFromDb.getUser();
        user.getAdresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDb);
        return "Address deleted succesfully with addressId: " + addressId;
    }


}

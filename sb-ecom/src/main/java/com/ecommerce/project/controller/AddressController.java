package com.ecommerce.project.controller;


import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Adresses APIs", description = "APIs for managing addreses") //Swagger: Agrupar metodos
@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    AuthUtil authUtil;

    @Operation(summary = "Create address", description = "API to Create address")
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(createdAddressDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all addresses", description = "API to Get all addresses")
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> listAddressesDTO = addressService.getAllAddresses();
        return new ResponseEntity<>(listAddressesDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get address by Id", description = "API to Get address by Id")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @Operation(summary = "Get address by user", description = "API to Get address by user")
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> listAddressDTObyUser = addressService.getAddressByUser(user);
        return new ResponseEntity<>(listAddressDTObyUser,HttpStatus.OK);
    }

    @Operation(summary = "Get address by Id", description = "API to Get address by Id")
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
                                                    @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddressDTO,HttpStatus.OK);
    }

    @Operation(summary = "Delete address by Id", description = "API to Delete address by Id")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById (@PathVariable Long addressId) {
        String message = addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}

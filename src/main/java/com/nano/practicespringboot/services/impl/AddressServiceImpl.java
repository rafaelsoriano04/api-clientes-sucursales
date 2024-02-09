package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.repositories.AddressRepository;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.utilities.Utilities;
import jdk.jshell.execution.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private Utilities utilities;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl() {
        this.modelMapper = new ModelMapper();
    }


    @Override
    public AddressPresenter saveAddressByClient(Long clientId, AddressPresenter addressPresenter) {
        if (addressPresenter.getType().equals("0")) {
            if (!addressRepository.existsByType(clientId).isEmpty()) {
                utilities.throwConflictException("El cliente ya tiene dirección matris");
            }
        }
        addressPresenter.setClientId(clientId);
        return addressToPresenter(addressRepository.save(addressPresenterToAddress(addressPresenter)));
    }

    private Address addressPresenterToAddress(AddressPresenter addressPresenter) {
        Address address = modelMapper.map(addressPresenter, Address.class);
        address.setClient(clientService.getClient(addressPresenter.getClientId()));
        return address;
    }

    @Override
    public AddressPresenter addressToPresenter(Address address) {
        AddressPresenter addressPresenter = modelMapper.map(address, AddressPresenter.class);
        addressPresenter.setClientId(address.getClient().getId());
        return addressPresenter;
    }
}

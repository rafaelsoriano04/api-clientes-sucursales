package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.enums.AddressType;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.repositories.AddressRepository;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.utilities.Utilities;
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
        if (addressPresenter.getType().equals(AddressType.MATRIS)) {
            if (addressRepository.existsByType(clientId).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El cliente no existe");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El cliente ya tiene dirección matris");
        }
        addressPresenter.setType(AddressType.NORMAL);
        addressPresenter.setClientId(clientId);
        return addressToPresenter(addressRepository.save(addressPresenterToAddress(addressPresenter)));
    }

    private Address addressPresenterToAddress(AddressPresenter addressPresenter) {
        Address address = modelMapper.map(addressPresenter, Address.class);
        address.setClient(clientService.getClient(addressPresenter.getClientId()));
        return address;
    }

    private AddressPresenter addressToPresenter(Address address) {
        AddressPresenter addressPresenter = modelMapper.map(address, AddressPresenter.class);
        addressPresenter.setClientId(address.getClient().getId());
        return addressPresenter;
    }
}

package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.repositories.AddressRepository;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
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

    @Override
    public AddressPresenter saveAddressForExistingClient(AddressPresenter addressPresenter) {
        if (addressRepository.existsByType(addressPresenter.getType())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El cliente ya tiene direccion matris");
        }
        return addressToPresenter(addressRepository.save(addressPresenterToAddress(addressPresenter)));
    }

    private Address addressPresenterToAddress(AddressPresenter addressPresenter) {
        Client client = clientService.getClient(addressPresenter.getClientId());
        return Address.builder().client(client).id(addressPresenter.getId()).city(addressPresenter.getCity())
                .province(addressPresenter.getProvince()).streetName(addressPresenter.getStreetName())
                .streetNumber(addressPresenter.getStreetNumber()).type(addressPresenter.getType()).build();
    }

    private AddressPresenter addressToPresenter(Address address) {
        return AddressPresenter.builder().id(address.getId()).province(address.getProvince())
                .city(address.getCity()).streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber()).clientId(address.getClient().getId())
                .type(address.getType()).build();
    }
}

package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.AddressModel;
import com.nano.practicespringboot.entities.ClientModel;
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
        return addressModelToPresenter(addressRepository.save(addressPresenterToModel(addressPresenter)));
    }

    private AddressModel addressPresenterToModel(AddressPresenter addressPresenter) {
        ClientModel client = clientService.getClient(addressPresenter.getClientId());
        return AddressModel.builder().clientModel(client).id(addressPresenter.getId()).city(addressPresenter.getCity())
                .province(addressPresenter.getProvince()).streetName(addressPresenter.getStreetName())
                .streetNumber(addressPresenter.getStreetNumber()).type(addressPresenter.getType()).build();
    }

    private AddressPresenter addressModelToPresenter(AddressModel addressModel) {
        return AddressPresenter.builder().id(addressModel.getId()).province(addressModel.getProvince())
                .city(addressModel.getCity()).streetName(addressModel.getStreetName())
                .streetNumber(addressModel.getStreetNumber()).clientId(addressModel.getClientModel().getId())
                .type(addressModel.getType()).build();
    }
}

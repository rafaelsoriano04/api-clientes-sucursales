package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.AddressModel;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.entities.ClientModel;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AddressService addressService;

    @Override
    public List<ClientPresenter> getByParameters(String idNumber, String names) {
        return clientRepository.getByParamerters(idNumber, names).stream()
                .map(this::clientModelToPresenter).collect(Collectors.toList());
    }

    @Override
    public ClientPresenter saveClient(ClientModel clientModel) {
        if (clientRepository.existsByIdNumber(clientModel.getIdNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una persona con el idNumber=" + clientModel.getIdNumber());
        }
        return clientModelToPresenter(clientRepository.save(clientModel));
    }

    @Override
    public List<AddressPresenter> getAddressesByClient(Long id) {
        return clientRepository.getAddressesByClient(id).stream()
                .map(this::addressModelToPresenter).collect(Collectors.toList());
    }

    @Override
    public ClientModel getClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "No existe el cliente con id=" + id));
    }

    private ClientPresenter clientModelToPresenter(ClientModel client) {
        return ClientPresenter.builder().id(client.getId()).idType(client.getIdType()).idNumber(client.getIdNumber())
                .names(client.getNames()).email(client.getEmail()).phoneNumber(client.getPhoneNumber()).build();
    }

    private AddressPresenter addressModelToPresenter(AddressModel addressModel) {
        return AddressPresenter.builder().id(addressModel.getId()).city(addressModel.getCity()).province(addressModel.getProvince())
                .streetName(addressModel.getStreetName()).streetNumber(addressModel.getStreetNumber()).build();
    }

    private String getMainAddres(ClientModel clientModel) {
        return clientModel.getAddressModelList().stream()
                .filter(address -> address.getType().equals("matris"))
                .toString();
    }


}

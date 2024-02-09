package com.nano.practicespringboot.services.impl;

import ch.qos.logback.core.net.server.Client;
import com.nano.practicespringboot.entities.AddressModel;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.entities.ClientModel;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClientPresenter> getAll() {
        return clientRepository.findAll().stream().map(this::clientModelToPresenter).collect(Collectors.toList());
    }


    @Override
    public List<ClientPresenter> getByParameters(String idNumber, String names) {
        return clientRepository.getByParamerters(idNumber, names).stream()
                .map(this::clientModelToPresenter).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientPresenter saveClient(ClientPresenter clientPresenter) {
        if (clientRepository.existsByIdNumber(clientPresenter.getIdNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una persona con el idNumber=" + clientPresenter.getIdNumber());
        }
        if (clientPresenter.getMatrix() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Debe ingresar una direccion matris");
        }
        return clientModelToPresenter(clientRepository.save(clientPresenterToModel(clientPresenter)));
    }

    @Override
    public List<AddressPresenter> getAddressesByClient(Long id) {
        return clientRepository.getAddressesByClient(id).stream()
                .map(this::addressModelToPresenter).collect(Collectors.toList());
    }

    private AddressPresenter getMatrixByClient(Long id) {
        return addressModelToPresenter(clientRepository.getMatrixByClient(id));
    }

    @Override
    public ClientModel getClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "No existe el cliente con id=" + id));
    }

    private ClientPresenter clientModelToPresenter(ClientModel clientModel) {
        return ClientPresenter.builder().id(clientModel.getId()).idType(clientModel.getIdType()).idNumber(clientModel.getIdNumber())
                .names(clientModel.getNames()).email(clientModel.getEmail()).phoneNumber(clientModel.getPhoneNumber())
                .matrix(addressModelToPresenter(clientModel.getAddressModelList().get(0))).build();
    }

    
    private ClientModel clientPresenterToModel(ClientPresenter clientPresenter) {
        ClientModel clientModel = ClientModel.builder().id(clientPresenter.getId()).idType(clientPresenter.getIdType())
                .idNumber(clientPresenter.getIdNumber()).names(clientPresenter.getNames())
                .email(clientPresenter.getEmail()).phoneNumber(clientPresenter.getPhoneNumber()).build();
        //clientModel.setAddressModelList(List.of(addressPresenterToModel(clientPresenter.getMatrix(), clientModel)));
        return clientModel;
    }

    private AddressModel addressPresenterToModel(AddressPresenter addressPresenter, ClientModel clientModel) {
        return AddressModel.builder().id(addressPresenter.getId()).province(addressPresenter.getProvince())
                .city(addressPresenter.getCity()).streetNumber(addressPresenter.getStreetNumber())
                .streetName(addressPresenter.getStreetName()).type(addressPresenter.getType()).clientModel(clientModel).build();
    }

    private AddressPresenter addressModelToPresenter(AddressModel addressModel) {
        return AddressPresenter.builder().id(addressModel.getId()).city(addressModel.getCity()).province(addressModel.getProvince())
                .streetName(addressModel.getStreetName()).streetNumber(addressModel.getStreetNumber()).build();
    }


}

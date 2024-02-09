package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.ClientService;
import jakarta.transaction.Transactional;
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

    @Override
    public List<ClientPresenter> getAll() {
        return clientRepository.findAll().stream().map(this::clientToPresenter).collect(Collectors.toList());
    }


    @Override
    public List<ClientPresenter> getByParameters(String idNumber, String names) {
        return clientRepository.getByParamerters(idNumber, names).stream()
                .map(this::clientToPresenter).collect(Collectors.toList());
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
        return clientToPresenter(clientRepository.save(clientPresenterToClient(clientPresenter)));
    }

    @Override
    public List<AddressPresenter> getAddressesByClient(Long id) {
        return clientRepository.getAddressesByClient(id).stream()
                .map(this::addressToPresenter).collect(Collectors.toList());
    }

    private AddressPresenter getMatrixByClient(Long id) {
        return addressToPresenter(clientRepository.getMatrixByClient(id));
    }

    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "No existe el cliente con id=" + id));
    }

    private ClientPresenter clientToPresenter(Client client) {
        return ClientPresenter.builder().id(client.getId()).idType(client.getIdType()).idNumber(client.getIdNumber())
                .names(client.getNames()).email(client.getEmail()).phoneNumber(client.getPhoneNumber())
                .matrix(addressToPresenter(client.getAddressList().get(0))).build();
    }

    
    private Client clientPresenterToClient(ClientPresenter clientPresenter) {
        Client client = Client.builder().id(clientPresenter.getId()).idType(clientPresenter.getIdType())
                .idNumber(clientPresenter.getIdNumber()).names(clientPresenter.getNames())
                .email(clientPresenter.getEmail()).phoneNumber(clientPresenter.getPhoneNumber()).build();
        client.setAddressList(List.of(addressPresenterToAddress(clientPresenter.getMatrix(), client)));
        return client;
    }

    private Address addressPresenterToAddress(AddressPresenter addressPresenter, Client client) {
        return Address.builder().id(addressPresenter.getId()).province(addressPresenter.getProvince())
                .city(addressPresenter.getCity()).streetNumber(addressPresenter.getStreetNumber())
                .streetName(addressPresenter.getStreetName()).type(addressPresenter.getType()).client(client).build();
    }

    private AddressPresenter addressToPresenter(Address address) {
        return AddressPresenter.builder().id(address.getId()).city(address.getCity()).province(address.getProvince())
                .streetName(address.getStreetName()).streetNumber(address.getStreetNumber()).build();
    }


}

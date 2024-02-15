package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.enums.AddressType;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.utilities.Utilities;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private Utilities utilities;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl() {
        this.modelMapper = new ModelMapper();
    }

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
        if (clientRepository.existsByIdentificationNumber(clientPresenter.getIdentificationNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una persona con el idNumber=" + clientPresenter.getIdentificationNumber());
        }
        if (!utilities.validatePhoneNumber(clientPresenter.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "El número de teléfono no es válido");
        }
        if (!utilities.validateIdNumber(clientPresenter.getIdentificationType(), clientPresenter.getIdentificationNumber())) {
            utilities.throwPreconditionException("El número de RUC o CI no es válido");
        }
        if (clientPresenter.getMatrix() == null) {
            utilities.throwConflictException("Debe ingresar una dirección matris");
        }
        clientPresenter.getMatrix().setType(AddressType.MATRIS);
        return clientToPresenter(clientRepository.save(clientPresenterToClient(clientPresenter)));
    }

    @Override
    @Transactional
    public ClientPresenter updateClient(Long id, ClientPresenter request) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                "No se encuentra el registro del cliente con id=" + id));

        utilities.validatePhoneNumber(request.getPhoneNumber());
        utilities.validateIdNumber(request.getIdentificationType(), request.getIdentificationNumber());
        client.setIdentificationType(request.getIdentificationType());
        client.setIdentificationNumber(request.getIdentificationNumber());
        client.setNames(request.getNames());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        return clientToPresenter(clientRepository.save(client));
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }


    @Override
    public List<AddressPresenter> getAddressesByClient(Long id) {
        return clientRepository.getAddressesByClient(id).stream()
                .map(this::addressToPresenter).collect(Collectors.toList());
    }

    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "No existe el cliente con id=" + id));
    }

    private ClientPresenter clientToPresenter(Client client) {
        ClientPresenter clientPresenter = modelMapper.map(client, ClientPresenter.class);
        AddressPresenter addressPresenter = modelMapper.map(client.getAddressList().get(0), AddressPresenter.class);
        clientPresenter.setMatrix(addressPresenter);
        return clientPresenter;
    }

    
    private Client clientPresenterToClient(ClientPresenter clientPresenter) {
        Client client = modelMapper.map(clientPresenter, Client.class);
        Address address = modelMapper.map(clientPresenter.getMatrix(), Address.class);
        address.setClient(client);
        client.setAddressList(List.of(address));
        return client;
    }

    private AddressPresenter addressToPresenter(Address address) {
        AddressPresenter addressPresenter = modelMapper.map(address, AddressPresenter.class);
        addressPresenter.setClientId(address.getClient().getId());
        return addressPresenter;
    }
}

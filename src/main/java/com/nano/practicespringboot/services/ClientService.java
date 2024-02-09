package com.nano.practicespringboot.services;

import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;

import java.util.List;

public interface ClientService {
    List<ClientPresenter> getByParameters(String idNumber, String names);
    ClientPresenter saveClient(ClientPresenter clientPresenter);

    List<AddressPresenter> getAddressesByClient(Long id);

    Client getClient(Long id);

    List<ClientPresenter> getAll();

    ClientPresenter updateClient(Long id, ClientPresenter request);

    void deleteClient(Long id);
}

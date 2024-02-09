package com.nano.practicespringboot.services;

import com.nano.practicespringboot.entities.AddressModel;
import com.nano.practicespringboot.entities.ClientModel;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;

import java.util.List;

public interface ClientService {
    List<ClientPresenter> getByParameters(String idNumber, String names);

    ClientPresenter saveClient(ClientModel clientModel);

    List<AddressPresenter> getAddressesByClient(Long id);

    ClientModel getClient(Long id);

}

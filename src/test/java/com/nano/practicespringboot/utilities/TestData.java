package com.nano.practicespringboot.utilities;


import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.enums.AddressType;
import com.nano.practicespringboot.enums.IdentificationType;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;

import java.util.List;

public class TestData {
    public Client getClientInstance() {
        Client client = new Client();
        client.setId(1L);
        client.setIdentificationType(IdentificationType.CI);
        client.setIdentificationNumber("1805468467");
        client.setNames("Rafael Soriano");
        client.setAddressList(List.of(new Address()));

        return client;
    }

    public ClientPresenter getClientPresenterInstance() {
        ClientPresenter clientPresenter = new ClientPresenter();
        clientPresenter.setId(1L);
        clientPresenter.setIdentificationType(IdentificationType.CI);
        clientPresenter.setIdentificationNumber("1805468467");
        clientPresenter.setNames("Rafa");
        AddressPresenter addressPresenter = new AddressPresenter(1L, "Tun", "Amb",
                "Agua", "02", AddressType.MATRIS, 1L);
        clientPresenter.setMatrix(addressPresenter);
        return clientPresenter;
    }
}

package com.nano.practicespringboot.services;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.presenters.AddressPresenter;

public interface AddressService {
    AddressPresenter saveAddressByClient(Long clientId, AddressPresenter addressPresenter);
    AddressPresenter addressToPresenter(Address address);
}

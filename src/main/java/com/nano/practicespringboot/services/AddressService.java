package com.nano.practicespringboot.services;

import com.nano.practicespringboot.presenters.AddressPresenter;

public interface AddressService {
    AddressPresenter saveAddressForExistingClient(AddressPresenter addressPresenter);
}

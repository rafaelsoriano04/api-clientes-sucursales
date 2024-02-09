package com.nano.practicespringboot.controllers;

import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    public AddressPresenter saveAddressForExistingClient(@RequestBody AddressPresenter addressPresenter) {
        return addressService.saveAddressForExistingClient(addressPresenter);
    }
}

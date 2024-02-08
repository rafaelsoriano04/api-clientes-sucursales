package com.nano.practicespringboot.controllers;

import com.nano.practicespringboot.entities.ClientModel;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/list/parameter")
    public List<ClientPresenter> getByParameters(@RequestParam(required = false) String idNumber, @RequestParam(required = false) String names) {
        return clientService.getByParameters(idNumber, names);
    }

    @PostMapping
    public ClientPresenter saveClient(@RequestBody ClientModel clientModel) {
        return clientService.saveClient(clientModel);
    }
}


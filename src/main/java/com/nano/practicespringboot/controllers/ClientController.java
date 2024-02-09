package com.nano.practicespringboot.controllers;

import com.nano.practicespringboot.presenters.AddressPresenter;
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

    @GetMapping
    public List<ClientPresenter> getAll(){
        return clientService.getAll();
    }

    @GetMapping("/parameter")
    public List<ClientPresenter> getByParameters(@RequestParam(required = false) String idNumber, @RequestParam(required = false) String names) {
        return clientService.getByParameters(idNumber, names);
    }

    @PutMapping("/{id}")
    public ClientPresenter updateClient(@PathVariable Long id, @RequestBody ClientPresenter request) {
        return clientService.updateClient(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }


    @GetMapping("/{id}/address")
    public List<AddressPresenter> getAddressByClient(@PathVariable Long id) {
        return clientService.getAddressesByClient(id);
    }

    @PostMapping
    public ClientPresenter saveClient(@RequestBody ClientPresenter clientPresenter) {
        return clientService.saveClient(clientPresenter);
    }
}


package com.nano.practicespringboot.controllers;

import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    private final TestData testData = new TestData();

    @Test
    void shouldGetAll() {
        List<ClientPresenter> clientPresenterList = List.of(testData.getClientPresenterInstance());
        when(clientService.getAll()).thenReturn(clientPresenterList);

        List<ClientPresenter> result = clientController.getAll();

        verify(clientService, times(1)).getAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void shouldGetByParameters() {
        List<ClientPresenter> clientPresenterList = List.of(testData.getClientPresenterInstance());
        when(clientService.getByParameters(any(), any())).thenReturn(clientPresenterList);

        List<ClientPresenter> result = clientController.getByParameters("180", "Rafa");

        verify(clientService, times(1)).getByParameters(any(), any());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void shouldUpdateClient() {
        when(clientService.updateClient(any(), any())).thenReturn(testData.getClientPresenterInstance());

        ClientPresenter result = clientController.updateClient(1L, testData.getClientPresenterInstance());

        verify(clientService).updateClient(any(), any());
        verify(clientService, times(1)).updateClient(any(), any());
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void deleteClient() {
        clientController.deleteClient(1L);
        verify(clientService).deleteClient(1L);
        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void getAddressByClient() {
        List<AddressPresenter> clientPresenterList = List.of(testData.getClientPresenterInstance().getMatrix());
        when(clientService.getAddressesByClient(any())).thenReturn(clientPresenterList);

        List<AddressPresenter> result = clientController.getAddressByClient(1L);

        verify(clientService, times(1)).getAddressesByClient(any());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void saveClient() {
        when(clientService.saveClient(any())).thenReturn(testData.getClientPresenterInstance());

        ClientPresenter clientPresenter = clientController.saveClient(testData.getClientPresenterInstance());

        verify(clientService, times(1)).saveClient(any());
        Assertions.assertThat(clientPresenter).isNotNull();
    }
}
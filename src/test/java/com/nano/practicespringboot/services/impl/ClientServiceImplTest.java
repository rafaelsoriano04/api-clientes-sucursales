package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.enums.AddressType;
import com.nano.practicespringboot.enums.IdentificationType;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.TestData;
import com.nano.practicespringboot.utilities.Utilities;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @InjectMocks
    private ClientService clientService = new ClientServiceImpl();
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private Utilities utilities;
    @Spy
    private ModelMapper modelMapper;

    private final TestData testData = new TestData();

    @BeforeEach
    public void prepare() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    @Test
    void shouldGetAll() {
        List<Client> clientList = List.of(testData.getClientInstance());
        when(clientRepository.findAll()).thenReturn(clientList);

        List<ClientPresenter> result = clientService.getAll();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getMatrix().getId())
                .isEqualTo(clientList.get(0).getAddressList().get(0).getId());
        Assertions.assertThat(result.get(0).getMatrix().getClientId())
                .isEqualTo(clientList.get(0).getAddressList().get(0).getClient().getId());
    }

    @Test
    void shouldGetAllEmptyList() {
        List<Client> clientList = new ArrayList<>();
        when(clientRepository.findAll()).thenReturn(clientList);

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.getAll();
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("No existen clientes");
    }

    @Test
    void shouldGetByParametersEmptyList() {
        List<Client> clientList = new ArrayList<>();
        when(clientRepository.getByParameters(any(), any())).thenReturn(clientList);

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.getByParameters(any(), any());
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("No existen clientes");
    }

    @Test
    void shouldGetByParameters() {
        List<Client> clientList = List.of(testData.getClientInstance());

        when(clientRepository.getByParameters(any(), any())).thenReturn(clientList);

        ArgumentCaptor<String> stringArgumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> stringArgumentCaptor2 = ArgumentCaptor.forClass(String.class);
        List<ClientPresenter> result = clientService.getByParameters(clientList.get(0).getIdentificationNumber(),
                clientList.get(0).getNames());

        verify(clientRepository).getByParameters(stringArgumentCaptor1.capture(), stringArgumentCaptor2.capture());
        String idNumber = stringArgumentCaptor1.getValue();
        String names = stringArgumentCaptor2.getValue();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(idNumber).isEqualTo(clientList.get(0).getIdentificationNumber());
        Assertions.assertThat(names).isEqualTo(clientList.get(0).getNames());
    }

    @Test
    void shouldSaveClient() {
        ClientPresenter clientPresenter = testData.getClientPresenterInstance();

        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        when(clientRepository.save(any())).thenReturn(testData.getClientInstance());

        ClientPresenter result = clientService.saveClient(clientPresenter);

        verify(clientRepository).save(clientCaptor.capture());
        Client capturedClient = clientCaptor.getValue();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getMatrix()).isNotNull();
        Assertions.assertThat(result.getMatrix().getType()).isEqualTo(AddressType.MATRIS);
        Assertions.assertThat(capturedClient.getId()).isEqualTo(1L);
        Assertions.assertThat(capturedClient.getNames()).isEqualTo("Rafa");
        Assertions.assertThat(capturedClient.getIdentificationNumber()).isEqualTo("1805468467");
        Assertions.assertThat(capturedClient.getAddressList().get(0).getId())
                .isEqualTo(clientPresenter.getMatrix().getId());
        Assertions.assertThat(capturedClient.getAddressList().get(0).getClient().getId())
                .isEqualTo(clientPresenter.getId());
    }

    @Test
    void shouldSaveClientFailedByAddressType() {
        ClientPresenter clientPresenter = testData.getClientPresenterInstance();
        clientPresenter.getMatrix().setType(AddressType.NORMAL);

        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.saveClient(clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("Debe ingresar una dirección matris");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldSaveClientFailedByIdNumberExists() {
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(true);

        ClientPresenter clientPresenter = ClientPresenter.builder().identificationNumber("1").build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.saveClient(clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("Ya existe una persona con el idNumber=" + clientPresenter.getIdentificationNumber());

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldSaveClientFailedByInvalidPhoneNumber() {
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);

        ClientPresenter clientPresenter = ClientPresenter.builder()
                .phoneNumber("091").build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.saveClient(clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El número de teléfono no es válido");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldSaveClientFailedByInvalidIdNumber() {
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);
        when(utilities.validatePhoneNumber(any())).thenReturn(true);

        ClientPresenter clientPresenter = ClientPresenter.builder()
                .identificationType(IdentificationType.CI)
                .identificationNumber("01").build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.saveClient(clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El número de RUC o CI no es válido");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldSaveClientFailedByNullMatrix() {
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);

        ClientPresenter clientPresenter = ClientPresenter.builder()
                .matrix(null).build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.saveClient(clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("Debe ingresar una dirección matris");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClient() {
        ClientPresenter clientPresenter = testData.getClientPresenterInstance();
        clientPresenter.setIdentificationType(IdentificationType.RUC);
        clientPresenter.setIdentificationNumber("1803332715001");
        clientPresenter.setNames("Juan");
        clientPresenter.setEmail("juan@");
        clientPresenter.setPhoneNumber("0987289560");

        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.getClientInstance()));
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);
        when(clientRepository.save(any())).thenReturn(testData.getClientInstance());
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);

        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);

        ClientPresenter result = clientService.updateClient(1L, clientPresenter);

        verify(clientRepository).save(clientArgumentCaptor.capture());
        Client updatedClient = clientArgumentCaptor.getValue();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(updatedClient.getIdentificationType()).isEqualTo(clientPresenter.getIdentificationType());
        Assertions.assertThat(updatedClient.getIdentificationNumber()).isEqualTo(clientPresenter.getIdentificationNumber());
        Assertions.assertThat(updatedClient.getNames()).isEqualTo(clientPresenter.getNames());
        Assertions.assertThat(updatedClient.getEmail()).isEqualTo(clientPresenter.getEmail());
        Assertions.assertThat(updatedClient.getPhoneNumber()).isEqualTo(clientPresenter.getPhoneNumber());
    }

    @Test
    void shouldUpdateClientFailedByIdNotFound() {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.updateClient(1L, testData.getClientPresenterInstance());
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("No se encuentra el registro del cliente con id=" + 1L);

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClientFailedByPhoneNumber() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.getClientInstance()));

        ClientPresenter clientPresenter = ClientPresenter.builder()
                .phoneNumber("091").build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.updateClient(1L, clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El número de teléfono no es válido");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClientFailedByInvalidIdNumber() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.getClientInstance()));
        when(utilities.validatePhoneNumber(any())).thenReturn(true);

        ClientPresenter clientPresenter = ClientPresenter.builder()
                .identificationType(IdentificationType.CI)
                .identificationNumber("01").build();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.updateClient(1L, clientPresenter);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El número de RUC o CI no es válido");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClientFailedByExistingIdNumber() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(testData.getClientInstance()));
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);
        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(true);

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.updateClient(1L, testData.getClientPresenterInstance());
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("Ya existe una persona con el idNumber=" + testData.getClientPresenterInstance().getIdentificationNumber());

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldDeleteClient() {
        clientService.deleteClient(1L);
        verify(clientRepository).deleteById(any());
    }

    @Test
    void shouldGetAddressByClient() {
        List<Address> addressList = testData.getClientInstance().getAddressList();

        when(clientRepository.getAddressesByClient(any())).thenReturn(addressList);

        List<AddressPresenter> result = clientService.getAddressesByClient(1l);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(addressList.get(0).getId()).isEqualTo(result.get(0).getClientId());

    }

    @Test
    void shouldGetClient() {
        Client client = testData.getClientInstance();
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        Client result = clientService.getClient(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(client.getId()).isEqualTo(result.getId());
    }

    @Test
    void shouldGetClientFailedByIdNotFound() {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            clientService.getClient(1L);
        });

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("No existe el cliente con id=" + 1L);
    }
}
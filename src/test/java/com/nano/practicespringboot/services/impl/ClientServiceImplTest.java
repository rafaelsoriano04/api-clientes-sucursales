package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.presenters.ClientPresenter;
import com.nano.practicespringboot.repositories.ClientRepository;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.entities.Client;
import com.nano.practicespringboot.utilities.TestData;
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
    void shouldSaveClient() {
        Client clientExpected = testData.getClientInstance();
        ClientPresenter clientPresenter = testData.getClientPresenterInstance();

        when(clientRepository.existsByIdentificationNumber(any())).thenReturn(false);
        when(utilities.validatePhoneNumber(any())).thenReturn(true);
        when(utilities.validateIdNumber(any(), any())).thenReturn(true);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        when(clientRepository.save(any())).thenReturn(clientExpected);

        ClientPresenter result = clientService.saveClient(clientPresenter);

        verify(clientRepository).save(clientCaptor.capture());
        Client capturedClient = clientCaptor.getValue();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getMatrix()).isNotNull();
        Assertions.assertThat(capturedClient.getId()).isEqualTo(1L);
        Assertions.assertThat(capturedClient.getNames()).isEqualTo("Rafa");
        Assertions.assertThat(capturedClient.getIdentificationNumber()).isEqualTo("1805468467");
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
}
package com.nano.practicespringboot.services.impl;

import com.nano.practicespringboot.entities.Address;
import com.nano.practicespringboot.enums.AddressType;
import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.repositories.AddressRepository;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.services.ClientService;
import com.nano.practicespringboot.TestData;
import com.nano.practicespringboot.utilities.Utilities;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressService addressService = new AddressServiceImpl();
    @Mock
    private ClientService clientService;
    @Mock
    private AddressRepository addressRepository;
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
    void saveAddressByClient() {
        AddressPresenter addressPresenter = testData.getClientPresenterInstance().getMatrix();
        addressPresenter.setType(AddressType.NORMAL);

        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        when(addressRepository.save(any())).thenReturn(testData.getClientInstance().getAddressList().get(0));
        when(clientService.getClient(any())).thenReturn(testData.getClientInstance());

        AddressPresenter result = addressService.saveAddressByClient(addressPresenter);

        verify(addressRepository).save(addressCaptor.capture());
        Address capturedAddress = addressCaptor.getValue();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(capturedAddress.getId()).isEqualTo(addressPresenter.getId());
        Assertions.assertThat(capturedAddress.getClient().getId()).isEqualTo(addressPresenter.getClientId());
        Assertions.assertThat(capturedAddress.getClient().getIdentificationNumber()).isNotNull();
    }

    @Test
    void saveAddressByClientFailedByType() {
        AddressPresenter addressPresenter = testData.getClientPresenterInstance().getMatrix();
        List<Address> addressList = testData.getClientInstance().getAddressList();
        when(clientService.getClient(any())).thenReturn(testData.getClientInstance());

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            addressService.saveAddressByClient(addressPresenter);
        });

        verify(addressRepository, never()).save(any());

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El cliente ya tiene una dirección matris");
    }

    @Test
    void saveAddressByClientFailedByNotExistingId() {
        AddressPresenter addressPresenter = testData.getClientPresenterInstance().getMatrix();

        ResponseStatusException responseStatusException = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            addressService.saveAddressByClient(addressPresenter);
        });

        verify(addressRepository, never()).save(any());

        Assertions.assertThat(responseStatusException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(responseStatusException.getReason()).isEqualTo("El cliente no existe");

    }
}
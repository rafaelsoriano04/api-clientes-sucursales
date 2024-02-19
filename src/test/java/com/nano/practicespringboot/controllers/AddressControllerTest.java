package com.nano.practicespringboot.controllers;

import com.nano.practicespringboot.presenters.AddressPresenter;
import com.nano.practicespringboot.services.AddressService;
import com.nano.practicespringboot.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {
    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    private final TestData testData = new TestData();

    @Test
    void shouldSaveAddressByClient() {
        AddressPresenter addressPresenter = testData.getClientPresenterInstance().getMatrix();
        when(addressService.saveAddressByClient(any()))
                .thenReturn(addressPresenter);

        AddressPresenter result = addressController.saveAddressByClient(addressPresenter);

        verify(addressService, times(1)).saveAddressByClient(any());
        Assertions.assertThat(result).isNotNull();
    }
}
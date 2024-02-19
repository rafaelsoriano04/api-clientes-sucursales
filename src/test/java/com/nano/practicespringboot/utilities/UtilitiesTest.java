package com.nano.practicespringboot.utilities;

import com.nano.practicespringboot.enums.IdentificationType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilitiesTest {

    @InjectMocks
    @Spy
    private Utilities utilities;

    @Test
    void shouldValidatePhoneNumber() {
        String validPhoneNumber = "0123456789";

        boolean result = utilities.validatePhoneNumber(validPhoneNumber);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldValidatePhoneNumberFailedByNotANumber() {
        String invalidPhoneNumber = "Rafael Soriano";

        boolean result = utilities.validatePhoneNumber(invalidPhoneNumber);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void shouldValidatePhoneNumberFailedByLength() {
        String invalidPhoneNumber = "1";

        boolean result = utilities.validatePhoneNumber(invalidPhoneNumber);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void shouldValidateIdNumberByRuc() {
        boolean result = utilities.validateIdNumber(IdentificationType.RUC, "1803375896001");

        verify(utilities, times(1)).validateRuc(any());
        verify(utilities, never()).validateCi(any());
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldValidateIdNumberByCi() {
        when(utilities.validateCi(any())).thenAnswer(invocation -> {
            String ci = invocation.getArgument(0);
            Assertions.assertThat(ci).isEqualTo("1803375896");
            return true;
        });

        boolean result = utilities.validateIdNumber(IdentificationType.CI, "1803375896");

        verify(utilities, times(1)).validateCi(any());
        verify(utilities, never()).validateRuc(any());
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldValidateRucFailedByLength() {
        boolean result = utilities.validateRuc( "1803375896");
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void shouldValidateCiFailedByLengthAndNull() {
        boolean result = utilities.validateCi("180337589");
        boolean result2 = utilities.validateCi(null);
        Assertions.assertThat(result).isFalse();
        Assertions.assertThat(result2).isFalse();
    }

    @Test
    void shouldValidateCi() {
        boolean result = utilities.validateCi("1803375896");
        Assertions.assertThat(result).isTrue();
    }
}
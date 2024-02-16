package com.nano.practicespringboot.utilities;

import com.nano.practicespringboot.enums.IdentificationType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilitiesTest {
    private Utilities utilities = new Utilities();

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

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldValidateIdNumberByCi() {
        boolean result = utilities.validateIdNumber(IdentificationType.CI, "1803375896");

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
}
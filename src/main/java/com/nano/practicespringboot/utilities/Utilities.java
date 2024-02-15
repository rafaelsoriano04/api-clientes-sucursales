package com.nano.practicespringboot.utilities;

import com.nano.practicespringboot.enums.IdentificationType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class Utilities {
    public boolean validateIdNumber(IdentificationType type, String idNumber) {
        if (type.equals(IdentificationType.RUC)) {
            return validateRuc(idNumber);
        }
        return validateCi(idNumber);
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        try {
            Integer number = Integer.parseInt(phoneNumber);
            return phoneNumber.length() == 10;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateCi(String ci) {
        boolean validation;
        // Verificar que la cédula tenga 10 dígitos
        if (ci == null || ci.length() != 10) {
            validation = false;
        } else {
            // Extraer los primeros 9 dígitos de la cédula
            String digits = ci.substring(0, 9);
            // Calcular el dígito verificador
            int sum = 0;
            int mult = 2;
            for (int i = digits.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(digits.charAt(i));
                int result = digit * mult;
                if (result >= 10) {
                    result -= 9;
                }
                sum += result;
                mult = (mult == 2) ? 1 : 2;
            }
            int verifier = (10 - (sum % 10)) % 10;
            // Verificar que el último dígito de la cédula sea igual al dígito verificador
            int lastDigit = Character.getNumericValue(ci.charAt(9));
            validation = (lastDigit == verifier);
        }
        return validation;
    }

    private boolean validateRuc(String ruc) {
        boolean validation;
        // Verificar que la cédula tenga 10 dígitos
        if (ruc == null || ruc.length() != 13) {
            validation = false;
        } else {
            // Extraer los primeros 9 dígitos de la cédula
            String digits = ruc.substring(0, 9);
            // Calcular el dígito verificador
            int sum = 0;
            int mult = 2;
            for (int i = digits.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(digits.charAt(i));
                int result = digit * mult;
                if (result >= 10) {
                    result -= 9;
                }
                sum += result;
                mult = (mult == 2) ? 1 : 2;
            }
            int verifier = (10 - (sum % 10)) % 10;
            // Verificar que el último dígito de la cédula sea igual al dígito verificador
            int lastDigit = Character.getNumericValue(ruc.charAt(9));
            validation = (lastDigit == verifier);
            if (validation) {
                if (ruc.charAt(10) == '0' && ruc.charAt(11) == '0') {
                    if (ruc.charAt(12) == '1' || ruc.charAt(12) == '2' || ruc.charAt(12) == '3') {
                        validation = true;
                    } else {
                        validation = false;
                    }
                } else {
                    validation = false;
                }
            }
        }
        return validation;
    }

    public void throwPreconditionException(String message) {
        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                message);
    }

    public void throwConflictException(String message) {
        throw new ResponseStatusException(HttpStatus.CONFLICT,
                message);
    }


}

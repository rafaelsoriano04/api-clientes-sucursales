package com.nano.practicespringboot.presenters;

import com.nano.practicespringboot.enums.IdentificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientPresenter {
    private Long id;
    private IdentificationType identificationType; // 0=Ruc 1=Cedula
    private String identificationNumber;
    private String names;
    private String email;
    private String phoneNumber;
    private AddressPresenter matrix;
}

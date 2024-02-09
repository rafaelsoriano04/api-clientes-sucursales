package com.nano.practicespringboot.presenters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientPresenter {
    private Long id;
    private String idType; // 0=Ruc 1=Cedula
    private String idNumber;
    private String names;
    private String email;
    private Integer phoneNumber;
    private AddressPresenter matrix;
}

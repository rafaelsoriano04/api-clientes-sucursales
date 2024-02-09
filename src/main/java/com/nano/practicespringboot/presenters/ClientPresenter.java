package com.nano.practicespringboot.presenters;

import com.nano.practicespringboot.entities.AddressModel;
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
    private String idType; // 0=Ruc 1=Cedula
    private String idNumber;
    private String names;
    private String email;
    private Integer phoneNumber;
    private String mainAddress;
    private String mainProvince;
    private String mainCity;
}

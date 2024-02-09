package com.nano.practicespringboot.presenters;

import com.nano.practicespringboot.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressPresenter {
    private Long id;
    private String province;
    private String city;
    private String streetName;
    private String streetNumber;
    private AddressType type;
    private Long clientId;
}

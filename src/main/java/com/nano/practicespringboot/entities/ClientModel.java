package com.nano.practicespringboot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idType; // RUC o CI
    private String idNumber;
    private String names;
    private String email;
    private Integer phoneNumber;
    @OneToMany(mappedBy = "clientModel", cascade = CascadeType.ALL)
    private List<AddressModel> addressModelList;
}

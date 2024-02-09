package com.nano.practicespringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idType; // RUC o CI
    private String idNumber;
    private String names;
    private String email;
    private Integer phoneNumber;
    @OneToMany(mappedBy = "clientModel")
    private List<AddressModel> addressModelList;
}

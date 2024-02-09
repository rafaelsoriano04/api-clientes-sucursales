package com.nano.practicespringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String province;
    private String city;
    private String streetName;
    private String streetNumber;
    private String type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientModel clientModel;
}

package com.nano.practicespringboot.entities;

import com.nano.practicespringboot.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String province;
    private String city;
    private String streetName;
    private String streetNumber;
    @Enumerated(EnumType.STRING)
    private AddressType type; // Matris o Normal

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

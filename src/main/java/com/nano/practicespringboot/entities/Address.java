package com.nano.practicespringboot.entities;

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
    private String type; // 0=Matris 1=Normal

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

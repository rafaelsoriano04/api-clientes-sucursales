package com.nano.practicespringboot.repositories;

import com.nano.practicespringboot.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Boolean existsByType(String type);
}

package com.nano.practicespringboot.repositories;

import com.nano.practicespringboot.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a " +
            "FROM Address a " +
            "WHERE a.client.id = :id " +
            "AND a.type = '0'")
    List<Address> existsByType(Long id);
}

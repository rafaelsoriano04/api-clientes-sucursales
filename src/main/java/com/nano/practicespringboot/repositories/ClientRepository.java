package com.nano.practicespringboot.repositories;

import com.nano.practicespringboot.entities.AddressModel;
import com.nano.practicespringboot.entities.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {
    @Query("SELECT c " +
            "FROM ClientModel c " +
            "WHERE c.idNumber = COALESCE(:idNumber, c.idNumber) AND c.names = COALESCE(:names, c.names)")
    List<ClientModel> getByParamerters(String idNumber, String names);

    Boolean existsByIdNumber (String idNumber);

    @Query("SELECT a " +
            "FROM AddressModel a " +
            "WHERE a.clientModel.id = :id")
    List<AddressModel> getAddressesByClient(Long id);

    @Query("SELECT a " +
            "FROM AddressModel a " +
            "WHERE a.clientModel.id > :id " +
            "AND a.type = '0'")
    AddressModel getMatrixByClient(Long id);


}

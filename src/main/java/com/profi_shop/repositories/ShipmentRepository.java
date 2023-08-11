package com.profi_shop.repositories;

import com.profi_shop.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query("SELECT DISTINCT s.state FROM Shipment s")
    List<String> findDistinctStates();
    @Query("SELECT DISTINCT s.town FROM Shipment s")
    List<String> findDistinctTown();

    @Query("SELECT s.town FROM Shipment s WHERE s.state = :state")
    List<String> findTownsByState(String state);

    Optional<Shipment> findByTownAndState(String town,String state);
}

package com.profi_shop.model;

import com.profi_shop.model.enums.ShipmentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String town;

    private String stateCAPS;
    private String townCAPS;
    private int cost;
}

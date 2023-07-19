package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "consumptions")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Store store;
    private String description;
    private int amount;
}

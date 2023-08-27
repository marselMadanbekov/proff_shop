package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String town;
    private int balance;
    private String address;
    private String email;
    @OneToOne
    private User admin;
    private String phone_number;


    public void balanceUp(Integer sum){
        this.balance += sum;
    }

    public void balanceDown(Integer sum){
        this.balance -= sum;
    }
}

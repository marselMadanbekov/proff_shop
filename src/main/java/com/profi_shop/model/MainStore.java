package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class MainStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String town;
    private String state;
    private String address;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> phone_numbers = new ArrayList<>();
    private String working_time;
    private String email;
    private String instagram;
    private int balance;

    public void addPhoneNumber(String phone_number){
        this.phone_numbers.add(phone_number);
    }

    public void deletePhone(String phone) {
        this.phone_numbers.remove(phone);
    }

    public void balanceUp(int sum){
        this.balance += sum;
    }
}

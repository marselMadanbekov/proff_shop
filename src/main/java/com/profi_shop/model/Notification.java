package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @ManyToOne
    private User user;
    private boolean viewed;
    private LocalDateTime date;
    @PrePersist
    public void onCreate(){
        this.date = LocalDateTime.now();
    }
}

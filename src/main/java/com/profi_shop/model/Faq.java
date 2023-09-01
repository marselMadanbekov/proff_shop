package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @Column(columnDefinition = "text")
    private String answer;
}

package com.profi_shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MainPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carouselFirstPhoto;
    private String carouselSecondPhoto;
    private String carouselThirdPhoto;
    private String carouselFirstText;
    private String carouselSecondText;
    private String carouselThirdText;
    private String delivery;
    private String payment;
    private String friendlyService;
    private String salesPhoto;
    private String salesText;
    private String topProductsPhoto;
    private String topProductsText;
}

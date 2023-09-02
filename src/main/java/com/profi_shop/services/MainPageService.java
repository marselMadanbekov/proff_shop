package com.profi_shop.services;

import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.model.MainPage;
import com.profi_shop.repositories.MainPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MainPageService {
    private final MainPageRepository mainPageRepository;
    private final PhotoService photoService;

    @Autowired
    public MainPageService(MainPageRepository mainPageRepository, PhotoService photoService) {
        this.mainPageRepository = mainPageRepository;
        this.photoService = photoService;
    }

    public MainPage getMainPage() {
        Optional<MainPage> mainPage = mainPageRepository.findFirstById(1L);
        return mainPage.orElseGet(() -> {
            MainPage newMainStore = new MainPage();
            return mainPageRepository.save(newMainStore);
        });
    }

    public void setFirstItemOnSlider(MultipartFile photo, String firstPhotoText) throws Exception {
        MainPage mainPage = getMainPage();
        if (photo != null && !photo.isEmpty()) {
            if (mainPage.getCarouselFirstPhoto() != null) {
                try {
                    photoService.deletePhoto(mainPage.getCarouselFirstPhoto());
                } catch (IOException ignored) {
                }
            }
            try {

                String firstPhoto = photoService.saveProductPhotoWithoutResizing(photo);
                mainPage.setCarouselFirstPhoto(firstPhoto);
            } catch (IOException e) {
                throw new Exception("Ошибка при загрузке фото");
            }
        }
        if (firstPhotoText != null && !firstPhotoText.equals("")) {
            mainPage.setCarouselFirstText(firstPhotoText);
        }
        mainPageRepository.save(mainPage);
    }

    public void setSecondItemOnSlider(MultipartFile photo, String secondPhotoText) throws Exception {
        MainPage mainPage = getMainPage();
        if (photo != null && !photo.isEmpty()) {
            if (mainPage.getCarouselSecondPhoto() != null) {
                try {
                    photoService.deletePhoto(mainPage.getCarouselSecondPhoto());
                } catch (IOException ignored) {
                }
            }
            try {

                String secondPhoto = photoService.saveProductPhotoWithoutResizing(photo);
                mainPage.setCarouselSecondPhoto(secondPhoto);
            } catch (IOException e) {
                throw new Exception("Ошибка при загрузке фото");
            }
        }
        if (secondPhotoText != null && !secondPhotoText.equals("")) {
            mainPage.setCarouselSecondText(secondPhotoText);
        }
        mainPageRepository.save(mainPage);
    }

    public void setThirdItemOnSlider(MultipartFile photo, String thirdPhotoText) throws Exception {
        MainPage mainPage = getMainPage();
        if (photo != null && !photo.isEmpty()) {
            if (mainPage.getCarouselThirdPhoto() != null) {
                try {
                    photoService.deletePhoto(mainPage.getCarouselThirdPhoto());
                } catch (IOException ignored) {
                }
            }
            try {

                String thirdPhoto = photoService.saveProductPhotoWithoutResizing(photo);
                mainPage.setCarouselThirdPhoto(thirdPhoto);
            } catch (IOException e) {
                throw new Exception("Ошибка при загрузке фото");
            }
        }
        if (thirdPhotoText != null && !thirdPhotoText.equals("")) {
            mainPage.setCarouselThirdText(thirdPhotoText);
        }
        mainPageRepository.save(mainPage);
    }

    public void setSalesItem(MultipartFile photo, String salesPhotoText) throws Exception {
        MainPage mainPage = getMainPage();
        if (photo != null && !photo.isEmpty()) {
            if (mainPage.getSalesPhoto() != null) {
                try {
                    photoService.deletePhoto(mainPage.getSalesPhoto());
                } catch (IOException ignored) {
                }
            }
            try {

                String salesPhoto = photoService.saveProductPhotoWithoutResizing(photo);
                mainPage.setSalesPhoto(salesPhoto);
            } catch (IOException e) {
                throw new Exception("Ошибка при загрузке фото");
            }
        }
        if (salesPhotoText != null && !salesPhotoText.equals("")) {
            mainPage.setSalesText(salesPhotoText);
        }
        mainPageRepository.save(mainPage);
    }

    public void setTopProductItem(MultipartFile photo, String topProductText) throws Exception {
        MainPage mainPage = getMainPage();
        if (photo != null && !photo.isEmpty()) {
            if (mainPage.getTopProductsPhoto() != null) {
                try {
                    photoService.deletePhoto(mainPage.getTopProductsPhoto());
                } catch (IOException ignored) {
                }
            }
            try {

                String topPhoto = photoService.saveProductPhotoWithoutResizing(photo);
                mainPage.setTopProductsPhoto(topPhoto);
            } catch (IOException e) {
                throw new Exception("Ошибка при загрузке фото");
            }
        }
        if (topProductText != null && !topProductText.equals("")) {
            mainPage.setTopProductsText(topProductText);
        }
        mainPageRepository.save(mainPage);
    }

    public void setDeliveryDescription(String delivery) {
        MainPage mainPage = getMainPage();
        mainPage.setDelivery(delivery);
        mainPageRepository.save(mainPage);
    }
    public void setPaymentDescription(String payment){
        MainPage mainPage = getMainPage();
        mainPage.setPayment(payment);
        mainPageRepository.save(mainPage);
    }
    public void setServiceDescription(String service){
        MainPage mainPage = getMainPage();
        mainPage.setFriendlyService(service);
        mainPageRepository.save(mainPage);
    }
}

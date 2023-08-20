package com.profi_shop;

import com.profi_shop.settings.Templates;
import com.profi_shop.settings.Text;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProfiShopApplication {

    public static void main(String[] args) {
        Text.init();
        Templates.init();
        SpringApplication.run(ProfiShopApplication.class, args);
    }

}

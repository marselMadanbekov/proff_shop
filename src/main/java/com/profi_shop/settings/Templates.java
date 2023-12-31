package com.profi_shop.settings;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class Templates {

    public static final String COUPON_PREFIX = "PROFFSHOP-";
    public static final String SITE_NAME_FILLER = "SITE_NAME";
    public static final String PASSWORD_FILLER = "NEW_PASSWORD";
    public static final String numberBody = "nnn nnn nnn";
    public static List<String> numberPrefixes = new ArrayList<>();
    public static final String timeTemplate = "HH:MM";

    public static void init(){
        numberPrefixes.add("0");
        numberPrefixes.add("+996");
    }
}

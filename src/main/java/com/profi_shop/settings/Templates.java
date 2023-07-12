package com.example.academia1_1.infrastructure.settings;

import java.util.ArrayList;
import java.util.List;

public class Templates {
    public static final String numberBody = "nnn nnn nnn";
    public static List<String> numberPrefixes = new ArrayList<>();
    public static final String timeTemplate = "HH:MM";

    public static void init(){
        numberPrefixes.add("0");
        numberPrefixes.add("+996");
    }
}

package com.profi_shop.settings;

import java.util.HashMap;

public class Text {
    private static final HashMap<String, String> data = new HashMap<>();
    public static String get(String key){
        return data.get(key);
    }
    public static String[] getMonths(){
        String[] months = new String[12];
        months[0] = get("JANUARY");
        months[1] = get("FEBRUARY");
        months[2] = get("MARCH");
        months[3] = get("APRIL");
        months[4] = get("MAY");
        months[5] = get("JUNE");
        months[6] = get("JULY");
        months[7] = get("AUGUST");
        months[8] = get("SEPTEMBER");
        months[9] = get("OCTOBER");
        months[10] = get("NOVEMBER");
        months[11] = get("DECEMBER");
        return months;
    }
    public static void init(){
        data.put("PROGRAM_NAME","Офисный менеджер");
        data.put("MENU_FILE","Файл");
        data.put("MENU_EDIT","Правка");
        data.put("MENU_VIEW","Вид");
        data.put("MENU_HELP","Помощь");

        data.put("JANUARY","Январь");
        data.put("FEBRUARY","Февраль");
        data.put("MARCH","Март");
        data.put("APRIL","Апрель");
        data.put("MAY","Май");
        data.put("JUNE","Июнь");
        data.put("JULY","Июль");
        data.put("AUGUST","Август");
        data.put("SEPTEMBER","Сентябрь");
        data.put("OCTOBER","Октябрь");
        data.put("NOVEMBER","Ноябрь");
        data.put("DECEMBER","Декабрь");

        data.put("ERROR_TITLE_EMPTY","Вы не ввели название!");
        data.put("ERROR_DATE_FORMAT","Неправильный формат даты!");

        data.put("ERROR_USER_NOT_FOUND","Пользователь не найден!");
        data.put("ERROR_STORE_NOT_FOUND","Филиал не найден");
        data.put("ERROR_PRODUCT_NOT_FOUND","Продукт не найден");
        data.put("ERROR_ORDER_NOT_FOUND","Заказ не найден");
        data.put("ERROR_CATEGORY_NOT_FOUND","Категория не найдена");
        data.put("ERROR_CONSUMPTION_NOT_FOUND","Расход не найден");
        data.put("ERROR_STOCK_NOT_FOUND","Склад не найден");



        data.put("ERROR_USER_EXISTS","Пользователь с таким логином уже существует!");

        data.put("ERROR_NAME_EMPTY","Поле имени не может быть пустым!");
        data.put("ERROR_PASSWORD_EMPTY","Пароль не должен быть пустым!");
        data.put("ERROR_USERNAME_EMPTY","Имя пользователя не может быть пустым!");
        data.put("ERROR_AGE_MUST_BE_BETWEEN_0_100","Возраст должен быть в диапазоне 0-100!");
        data.put("ERROR_FOREIGN_BRANCH","Вы не можете призводить манипуляции над данным филиалом!");
        data.put("ERROR_FOREIGN_ACCOUNT","Вы не можете производить манипуляции над чужим аккаунтом!");

        data.put("ERROR_INVALID_NAME","В имени есть недопустимые символы!");
        data.put("ERROR_INVALID_AGE","Возраст введен неверно!");
        data.put("ERROR_INVALID_EMAIL","Возраст введен неверно!");
        data.put("ERROR_INVALID_TIME_FORMAT","Время введено неверно");
        data.put("ERROR_INVALID_PHONE_NUMBER","Номер введен неправильно");
        data.put("ERROR_INVALID_PASSWORD","Неверно введен пароль");
        data.put("ERROR_INVALID_PHOTO","Проблема с обработкой фотографии");

        data.put("ERROR_PASSWORD_DONT_MATCH","Новый пароль и подтверждение не совпадают");

        data.put("ERROR_TARGET_USER_NOT_EQUAL_REQUESTER","Вы не можете изменять данные другого пользователя");
        data.put("ERROR_LOW_AUTHORITIES","У вас недостаточно прав для создания или изменения пользователя с такой ролью");

        data.put("YES","Да");
        data.put("NO","Нет");
    }
}

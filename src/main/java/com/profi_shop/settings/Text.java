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
        data.put("REQUEST","Запрос");
        data.put("PAID","Оплачено");
        data.put("DELIVERING","В пути");
        data.put("FINISHED","Завершен");


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
        data.put("MESSAGE_PASSWORD_RESET","Ваш пароль на сайте SITE_NAME, был обновлен на - ( NEW_PASSWORD ). Вы можете изменить пароль в личном кабинете.");
        data.put("MESSAGE_ORDER_NOTIFICATION","Поступил новый заказ, номер заказа - ORDER_NUMBER.");
        data.put("MESSAGE_REDIRECT_ORDER_NOTIFICATION","Новый перенаправленный заказ номер заказа - ORDER_NUMBER. Перенаправлен пользователем - SENDER");
        data.put("MESSAGE_CHANGING_STATUS_OF_CANCELED_ORDER","Нельзя изменить статус отмененного заказа!");
        data.put("MESSAGE_NEW_TRANSACTION","Новый перевод с филиала - SENDER , в филиал - TARGET в сумме ( AMOUNT ). Чтобы он вступил в силу вам нужно его одобрить. Чтобы одобрить перейдите в раздел ТРАНЗАКЦИИ");

        data.put("ERROR_TITLE_EMPTY","Вы не ввели название!");
        data.put("ERROR_DATE_FORMAT","Неправильный формат даты!");

        data.put("ERROR_USER_NOT_FOUND","Пользователь не найден!");
        data.put("ERROR_STORE_NOT_FOUND","Филиал не найден!");
        data.put("ERROR_PRODUCT_NOT_FOUND","Продукт не найден!");
        data.put("ERROR_ORDER_NOT_FOUND","Заказ не найден!");
        data.put("ERROR_CATEGORY_NOT_FOUND","Категория не найдена!");
        data.put("ERROR_CONSUMPTION_NOT_FOUND","Расход не найден!");
        data.put("ERROR_STOCK_NOT_FOUND","Склад не найден!");
        data.put("ERROR_SHIPMENT_NOT_FOUND","Доставка не найдена!");
        data.put("ERROR_COUPON_TEMPLATE_NOT_FOUND","Шаблон купона не найден!");
        data.put("ERROR_WISHLIST_NOT_FOUND","Избранное не найдено!");
        data.put("ERROR_PRODUCT_VARIATION_NOT_FOUND","Такого размера товара нет!");
        data.put("ERROR_PRODUCT_GROUP_NOT_FOUND","Карточка не найдена!");
        data.put("ERROR_NOTIFICATION_NOT_FOUND","Уведомление не найдено!");
        data.put("ERROR_ORDER_ITEM_NOT_FOUND","Элемент заказа не найден!");
        data.put("ERROR_TRANSACTION_NOT_FOUND","Транзакция не найдена!");
        data.put("ERROR_STORE_HOUSE_NOT_FOUND","Склад не найден!");
        data.put("ERROR_COUPON_NOT_FOUND","Купон не найден!");
        data.put("ERROR_FAQ_NOT_FOUND","Часто задаваемый вопрос не найден!");


        data.put("ERROR_COUPON_ALREADY_USED","Купон ранее был использован и недоступен для повторного использования!");
        data.put("ERROR_COUPON_EXPIRED","Срок годности купона истек!");
        data.put("ERROR_COUPON_NOT_APPLICABLE_TO_DISCOUNTED_PRODUCTS","Скидка купона не распространяетс на товары, которые имеют акционные скидки! Используйте купон если есть хотя бы один товар без начальной скидки");

        data.put("USER_NOT_AUTHENTICATED","Пользователь не аутентифицирован");


        data.put("ERROR_USER_EXISTS","Пользователь с таким логином уже существует!");
        data.put("ERROR_SHIPMENT_EXISTS","Доставка уже существует!");
        data.put("ERROR_CART_ITEM_EXIST","Продукт уже есть в корзине!");
        data.put("ERROR_SIZE_OF_PRODUCT_EXIST","Вариант продукта с таким размером уже существует!");
        data.put("ERROR_PRODUCT_SKU_EXIST","Продукт с таким штрихкодом уже существует!");
        data.put("ERROR_SPECIFICATION_EXISTS","Спецификация уже существует!");
        data.put("ERROR_ORDER_ITEM_EXISTS","Такой вариант товара уже существует в заказе!");
        data.put("ERROR_WISHLIST_CONTAINS_PRODUCT","Продукт уже в избранных!");
        data.put("ERROR_PRODUCT_IS_ALREADY_IN_STOCK","В списке присутствует продукт который уже участвует в другой акции!");
        data.put("ERROR_PRODUCT_IS_ALREADY_IN_GROUP","Товар уже состоит в этой группе!");

        data.put("ERROR_NAME_EMPTY","Поле имени не может быть пустым!");
        data.put("ERROR_PASSWORD_EMPTY","Пароль не должен быть пустым!");
        data.put("ERROR_USERNAME_EMPTY","Имя пользователя не может быть пустым!");
        data.put("ERROR_AGE_MUST_BE_BETWEEN_0_100","Возраст должен быть в диапазоне 0-100!");
        data.put("ERROR_FOREIGN_BRANCH","Вы не можете производить манипуляции над данным филиалом!");
        data.put("ERROR_FOREIGN_ACCOUNT","Вы не можете производить манипуляции над чужим аккаунтом!");
        data.put("ERROR_ONLY_ADMIN_OF_SHOP","Только администратор конкретного филиала может создавать заказы!");
        data.put("ERROR_FOREIGN_TRANSACTION","Вы не являетесь администратором данного филиала, и не можете изменять его транзакции!");
        data.put("ERROR_ONLY_SUPER_ADMIN","У вас недостаточно прав для изменения. Действие только для владельца магазина!");

        data.put("ERROR_INVALID_NAME","В имени есть недопустимые символы!");
        data.put("ERROR_INVALID_QUANTITY","Количество должно быть строго больше 0!");
        data.put("ERROR_INVALID_AGE","Возраст введен неверно!");
        data.put("ERROR_INVALID_EMAIL","Возраст введен неверно!");
        data.put("ERROR_INVALID_TIME_FORMAT","Время введено неверно");
        data.put("ERROR_INVALID_PHONE_NUMBER","Номер введен неправильно");
        data.put("ERROR_INVALID_PASSWORD","Неверно введен пароль");
        data.put("ERROR_INVALID_PHOTO","Проблема с обработкой фотографии");
        data.put("ERROR_EMPTY_CART","Корзина пуста. Для заказа пополните корзину");

        data.put("ERROR_PASSWORD_DONT_MATCH","Новый пароль и подтверждение не совпадают");

        data.put("ERROR_TARGET_USER_NOT_EQUAL_REQUESTER","Вы не можете изменять данные другого пользователя");
        data.put("ERROR_LOW_AUTHORITIES","У вас недостаточно изменения пользователя с такой ролью");

        data.put("YES","Да");
        data.put("NO","Нет");
    }
}

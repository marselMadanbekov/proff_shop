package com.profi_shop.exceptions;

public class NotEnoughException extends Exception{
    private String message;
    public NotEnoughException(String product, int availableCount){
        String NOT_ENOUGH_EXCEPTION = "Продукта PRODUCT_NAME недостаточно для вашего заказа. Количество на складе = COUNT_ON_STORE";
        message = NOT_ENOUGH_EXCEPTION.replace("PRODUCT_NAME",product);
        message = message.replace("COUNT_ON_STORE", availableCount + "");
    }

    public String getMessage(){
        return message;
    }
}

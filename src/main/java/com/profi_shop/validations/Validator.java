package com.profi_shop.validations;


import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.settings.Templates;
import org.springframework.stereotype.Service;

@Service
public class Validator {
    public static String validNumber(String number) throws InvalidDataException {
        if(number.length() == 0)  return null;
        number = number.replaceAll(" ", "");
        for (String prefix : Templates.numberPrefixes) {
            if (number.startsWith(prefix)) {
                number = number.substring(prefix.length());
                break;
            }
        }
        if (number.length() != Templates.numberBody.replaceAll(" ","").length()) throw new InvalidDataException(InvalidDataException.INVALID_PHONE_NUMBER);
        StringBuilder validN = new StringBuilder(Templates.numberPrefixes.get(1) + " ");
        int i = 0;
        for (char c : Templates.numberBody.toCharArray()) {
            if(!Character.isDigit(number.charAt(i)))    throw new InvalidDataException(InvalidDataException.INVALID_PHONE_NUMBER);
            validN.append((Character.isLetter(c)) ? number.charAt(i++) : ' ');
        }
        return validN.toString();
    }

    public static String validTime(String time) {
        if(!time.contains(":")) return null;
        String[] times = time.split(":");
        StringBuilder validT = new StringBuilder();
        try{
            int hours = Integer.parseInt(times[0]);
            validT.append((hours < 10)? "0" + hours + ":" : hours + ":");
        }catch (NumberFormatException e){
            return null;
        }
        try{
            int minutes = Integer.parseInt(times[1]);
            validT.append((minutes < 10)? "0" + minutes: minutes);
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            validT.append("00");
        }
        return validT.toString();
    }

    public static String validFirstname(String name) throws InvalidDataException {
        name = name.trim();
        if(name.length() == 0)  throw new InvalidDataException(InvalidDataException.EMPTY_NAME);
        for(char c : name.toCharArray())
            if(!Character.isLetter(c))  throw new InvalidDataException(InvalidDataException.INVALID_NAME);
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static String validLastname(String lastname) throws InvalidDataException {
        lastname = lastname.trim();
        if(lastname.length() == 0)  throw new InvalidDataException(InvalidDataException.EMPTY_NAME);
        for(char c : lastname.toCharArray())
            if(!Character.isLetter(c) && c != ' ') throw new InvalidDataException(InvalidDataException.INVALID_NAME);
        return lastname.substring(0, 1).toUpperCase() + lastname.substring(1).toLowerCase();
    }
}

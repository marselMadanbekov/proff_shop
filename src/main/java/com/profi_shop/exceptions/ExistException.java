package com.example.academia1_1.domain.exceptions;


import com.example.academia1_1.infrastructure.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistException extends RuntimeException{
    public static final int USER_EXISTS = 1;
    public static final int SUBJECT_EXISTS = 2;
    public static final int GROUP_EXISTS = 3;
    public static final int ADDITIONAL_EXISTS = 4;
    public static final int TIMETABLE_EXISTS = 5;

    public static final int GROUP_CONTAINS_USER = 6;


    private final int code;


    public ExistException(int code) {
        this.code = code;
    }

    public String getMessage(){
        switch (code){
            case 1:
                return Text.get("ERROR_USER_EXISTS");
            case 2:
                return Text.get("ERROR_SUBJECT_EXISTS");
            case 3:
                return Text.get("ERROR_GROUP_EXISTS");
            case 4:
                return Text.get("ERROR_ADDITIONAL_EXISTS");
            case 5:
                return Text.get("ERROR_TIMETABLE_EXISTS");
            case 6:
                return Text.get("ERROR_GROUP_CONTAINS_USER");
        }
        return "";
    }
}

package com.example.academia1_1.domain.exceptions;


import com.example.academia1_1.infrastructure.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SearchException extends RuntimeException{
    public static final int USER_NOT_FOUND = 1;
    public static final int SUBJECT_NOT_FOUND = 2;
    public static final int GROUP_NOT_FOUND = 3;
    public static final int BRANCH_NOT_FOUND = 4;
    public static final int TIMETABLE_NOT_FOUND = 5;

    public static final int TASK_NOT_FOUND = 6;

    public static final int LESSON_NOT_FOUND = 7;

    private final int code;


    public SearchException(int code) {
        this.code = code;
    }


    public String getMessage(){
        switch (code){
            case 1:
                return Text.get("ERROR_USER_NOT_FOUND");
            case 2:
                return Text.get("ERROR_SUBJECT_NOT_FOUND");
            case 3:
                return Text.get("ERROR_GROUP_NOT_FOUND");
            case 4:
                return Text.get("ERROR_BRANCH_NOT_FOUND");
            case 5:
                return Text.get("ERROR_TIMETABLE_NOT_FOUND");
            case 6:
                return Text.get("ERROR_TASK_NOT_FOUND");
            case 7:
                return Text.get("ERROR_LESSON_NOT_FOUND");
        }
        return "";
    }
}

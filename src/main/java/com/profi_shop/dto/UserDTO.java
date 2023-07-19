package com.profi_shop.dto;

import com.profi_shop.model.enums.Role;
import lombok.Data;

import java.sql.Date;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String phone_number;
    private String address;
    private Role role;
    private Date created_date;
}

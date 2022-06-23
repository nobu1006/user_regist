package com.example.user_regist.domain;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private String nameKana;
    private String email;
    private String zipCode;
    private String address;
    private String telephone;
    private String password;

}

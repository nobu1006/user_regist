package com.example.user_regist.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class RegistRequest {
    private Integer id;
    private String email;
    private String uniqueKey;
    private Date registDate;
    private Boolean delFlg;

    
}

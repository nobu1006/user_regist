package com.example.user_regist.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {
    // TODO: それ以外の入力チェックも盛り込め
    @NotBlank
    private String name;
    @NotBlank
    private String nameKana;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String address;
    @NotBlank
    private String telephone;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
}

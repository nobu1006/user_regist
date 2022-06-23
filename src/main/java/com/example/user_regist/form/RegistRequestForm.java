package com.example.user_regist.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistRequestForm {
    @NotBlank
    @Email
    private String email;
}

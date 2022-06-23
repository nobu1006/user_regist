package com.example.user_regist.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.user_regist.common.MailClient;
import com.example.user_regist.domain.RegistRequest;
import com.example.user_regist.form.RegistRequestForm;
import com.example.user_regist.service.RegistRequestService;

@Controller
@RequestMapping("/request")
public class RegistRequestController {

    @Autowired
    private RegistRequestService registRequestService;

    
    @GetMapping("")
    public String toRequest(RegistRequestForm registRequestForm) {
        return "/regist_request";
    }


    @PostMapping("/send")
    public String send(@Validated RegistRequestForm registRequestForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/regist_request";
        }
        String email = registRequestForm.getEmail();
        if (registRequestService.checkExistsByEmail(email)) {
            result.rejectValue("email", null, "既に受付済みです");
            return "/regist_request";
        }
        registRequestService.accept(email);
        return "redirect:/request/toFinish";
    }

    @GetMapping("/toFinish")
    public String toFinish() {
        return "/regist_request_finish";
    }



}

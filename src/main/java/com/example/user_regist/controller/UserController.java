package com.example.user_regist.controller;

import com.example.user_regist.domain.RegistRequest;
import com.example.user_regist.domain.User;
import com.example.user_regist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.user_regist.form.UserForm;
import com.example.user_regist.service.RegistRequestService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegistRequestService registRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/signUp")
    public String signUp(UserForm userForm, String key, Model model) {
        RegistRequest registRequest = registRequestService.checkExistsByUniqeKey(key);
        if (registRequest != null) {
            session.setAttribute("email", registRequest.getEmail());
            return "/regist_user";
        } else {
            return "/request_expired";
        }
    }

    @PostMapping("/regist")
    public String regist(@Validated UserForm userForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/regist_user";
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        user.setEmail((String) session.getAttribute("email"));
        userService.doRegist(user);
        return "redirect:/user/toFinish";

    }

    @GetMapping("/toFinish")
    public String toFinish() {
        return "/regist_user_finish";
    }

}

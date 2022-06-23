package com.example.user_regist.service;

import com.example.user_regist.domain.User;
import com.example.user_regist.repository.RegistRequestRepository;
import com.example.user_regist.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistRequestRepository registRequestRepository;

    public void doRegist(User user) {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        user.setPassword(bcpe.encode(user.getPassword()));
        userRepository.insert(user);
        int count = registRequestRepository.deleteByEmail(user.getEmail());
    }
}

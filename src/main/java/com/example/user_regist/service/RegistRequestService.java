package com.example.user_regist.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import com.example.user_regist.domain.User;
import com.example.user_regist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_regist.common.MailClient;
import com.example.user_regist.domain.RegistRequest;
import com.example.user_regist.repository.RegistRequestRepository;

@Service
@Transactional
public class RegistRequestService {

    @Autowired
    private RegistRequestRepository registRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailClient mailClient;

    public boolean checkExistsByEmail(String email) {
        RegistRequest registRequest = registRequestRepository.findByEmail(email);
        if (registRequest != null && within24Hours(registRequest.getRegistDate())) {
            return true;
        }
        return false;
    }

    public RegistRequest checkExistsByUniqeKey(String uniqeKey) {
        RegistRequest registRequest = registRequestRepository.findByUniquKey(uniqeKey);
        if (registRequest != null && within24Hours(registRequest.getRegistDate())) {
            return registRequest;
        }
        return null;
    }

    public void accept(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            String uniqueKey =  UUID.randomUUID().toString();
            RegistRequest registRequest = new RegistRequest();
            registRequest.setEmail(email);
            registRequest.setUniqueKey(uniqueKey);
            registRequestRepository.insert(registRequest);
            mailClient.sendRegistUrl(email, uniqueKey);
        }
    }

    private boolean within24Hours(Date registDate) {
        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        return (registDate.getTime() + 24 * 60 * 60 * 1000) >= (now.getTime());
    }
    
}

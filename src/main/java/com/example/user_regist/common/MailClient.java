package com.example.user_regist.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailClient {

    private static final String MAIL_BODY = """
        新規ユーザー登録依頼を受け付けました。
        以下のURLから本登録処理を行ってください。
        
        Hogehogeシステム、ユーザー登録用URL
        http://localhost:8080/user/signUp?key=##UNIQUE_KEY##
        
        ※上記URLの有効期限は24時間以内です
        """;

    @Autowired
    private MailSender mailSender;

    public void sendRegistUrl(String email, String uniqueKey) throws MailException {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("hogehoge-admin@example.com");
        msg.setTo(email);
//        msg.setCc(); //Cc用
//        msg.setBcc(); //Bcc用
        msg.setSubject("【Hogehogeシステム】ユーザー登録用URL送付");
        msg.setText(MAIL_BODY.replace("##UNIQUE_KEY##", uniqueKey));
        mailSender.send(msg);
    }
    
}

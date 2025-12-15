package com.shop.cosmetics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.beans.JavaBean;

@Service
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;

    public void sendWelcomeMessage(String email, String username){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to cosmetics platform");
        message.setText("" +
                "Hi " + username + ",\n\n" + "" +
                "Your account has registered successfully.\n" +"" +
                "Have a good day!"
        );
        javaMailSender.send(message);
    }
}

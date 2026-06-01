package com.example.WorkforceIQ.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmployeeCredentials(String toEmail, String employeeName, String password) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Your WorkforceIQ Account");

        message.setText(
                "Hello " + employeeName + ",\n\n" +
                "Your WorkforceIQ account has been created.\n\n" +
                "Password: " + password + "\n\n" 
        );

        mailSender.send(message);
    }
}
package com.profi_shop.services.email;


import com.profi_shop.settings.Templates;
import com.profi_shop.settings.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

    private final JavaMailSender emailSender;
    private final String subject = "PROFF-SHOP";


    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String targetUser, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(targetUser);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendPasswordResetMail(String targetUser, String password){
        System.out.println("reset message sending to " + targetUser);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(targetUser);
        message.setSubject(subject);
        message.setText(passwordResetMail(password));
        emailSender.send(message);
    }


    private String passwordResetMail(String password){
        String content = Text.get("MESSAGE_PASSWORD_RESET").replace(Templates.SITE_NAME_FILLER,subject);
        content = content.replace(Templates.PASSWORD_FILLER,password);
        return content;
    }

}

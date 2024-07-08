package com.argus.dossier.service;

import com.argus.dossier.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * MailSenderService
 *
 * @author Maxim Chistyakov
 */
@Service
@RequiredArgsConstructor
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    public void send(EmailMessage emailMessage, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailMessage.getAddress());
        mailMessage.setSubject(emailMessage.getTheme().toString());
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }
}

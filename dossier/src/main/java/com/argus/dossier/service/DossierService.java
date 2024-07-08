package com.argus.dossier.service;

import com.argus.dossier.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * DossierService
 *
 * @author Maxim Chistyakov
 */
@Service
@RequiredArgsConstructor
public class DossierService {

    private final MailSenderService mailSenderService;

    @KafkaListener(topics = "finish-registration", groupId = "group")
    public void consumeFinishRegistration(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Finish your registration");
    }

    @KafkaListener(topics = "create-documents", groupId = "group")
    public void consumeCreateDocument(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Prepare documents");
    }

    @KafkaListener(topics = "send-documents", groupId = "group")
    public void consumeSendDocument(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Your documents");
    }

    @KafkaListener(topics = "send-ses", groupId = "group")
    public void consumeSendSes(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Sign documents");
    }

    @KafkaListener(topics = "credit-issued", groupId = "group")
    public void consumeCreditIssued(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Congratulations with your new credit");
    }

    @KafkaListener(topics = "statement-denied", groupId = "group")
    public void consumeStatementDenied(EmailMessage emailMessage) {
        mailSenderService.send(emailMessage, "Credit denied");
    }
}

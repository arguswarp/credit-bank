package com.argus.deal.service;

import com.argus.deal.dto.EmailMessage;
import com.argus.deal.model.enums.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * EmailProducer
 *
 * @author Maxim Chistyakov
 */
@Service
@RequiredArgsConstructor
public class EmailProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void send(UUID id, String email, Theme theme) {
        EmailMessage emailMessage = EmailMessage.builder()
                .theme(theme)
                .statementID(id)
                .address(email)
                .build();
        kafkaTemplate.send(theme.getTopic(), emailMessage);
    }
}

package com.argus.deal.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * RestTemplateConfiguration.
 *
 * @author Maxim Chistyakov
 */
@Configuration
@Slf4j
public class RestTemplateConfiguration {

    @Value("${deal.rest-client.root-uri}")
    private String rootUri;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        log.info("RestTemplate initialization with root URI: {}", rootUri);
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(rootUri))
                .build();
    }
}

package com.payment.service;

import com.payment.entity.WebhookEvent;
import com.payment.repository.WebhookEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WebhookService {

    private final WebhookEventRepository repository;
    private final RestTemplate restTemplate;

    public WebhookService(WebhookEventRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public void sendWebhook(WebhookEvent event) {
        try {
            String webhookUrl = "https://webhook.site/test-url"; // dummy

            restTemplate.postForObject(webhookUrl, event.getPayload(), String.class);

            event.setStatus("SENT");

        } catch (Exception e) {
            event.setRetryCount(event.getRetryCount() + 1);
            event.setStatus("FAILED");
        }

        repository.save(event);
    }

    @Scheduled(fixedDelay = 10000) // every 10 seconds
    public void retryFailedWebhooks() {

        List<WebhookEvent> failedEvents = repository.findAll()
                .stream()
                .filter(event -> "FAILED".equals(event.getStatus()) && event.getRetryCount() < 3)
                .toList();

        for (WebhookEvent event : failedEvents) {
            sendWebhook(event);
        }
    }

}


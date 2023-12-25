package com.kaluzny.demo.service;

import com.kaluzny.demo.domain.Automobile;
import com.kaluzny.demo.domain.AutomobileRepository;
import jakarta.jms.JMSException;
import jakarta.jms.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AutomobileServiceImpl implements AutomobileService {

    private final AutomobileRepository automobileRepository;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public AutomobileServiceImpl(AutomobileRepository automobileRepository, JmsTemplate jmsTemplate) {
        this.automobileRepository = automobileRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Automobile saveAndSendMessage(Automobile automobile) {
        try {
            Topic autoTopic = Objects.requireNonNull(jmsTemplate
                    .getConnectionFactory()).createConnection().createSession().createTopic("AutoTopic");
            Automobile savedAutomobile = automobileRepository.save(automobile);
            log.info("\u001B[32m" + "Sending Automobile with id: " + savedAutomobile.getId() + "\u001B[0m");
            jmsTemplate.convertAndSend(autoTopic, savedAutomobile);
            return savedAutomobile;
        } catch (Exception exception) {
            throw new RuntimeException("Failed to save and send message", exception);
        }
    }

    @Override
    public List<String> getCarByColor(String color) {
        try {
            Topic autoColorTopic = Objects.requireNonNull(jmsTemplate
                    .getConnectionFactory()).createConnection().createSession().createTopic("AutoColorTopic");
            List<Automobile> automobileList = automobileRepository.findByColor(color);
//            log.info("\u001B[32m" + "Sending Automobile with id: " + savedAutomobile.getId() + "\u001B[0m");
            jmsTemplate.convertAndSend(autoColorTopic, automobileList);
            List<String> collectionByColor = automobileList.stream()
                    .map(Automobile::getName)
                    .sorted()
                    .collect(Collectors.toList());

            return collectionByColor;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

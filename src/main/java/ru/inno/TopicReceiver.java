package ru.inno;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(name = "TopicReceiver", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topic/INNOTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class TopicReceiver implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(TopicReceiver.class);

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Message : {}", message.getBody(String.class));
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
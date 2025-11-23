package com.infrastructure.order.broker;

import com.application.order.create.CreateOrderCommand;
import com.infrastructure.config.rabbitmq.AMQPConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class MessageBrokerConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageBrokerConsumer.class);

    @KafkaListener(topics = "order-consumer-os", containerFactory = "kafkaListenerContainerFactory")

    public void kafkaListerner(@Payload CreateOrderCommand command){
        logger.info("Canal recebido: {}", command.channel());
    }
}

package com.infrastructure.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class AMQPConfiguration implements RabbitListenerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AMQPConfiguration.class);
    private final ConnectionFactory connectionFactory;
    
    public AMQPConfiguration(ConnectionFactory connectionFactory) {
        logger.info("==========================================");
        logger.info("AMQPConfiguration sendo instanciada!");
        logger.info("ConnectionFactory recebido: {}", connectionFactory != null ? connectionFactory.getClass().getName() : "NULL");
        logger.info("==========================================");
        this.connectionFactory = connectionFactory;
    }
    
    @Bean
    public Queue createQueue(){
        logger.info("Criando Queue: order-events");
        return new Queue("order-events", true);
    }
    
    @Bean
    public DirectExchange directExchange(){
        logger.info("Criando Exchange: order-exchange");
        return new DirectExchange("order-exchange");
    }

    @Bean
    public Binding bindingQueue(Queue queue, DirectExchange directExchange){
        logger.info("Criando Binding: order-events -> order-exchange");
        return BindingBuilder.bind(queue).to(directExchange).with("order-events");
    }
    
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        logger.info("Criando RabbitAdmin...");
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
    
    @PostConstruct
    public void init() {
        try {
            logger.info("==========================================");
            logger.info("Inicializando RabbitMQ Configuration...");
            logger.info("ConnectionFactory: {}", connectionFactory.getClass().getName());
            logger.info("Criando RabbitAdmin...");
            RabbitAdmin admin = new RabbitAdmin(connectionFactory);
            admin.setAutoStartup(true);
            logger.info("Inicializando filas e exchanges...");
            admin.initialize();
            logger.info("✅ RabbitMQ conectado e inicializado com sucesso!");
            logger.info("==========================================");
        } catch (Exception e) {
            logger.error("❌ Erro ao conectar com RabbitMQ no PostConstruct: ", e);
        }
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return  rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationListener(RabbitAdmin rabbitAdmin){
        return event -> {
            try {
                logger.info("==========================================");
                logger.info("ApplicationReadyEvent - Inicializando RabbitMQ...");
                logger.info("Criando filas e exchanges...");
                rabbitAdmin.initialize();
                logger.info("✅ RabbitMQ inicializado com sucesso!");
                logger.info("   - Queue: order-events");
                logger.info("   - Exchange: order-exchange");
                logger.info("   - Binding: order-events -> order-exchange");
                logger.info("==========================================");
            } catch (Exception e) {
                logger.error("❌ Erro ao inicializar RabbitMQ no ApplicationReadyEvent: ", e);
            }
        };
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setMessageConverter(messageConverter());
        return  factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setContainerFactory(rabbitListenerContainerFactory(connectionFactory));
    }
}

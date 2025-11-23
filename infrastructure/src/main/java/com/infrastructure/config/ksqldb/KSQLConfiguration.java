package com.infrastructure.config.ksqldb;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KSQLConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(KSQLConfiguration.class);

    @Value("${ksqldb.server:http://localhost:8088}")
    private String ksqldbServer;

    @Bean
    public Client ksqlClient() {
        try {
            logger.info("Configurando KSQLDB Client para: {}", ksqldbServer);
            
            // Extrair host e porta da URL
            String url = ksqldbServer.replace("http://", "").replace("https://", "");
            String[] parts = url.split(":");
            String host = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 8088;
            
            logger.info("Conectando ao KSQLDB: {}:{}", host, port);
            
            ClientOptions options = ClientOptions.create()
                    .setHost(host)
                    .setPort(port);
            
            Client client = Client.create(options);
            logger.info("✅ KSQLDB Client criado com sucesso!");
            return client;
        } catch (Exception e) {
            logger.error("❌ Erro ao criar KSQLDB Client: ", e);
            throw new RuntimeException("Falha ao criar KSQLDB Client", e);
        }
    }
}


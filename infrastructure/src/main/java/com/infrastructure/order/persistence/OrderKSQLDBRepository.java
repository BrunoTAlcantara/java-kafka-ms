package com.infrastructure.order.persistence;

import com.domain.order.Order;
import com.domain.order.OrderID;
import com.infrastructure.order.OrderEntity;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.infrastructure.utils.InfraStructureUtils.convert;

@Repository
public class OrderKSQLDBRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OrderKSQLDBRepository.class);
    private final Client ksqlClient;

    public OrderKSQLDBRepository(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
        LOG.info("OrderKSQLDBRepository inicializado com KSQLDB Client");
    }

    public Page<OrderEntity> findAll(final Specification<OrderEntity> specification, 
                                     final Pageable pageable, 
                                     final String parameter) {
        List<OrderEntity> ordersList = new ArrayList<>();
        
        try {
            String SQL = "SELECT * FROM oms_table";
            
            // Adicionar WHERE se houver specification
            if (specification != null && parameter != null && !parameter.isBlank()) {
                SQL += " WHERE channel " + convert(parameter);
            }
            
            // Adicionar LIMIT e OFFSET para paginação
            int limit = pageable.getPageSize();
            int offset = (int) pageable.getOffset();
            SQL += String.format(" LIMIT %d OFFSET %d;", limit, offset);
            
            LOG.info("Executando query KSQLDB: {}", SQL);
            
            // Executar query - KSQLDB Client usa StreamedQueryResult
            List<Row> rows = new ArrayList<>();
            CompletableFuture<Void> queryFuture = ksqlClient.streamQuery(SQL)
                    .thenAccept(streamedQueryResult -> {
                        try {
                            // Ler todas as rows do stream
                            while (true) {
                                CompletableFuture<Row> rowFuture = streamedQueryResult.poll();
                                if (rowFuture == null) {
                                    break;
                                }
                                try {
                                    Row row = rowFuture.get(5, TimeUnit.SECONDS);
                                    rows.add(row);
                                } catch (TimeoutException e) {
                                    LOG.warn("Timeout ao ler row, continuando...");
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Erro ao ler rows do stream: {}", e.getMessage());
                        }
                    });
            
            // Aguardar conclusão da query (timeout de 30 segundos)
            queryFuture.get(30, TimeUnit.SECONDS);
            
            // Converter rows para OrderEntity
            for (Row row : rows) {
                try {
                    Order order = mapRowForOrdersList(row);
                    ordersList.add(OrderEntity.from(order));
                } catch (Exception e) {
                    LOG.warn("Erro ao converter row para OrderEntity: {}", e.getMessage());
                }
            }
            
            LOG.info("Select Orders By K-SQL - {} registros encontrados", ordersList.size());
            
            // Para KSQLDB, não temos contagem total fácil, então usamos o tamanho da lista
            // Em produção, você pode executar uma query COUNT separada
            return new PageImpl<>(ordersList, pageable, ordersList.size());
            
        } catch (Exception cause) {
            LOG.error("Failed to Execute Query K-SQL: {}", cause.getMessage(), cause);
            return Page.empty();
        }
    }

    private Order mapRowForOrdersList(Row row) {
        String orderId = row.getString("ORDERID");
        BigDecimal orderValue = row.getDecimal("ORDERVALUE");
        String channel = row.getString("CHANNEL");
        String paymentStatus = row.getString("PAYMENTSTATUS");
        
        // Tentar obter timestamps, se não existirem usar Instant.now()
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();
        
        try {
            // KSQLDB pode retornar timestamps como String ou Long
            Object createdObj = row.getValue("CREATEDAT");
            Object updatedObj = row.getValue("UPDATEAT");
            
            if (createdObj != null) {
                if (createdObj instanceof String) {
                    createdAt = Instant.parse((String) createdObj);
                } else if (createdObj instanceof Long) {
                    createdAt = Instant.ofEpochMilli((Long) createdObj);
                }
            }
            
            if (updatedObj != null) {
                if (updatedObj instanceof String) {
                    updatedAt = Instant.parse((String) updatedObj);
                } else if (updatedObj instanceof Long) {
                    updatedAt = Instant.ofEpochMilli((Long) updatedObj);
                }
            }
        } catch (Exception e) {
            LOG.warn("Erro ao converter timestamps, usando Instant.now(): {}", e.getMessage());
        }
        
        return Order.aggregate(
            OrderID.from(orderId),
            orderValue,
            channel,
            paymentStatus,
            createdAt,
            updatedAt
        );
    }
}


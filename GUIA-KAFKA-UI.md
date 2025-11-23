# Guia de Configuração do Kafka UI

## 📋 Configuração do KSQLDB no Kafka UI

### Passo a Passo:

1. **Acesse o Kafka UI**
   - URL: http://localhost:8080

2. **Vá para a seção de KSQLDB**
   - No menu lateral, procure por **"KSQLDB"** ou **"Query"**
   - Ou vá em **Settings** → **KSQLDB**

3. **Adicione a conexão do KSQLDB**

   **URL do KSQLDB Server:**
   ```
   http://localhost:8088
   ```

   **Ou se estiver dentro do Docker:**
   ```
   http://ksqldb-server:8088
   ```

### 🔧 Configuração Completa:

| Campo | Valor |
|-------|-------|
| **KSQLDB Server URL** | `http://localhost:8088` |
| **Bootstrap Servers** | `localhost:29092` |
| **KSQLDB Cluster ID** | (deixe vazio ou use `ksql-service`) |

### 📝 Configuração do Cluster Kafka (se necessário):

No Kafka UI, você também precisa configurar o cluster Kafka:

| Campo | Valor |
|-------|-------|
| **Bootstrap Servers** | `localhost:29092` |
| **Zookeeper** | `localhost:2181` |

### ✅ Verificar se está funcionando:

1. Acesse: http://localhost:8080
2. Vá em **KSQLDB** ou **Query**
3. Você deve ver a interface de queries do KSQLDB
4. Teste uma query simples:
   ```sql
   SHOW STREAMS;
   ```

### 🔍 URLs dos Serviços:

- **Kafka UI**: http://localhost:8080
- **KSQLDB Server**: http://localhost:8088
- **Kafka Broker**: localhost:29092
- **Zookeeper**: localhost:2181

### ⚠️ Troubleshooting:

Se não conseguir conectar:

1. **Verifique se o KSQLDB está rodando:**
   ```bash
   sudo docker-compose ps ksqldb-server
   ```

2. **Verifique os logs:**
   ```bash
   sudo docker-compose logs ksqldb-server
   ```

3. **Teste a URL diretamente:**
   ```bash
   curl http://localhost:8088/info
   ```

4. **Reinicie o KSQLDB:**
   ```bash
   sudo docker-compose restart ksqldb-server
   ```


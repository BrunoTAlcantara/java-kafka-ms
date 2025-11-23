# Guia de Inicialização dos Serviços Docker

## ⚠️ Problema de Permissão do Docker

Se você receber erro de permissão ao executar comandos Docker, execute:

```bash
sudo usermod -aG docker $USER
```

Depois faça **logout e login novamente** (ou reinicie o terminal).

## 🚀 Iniciar Todos os Serviços

### Opção 1: Script Automático (Recomendado)
```bash
./start-services.sh
```

### Opção 2: Comando Manual
```bash
docker-compose up -d
```

### Opção 3: Iniciar Apenas o MySQL
```bash
docker-compose up -d mysql
```

## 📋 Verificar Status dos Serviços

```bash
docker-compose ps
```

## 📊 Ver Logs

```bash
# Todos os serviços
docker-compose logs -f

# Apenas MySQL
docker-compose logs -f mysql

# Apenas Kafka
docker-compose logs -f broker
```

## 🛑 Parar Serviços

```bash
# Parar todos
docker-compose down

# Parar apenas MySQL
docker-compose stop mysql
```

## 🔍 Verificar se MySQL está Acessível

```bash
# Testar conexão na porta
nc -zv localhost 3308

# Ou usar telnet
telnet localhost 3308
```

## ⏱️ Aguardar MySQL Inicializar

Após iniciar o MySQL, **aguarde 10-15 segundos** antes de rodar a aplicação Spring Boot. O MySQL precisa de tempo para inicializar completamente.

## 🔧 Troubleshooting

### MySQL não conecta

1. Verifique se o container está rodando:
   ```bash
   docker-compose ps mysql
   ```

2. Verifique os logs:
   ```bash
   docker-compose logs mysql
   ```

3. Reinicie o MySQL:
   ```bash
   docker-compose restart mysql
   ```

### Porta já em uso

Se a porta 3308 já estiver em uso, você pode:
- Parar o serviço que está usando a porta
- Ou alterar a porta no `docker-compose.yaml` (linha 16)

## 📝 Serviços Disponíveis

| Serviço | Porta | URL/Acesso |
|---------|-------|------------|
| MySQL | 3308 | `localhost:3308` |
| Kafka | 29092 | `localhost:29092` |
| Zookeeper | 2181 | `localhost:2181` |
| KSQLDB | 8088 | `http://localhost:8088` |
| RabbitMQ | 15672 | `http://localhost:15672` |
| Kafka UI | 8080 | `http://localhost:8080` |

## 🔐 Credenciais

- **MySQL**: 
  - Usuário: `admin`
  - Senha: `nimda`
  - Database: `oms`

- **RabbitMQ**:
  - Usuário: `admin`
  - Senha: `nimda`


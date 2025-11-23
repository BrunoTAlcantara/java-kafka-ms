.PHONY: help dependencies up down restart status logs build clean run test setup-ksqldb reset-ksqldb

# Variáveis
# Use 'docker-compose' (com hífen) - padrão na maioria das distribuições Linux
# Se tiver Docker Compose V2 e o comando não funcionar, altere para: DOCKER_COMPOSE := docker compose
DOCKER_COMPOSE := docker-compose
MAVEN := ./mvnw

# Cores para output (opcional)
CYAN := \033[0;36m
GREEN := \033[0;32m
YELLOW := \033[0;33m
NC := \033[0m # No Color

help: ## Mostra esta mensagem de ajuda
	@echo "$(CYAN)============================================$(NC)"
	@echo "$(CYAN)  Push Na Master - Comandos Disponíveis$(NC)"
	@echo "$(CYAN)============================================$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-15s$(NC) %s\n", $$1, $$2}'

dependencies: ## Sobe todos os serviços Docker (MySQL, Kafka, Zookeeper, KSQLDB, RabbitMQ, Kafka UI)
	@echo "$(CYAN)============================================$(NC)"
	@echo "$(CYAN)  Iniciando todas as dependências...$(NC)"
	@echo "$(CYAN)============================================$(NC)"
	@echo ""
	@echo "$(YELLOW)[1/6]$(NC) Iniciando MySQL..."
	@$(DOCKER_COMPOSE) up -d mysql
	@echo "Aguardando MySQL inicializar..."
	@sleep 5
	@echo "$(YELLOW)[2/6]$(NC) Iniciando Zookeeper..."
	@$(DOCKER_COMPOSE) up -d zookeeper
	@echo "Aguardando Zookeeper inicializar..."
	@sleep 3
	@echo "$(YELLOW)[3/6]$(NC) Iniciando Kafka Broker..."
	@$(DOCKER_COMPOSE) up -d broker
	@echo "Aguardando Kafka inicializar..."
	@sleep 5
	@echo "$(YELLOW)[4/6]$(NC) Iniciando KSQLDB Server..."
	@$(DOCKER_COMPOSE) up -d ksqldb-server
	@echo "Aguardando KSQLDB inicializar..."
	@sleep 10
	@echo "$(YELLOW)[5/6]$(NC) Iniciando RabbitMQ..."
	@$(DOCKER_COMPOSE) up -d rabbitmq
	@echo "$(YELLOW)[6/6]$(NC) Iniciando Kafka UI..."
	@$(DOCKER_COMPOSE) up -d kafka-ui
	@echo ""
	@echo "$(CYAN)Executando scripts SQL no KSQLDB...$(NC)"
	@make setup-ksqldb || echo "$(YELLOW)Aviso: Não foi possível executar SQL no KSQLDB. Execute manualmente com 'make setup-ksqldb'$(NC)"
	@echo ""
	@echo "$(GREEN)============================================$(NC)"
	@echo "$(GREEN)  Dependências iniciadas com sucesso!$(NC)"
	@echo "$(GREEN)============================================$(NC)"
	@echo ""
	@echo "$(CYAN)Serviços disponíveis:$(NC)"
	@echo "  MySQL:        localhost:3308 (admin/nimda)"
	@echo "  Kafka:        localhost:29092"
	@echo "  Zookeeper:    localhost:2181"
	@echo "  KSQLDB:       http://localhost:8088"
	@echo "  RabbitMQ:     http://localhost:15672 (admin/nimda)"
	@echo "  Kafka UI:     http://localhost:8080"
	@echo ""
	@echo "$(YELLOW)Use 'make status' para verificar o status dos serviços$(NC)"
	@echo "$(YELLOW)Use 'make logs' para ver os logs$(NC)"

up: ## Sobe todos os serviços Docker de uma vez
	@$(DOCKER_COMPOSE) up -d
	@make status

down: ## Para todos os serviços Docker
	@echo "$(YELLOW)Parando todos os serviços...$(NC)"
	@$(DOCKER_COMPOSE) down
	@echo "$(GREEN)Serviços parados!$(NC)"

restart: ## Reinicia todos os serviços Docker
	@echo "$(YELLOW)Reiniciando serviços...$(NC)"
	@$(DOCKER_COMPOSE) restart
	@make status

status: ## Mostra o status dos serviços Docker
	@echo "$(CYAN)============================================$(NC)"
	@echo "$(CYAN)  Status dos Serviços$(NC)"
	@echo "$(CYAN)============================================$(NC)"
	@$(DOCKER_COMPOSE) ps

logs: ## Mostra os logs de todos os serviços (use Ctrl+C para sair)
	@$(DOCKER_COMPOSE) logs -f

logs-mysql: ## Mostra os logs do MySQL
	@$(DOCKER_COMPOSE) logs -f mysql

logs-kafka: ## Mostra os logs do Kafka
	@$(DOCKER_COMPOSE) logs -f broker

logs-ksqldb: ## Mostra os logs do KSQLDB
	@$(DOCKER_COMPOSE) logs -f ksqldb-server

build: ## Compila o projeto com Maven
	@echo "$(CYAN)Compilando o projeto...$(NC)"
	@$(MAVEN) clean install -DskipTests
	@echo "$(GREEN)Build concluído!$(NC)"

build-with-tests: ## Compila o projeto com Maven incluindo testes
	@echo "$(CYAN)Compilando o projeto com testes...$(NC)"
	@$(MAVEN) clean install
	@echo "$(GREEN)Build concluído!$(NC)"

clean: ## Limpa os arquivos compilados (target/)
	@echo "$(YELLOW)Limpando arquivos compilados...$(NC)"
	@$(MAVEN) clean
	@echo "$(GREEN)Limpeza concluída!$(NC)"

run: ## Executa a aplicação Spring Boot
	@echo "$(CYAN)Iniciando aplicação Spring Boot...$(NC)"
	@cd infrastructure && $(MAVEN) spring-boot:run

test: ## Executa os testes do projeto
	@echo "$(CYAN)Executando testes...$(NC)"
	@$(MAVEN) test
	@echo "$(GREEN)Testes concluídos!$(NC)"

setup-ksqldb: ## Executa o script SQL inicial no KSQLDB
	@echo "$(CYAN)Configurando KSQLDB com tabelas e streams...$(NC)"
	@SQL_FILE="infrastructure/src/main/resources/ksqldb-init.sql"; \
	if [ ! -f "$$SQL_FILE" ]; then \
		echo "$(YELLOW)Arquivo SQL não encontrado: $$SQL_FILE$(NC)"; \
		exit 1; \
	fi; \
	echo "Aguardando KSQLDB estar pronto..."; \
	for i in 1 2 3 4 5 6; do \
		if curl -s http://localhost:8088/info >/dev/null 2>&1; then \
			echo "$(GREEN)KSQLDB está pronto!$(NC)"; \
			break; \
		fi; \
		if [ $$i -eq 6 ]; then \
			echo "$(YELLOW)KSQLDB ainda não está pronto. Execute 'make setup-ksqldb' manualmente mais tarde.$(NC)"; \
			exit 1; \
		fi; \
		echo "Tentativa $$i/6..."; \
		sleep 5; \
	done; \
	echo "Executando SQL no KSQLDB..."; \
	sed -n '/^[^--]/p' "$$SQL_FILE" | while IFS= read -r line; do \
		cmd=$$(echo "$$line" | sed 's/^[[:space:]]*//;s/[[:space:]]*$$//' | sed 's/;$$//'); \
		if [ -n "$$cmd" ] && ! echo "$$cmd" | grep -qE '^[[:space:]]*--'; then \
			echo "  Executando: $$cmd"; \
			response=$$(curl -s -X POST http://localhost:8088/ksql \
				-H "Content-Type: application/vnd.ksql.v1+json" \
				-d "{\"ksql\": \"$$cmd\", \"streamsProperties\": {}}"); \
			if echo "$$response" | grep -qi "errorMessage"; then \
				error=$$(echo "$$response" | grep -o '"message":"[^"]*"' | head -1 | cut -d'"' -f4); \
				if echo "$$error" | grep -qiE "(does not exist|Unknown|Cannot drop|already exists)"; then \
					echo "    $(YELLOW)⚠ Ignorado: $$error$(NC)"; \
				else \
					echo "    $(YELLOW)⚠ Aviso: $$error$(NC)"; \
				fi; \
			else \
				echo "    $(GREEN)✓$(NC)"; \
			fi; \
			sleep 2; \
		fi; \
	done; \
	echo "$(GREEN)✅ KSQLDB configurado!$(NC)"

reset-ksqldb: ## ⚠️ RESETA o KSQLDB (DROPA e recria todas as tabelas/streams)
	@echo "$(YELLOW)============================================$(NC)"
	@echo "$(YELLOW)⚠️  ATENÇÃO: Isso vai DROPAR todas as tabelas$(NC)"
	@echo "$(YELLOW)    e streams do KSQLDB!$(NC)"
	@echo "$(YELLOW)============================================$(NC)"
	@echo "$(CYAN)Resetando KSQLDB...$(NC)"
	@SQL_FILE="infrastructure/src/main/resources/ksqldb-reset.sql"; \
	if [ ! -f "$$SQL_FILE" ]; then \
		echo "$(YELLOW)Arquivo SQL não encontrado: $$SQL_FILE$(NC)"; \
		exit 1; \
	fi; \
	echo "Aguardando KSQLDB estar pronto..."; \
	for i in 1 2 3 4 5 6; do \
		if curl -s http://localhost:8088/info >/dev/null 2>&1; then \
			echo "$(GREEN)KSQLDB está pronto!$(NC)"; \
			break; \
		fi; \
		if [ $$i -eq 6 ]; then \
			echo "$(YELLOW)KSQLDB não está acessível. Certifique-se de que está rodando.$(NC)"; \
			exit 1; \
		fi; \
		echo "Tentativa $$i/6..."; \
		sleep 5; \
	done; \
	echo "Executando SQL de reset no KSQLDB..."; \
	sed -n '/^[^--]/p' "$$SQL_FILE" | while IFS= read -r line; do \
		cmd=$$(echo "$$line" | sed 's/^[[:space:]]*//;s/[[:space:]]*$$//' | sed 's/;$$//'); \
		if [ -n "$$cmd" ] && ! echo "$$cmd" | grep -qE '^[[:space:]]*--'; then \
			echo "  Executando: $$cmd"; \
			response=$$(curl -s -X POST http://localhost:8088/ksql \
				-H "Content-Type: application/vnd.ksql.v1+json" \
				-d "{\"ksql\": \"$$cmd\", \"streamsProperties\": {}}"); \
			if echo "$$response" | grep -qi "errorMessage"; then \
				error=$$(echo "$$response" | grep -o '"message":"[^"]*"' | head -1 | cut -d'"' -f4); \
				if echo "$$error" | grep -qiE "(does not exist|Unknown|Cannot drop|already exists)"; then \
					echo "    $(YELLOW)⚠ Ignorado: $$error$(NC)"; \
				else \
					echo "    $(YELLOW)⚠ Aviso: $$error$(NC)"; \
				fi; \
			else \
				echo "    $(GREEN)✓$(NC)"; \
			fi; \
			sleep 2; \
		fi; \
	done; \
	echo "$(GREEN)✅ KSQLDB resetado!$(NC)"

full-setup: dependencies build ## Sobe as dependências e compila o projeto
	@echo "$(GREEN)============================================$(NC)"
	@echo "$(GREEN)  Setup completo realizado!$(NC)"
	@echo "$(GREEN)============================================$(NC)"
	@echo "$(YELLOW)Use 'make run' para iniciar a aplicação$(NC)"

stop: down ## Alias para down - para todos os serviços
	@true

start: dependencies ## Alias para dependencies - inicia todas as dependências
	@true


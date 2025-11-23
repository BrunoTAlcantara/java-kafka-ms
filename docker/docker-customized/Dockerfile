# Use uma imagem base oficial e mantida (baseada em Ubuntu Focal)
FROM eclipse-temurin:11-jdk-focal

# Define variáveis de ambiente para evitar interatividade durante a instalação
ENV DEBIAN_FRONTEND=noninteractive

# Instalações necessárias
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    gnupg \
    software-properties-common \
    && rm -rf /var/lib/apt/lists/*

# Adicionar chave e repositório Confluent
# Nota: A sintaxe apt-key é depreciada, mas funcional. Para simplificar, mantivemos compatível.
RUN wget -qO - https://packages.confluent.io/deb/7.4/archive.key | apt-key add - \
    && add-apt-repository "deb [arch=amd64,arm64] https://packages.confluent.io/deb/7.4 stable main" \
    && apt-get update

# Instalar Confluent Platform (inclui ksqldb)
RUN apt-get install -y confluent-platform \
    && rm -rf /var/lib/apt/lists/*

# Criar diretório se não existir e copiar arquivo de propriedades customizado
RUN mkdir -p /etc/ksqldb /etc/ksql
COPY ksqldb-server.properties /etc/ksqldb/ksql-server.properties
# Também copiar para caminho alternativo
COPY ksqldb-server.properties /etc/ksql/ksql-server.properties

# Copiar script de entrada
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

# Variáveis de ambiente padrão (podem ser sobrescritas pelo docker-compose)
ENV KSQL_LISTENERS='http://0.0.0.0:8088' \
    KSQL_BOOTSTRAP_SERVERS='broker:9092'

# Porta exposta
EXPOSE 8088

# Script de entrada - configura propriedades baseado em variáveis de ambiente e inicia o ksql-server
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
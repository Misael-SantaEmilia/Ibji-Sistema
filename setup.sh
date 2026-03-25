#!/bin/bash

# ============================================
# App IBJI - Script de Setup Automatizado
# Sistema de Gestão Eclesiástica
# ============================================

echo "============================================"
echo "   IBJI - Sistema de Gestão Eclesiástica"
echo "   Script de Setup Automatizado"
echo "============================================"
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para verificar comando
check_command() {
    if command -v $1 &> /dev/null; then
        echo -e "${GREEN}✓${NC} $1 encontrado: $($1 --version 2>&1 | head -n1)"
        return 0
    else
        echo -e "${RED}✗${NC} $1 não encontrado. Por favor, instale $1."
        return 1
    fi
}

# Função para verificar porta
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠${NC} Porta $1 já está em uso!"
        return 1
    else
        echo -e "${GREEN}✓${NC} Porta $1 disponível"
        return 0
    fi
}

echo "=== Verificando Pré-requisitos ==="
echo ""

# Verificar Java
check_command "java"
JAVA_CHECK=$?

# Verificar Node.js
check_command "node"
NODE_CHECK=$?

# Verificar npm
check_command "npm"
NPM_CHECK=$?

# Verificar PostgreSQL
check_command "psql"
PSQL_CHECK=$?

echo ""
echo "=== Verificando Portas ==="
echo ""

# Verificar portas
check_port 8080  # Backend
check_port 3000  # Frontend

echo ""
echo "=== Configuração do Banco de Dados ==="
echo ""

read -p "Deseja criar o banco de dados automaticamente? (s/n): " CREATE_DB

if [[ $CREATE_DB == "s" || $CREATE_DB == "S" ]]; then
    echo "Criando banco de dados..."
    
    # Solicitar credenciais do PostgreSQL
    read -p "Usuário PostgreSQL [postgres]: " PG_USER
    PG_USER=${PG_USER:-postgres}
    
    read -s -p "Senha do PostgreSQL: " PG_PASS
    echo ""
    
    # Criar banco de dados
    PGPASSWORD=$PG_PASS psql -U $PG_USER -c "CREATE DATABASE db_ibji;" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} Banco de dados criado com sucesso!"
        
        # Executar schema
        echo "Executando schema..."
        PGPASSWORD=$PG_PASS psql -U $PG_USER -d db_ibji -f ibji-api/src/main/resources/schema.sql
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓${NC} Tabelas criadas com sucesso!"
        else
            echo -e "${RED}✗${NC} Erro ao criar tabelas."
        fi
    else
        echo -e "${YELLOW}⚠${NC} Banco de dados já existe ou erro na criação."
    fi
fi

echo ""
echo "=== Configuração do Backend ==="
echo ""

read -p "Deseja configurar o application.yml? (s/n): " CONFIG_YML

if [[ $CONFIG_YML == "s" || $CONFIG_YML == "S" ]]; then
    echo "Configurando application.yml..."
    
    read -p "Senha do PostgreSQL para o backend [postgres]: " DB_PASS
    DB_PASS=${DB_PASS:-postgres}
    
    # Criar application.yml
    cat > ibji-api/src/main/resources/application.yml << EOF
server:
  port: 8080

spring:
  application:
    name: ibji-api

  datasource:
    url: jdbc:postgresql://localhost:5432/db_ibji
    username: $PG_USER
    password: $DB_PASS
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false
    deserialization:
      fail-on-unknown-properties: false

logging:
  level:
    com.ibji.app: DEBUG
    org.springframework.web: INFO

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
EOF
    
    echo -e "${GREEN}✓${NC} application.yml configurado!"
fi

echo ""
echo "=== Instalando Dependências do Frontend ==="
echo ""

cd ibji-web
npm install
cd ..

echo ""
echo "============================================"
echo "          Setup Concluído!"
echo "============================================"
echo ""
echo "Para iniciar o projeto:"
echo ""
echo "1. Backend (Terminal 1):"
echo "   cd ibji-api && ./mvnw spring-boot:run"
echo ""
echo "2. Frontend (Terminal 2):"
echo "   cd ibji-web && npm start"
echo ""
echo "URLs:"
echo "  - Backend API: http://localhost:8080"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  - Frontend: http://localhost:3000"
echo ""
echo "Credenciais padrão:"
echo "  - Email: admin@ibji.org.br"
echo "  - Senha: admin123"
echo ""
echo "============================================"
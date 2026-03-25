# App IBJI - Guia de Desenvolvimento e Execução

Sistema de Gestão Eclesiástica completo com Backend (Spring Boot), Frontend (React) e Mobile (React Native).

---

## 📋 Pré-requisitos

### Software Necessário

| Software | Versão | Link |
|----------|--------|------|
| **Java JDK** | 21+ | https://adoptium.net/ |
| **Node.js** | 18+ | https://nodejs.org/ |
| **npm** | 9+ | (incluído com Node.js) |
| **PostgreSQL** | 14+ | https://www.postgresql.org/ |
| **Git** | 2.x | https://git-scm.com/ |
| **Maven** | 3.9+ | https://maven.apache.org/ |

### Verificar Instalações

```bash
# Java
java -version
# Deve mostrar: openjdk version "21.x.x"

# Node.js
node -v
# Deve mostrar: v18.x.x ou superior

# npm
npm -v
# Deve mostrar: 9.x.x ou superior

# PostgreSQL
psql --version
# Deve mostrar: psql (PostgreSQL) 14.x.x ou superior

# Maven (opcional, o projeto inclui Maven Wrapper)
./mvnw -v  # Linux/Mac
mvnw.cmd -v  # Windows
```

---

## 🗄️ Configuração do Banco de Dados

### Passo 1: Instalar e Iniciar PostgreSQL

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

**macOS (com Homebrew):**
```bash
brew install postgresql@15
brew services start postgresql@15
```

**Windows:**
Baixe o instalador em https://www.postgresql.org/download/windows/ e siga o assistente.

### Passo 2: Criar Usuário e Banco de Dados

```bash
# Acessar o PostgreSQL como superusuário
sudo -u postgres psql  # Linux
# ou
psql -U postgres       # Mac/Windows

# Criar usuário (opcional, pode usar o postgres padrão)
CREATE USER ibji_user WITH PASSWORD 'ibji_password';

# Dar permissões ao usuário
ALTER USER ibji_user CREATEDB;

# Criar banco de dados
CREATE DATABASE db_ibji OWNER ibji_user;

# Sair do psql
\q
```

### Passo 3: Executar o Script DDL

```bash
# Navegar até o diretório do projeto
cd /caminho/para/IBJI

# Executar o script SQL
psql -U postgres -d db_ibji -f ibji-api/src/main/resources/schema.sql
```

**OU** execute os comandos SQL manualmente:

```sql
-- Conectar ao banco
\c db_ibji

-- Criar tabelas (copiar do PLANO_IMPLEMENTACAO.md ou criar manualmente)
CREATE TABLE membros (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(20),
    data_nascimento DATE,
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    nivel_acesso VARCHAR(10) NOT NULL DEFAULT 'MEMBRO',
    status VARCHAR(10) NOT NULL DEFAULT 'ATIVO',
    foto_url VARCHAR(500),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ... (demais tabelas conforme PLANO_IMPLEMENTACAO.md)

-- Inserir usuário administrador padrão
INSERT INTO membros (nome, cpf, email, nivel_acesso, status)
VALUES ('Administrador Sistema', '000.000.000-00', 'admin@ibji.org.br', 'ADM', 'ATIVO');
```

---

## 🚀 Execução do Projeto

### Estrutura do Projeto

```
IBJI/
├── ibji-api/          # Backend Spring Boot (porta 8080)
├── ibji-web/          # Frontend React (porta 3000)
└── ibji-mobile/       # App React Native (futuro)
```

---

## 📡 Backend (ibji-api)

### Passo 1: Configurar o application.yml

Editar o arquivo `ibji-api/src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/db_ibji
    username: postgres              # Seu usuário PostgreSQL
    password: sua_senha_aqui        # Sua senha PostgreSQL
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate           # validate | update | create | create-drop
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Passo 2: Compilar o Projeto

**Opção A - Usando Maven Wrapper (recomendado):**

```bash
cd ibji-api

# Linux/Mac
./mvnw clean package

# Windows
mvnw.cmd clean package
```

**Opção B - Usando Maven instalado:**

```bash
cd ibji-api
mvn clean package
```

### Passo 3: Executar a Aplicação

**Opção A - Usando Maven:**

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Opção B - Usando Java diretamente:**

```bash
java -jar target/ibji-api-1.0.0.jar
```

**Opção C - Usando IDE (IntelliJ/Eclipse/VS Code):**

1. Importar o projeto como Maven
2. Executar a classe `IbjiApplication.java`
3. Clicar em "Run" ou pressionar F5

### Passo 4: Verificar se está rodando

```
✅ Backend rodando em: http://localhost:8080
✅ Swagger UI: http://localhost:8080/swagger-ui.html
✅ API Docs: http://localhost:8080/api-docs
```

Teste com curl:
```bash
curl http://localhost:8080/api/membresia
```

---

## 🌐 Frontend (ibji-web)

### Passo 1: Instalar Dependências

```bash
cd ibji-web
npm install
```

### Passo 2: Configurar Variáveis de Ambiente

Criar/editar o arquivo `ibji-web/.env`:

```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_NAME=IBJI - Sistema de Gestão Eclesiástica
```

### Passo 3: Executar em modo desenvolvimento

```bash
npm start
```

### Passo 4: Verificar se está rodando

```
✅ Frontend rodando em: http://localhost:3000
✅ Página de Login: http://localhost:3000/login
```

### Comandos Úteis

```bash
# Instalar dependências
npm install

# Executar em desenvolvimento
npm start

# Executar testes
npm test

# Build para produção
npm run build

# Servir build localmente
npx serve -s build
```

---

## 🧪 Testes e Validação

### Testar API Backend

**1. Listar membros:**
```bash
curl -X GET http://localhost:8080/api/membresia
```

**2. Criar membro:**
```bash
curl -X POST http://localhost:8080/api/membresia \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "dataNascimento": "1990-01-15",
    "nivelAcesso": "MEMBRO"
  }'
```

**3. Buscar aniversariantes do mês:**
```bash
curl -X GET "http://localhost:8080/api/aniversariantes?tipo=M"
```

**4. Ver Swagger UI:**
Acesse no navegador: http://localhost:8080/swagger-ui.html

### Testar Frontend

1. Acessar http://localhost:3000
2. Fazer login com:
   - Email: `admin@ibji.org.br`
   - Senha: `admin123` (ajuste conforme sua configuração)
3. Navegar pelas páginas:
   - Dashboard
   - Membros (apenas ADM)
   - Ministérios (ADM e LÍDER)
   - Aniversariantes

---

## 🔧 Troubleshooting Comum

### Erro: "Connection refused" no PostgreSQL

```bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql  # Linux
brew services list                 # macOS

# Iniciar se necessário
sudo systemctl start postgresql   # Linux
brew services start postgresql@15  # macOS
```

### Erro: "Password authentication failed"

```bash
# Resetar senha do usuário postgres
sudo -u postgres psql
ALTER USER postgres PASSWORD 'nova_senha';
\q
```

### Erro: "Port already in use"

**Backend (porta 8080):**
```bash
# Encontrar processo usando a porta
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Matar o processo
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows
```

**Frontend (porta 3000):**
```bash
# Mudar a porta no arquivo .env
PORT=3001 npm start
```

### Erro: "Node modules not found"

```bash
cd ibji-web
rm -rf node_modules package-lock.json
npm install
```

### Erro: "Maven wrapper not found"

```bash
chmod +x mvnw  # Linux/Mac
```

### Erro: "Java version mismatch"

```bash
# Verificar versão
java -version

# Configurar JAVA_HOME (Linux/Mac)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

# Configurar JAVA_HOME (Windows - PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
```

---

## 📝 Credenciais Padrão

| Usuário | Email | Senha | Nível |
|---------|-------|-------|-------|
| Admin | admin@ibji.org.br | admin123 | ADM |

> **⚠️ Importante:** Altere a senha padrão em produção!

---

## 🛠️ Comandos Rápidos

### Iniciar tudo de uma vez (Terminal 1 - Backend)

```bash
cd ibji-api && ./mvnw spring-boot:run
```

### Iniciar tudo de uma vez (Terminal 2 - Frontend)

```bash
cd ibji-web && npm start
```

### Build completo para produção

```bash
# Backend
cd ibji-api && ./mvnw clean package -Pprod

# Frontend
cd ibji-web && npm run build
```

---

## 📚 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/membresia` | Listar membros |
| GET | `/api/membresia/{id}` | Buscar membro |
| POST | `/api/membresia` | Criar membro |
| PUT | `/api/membresia/{id}` | Atualizar membro |
| PATCH | `/api/membresia/{id}/inativar` | Inativar membro |
| GET | `/api/ministerios` | Listar ministérios |
| POST | `/api/ministerios` | Criar ministério |
| GET | `/api/aniversariantes?tipo=M` | Aniversariantes |

---

## 📞 Suporte

Em caso de dúvidas ou problemas:
- Documentação: `PLANO_IMPLEMENTACAO.md`
- Issues: https://github.com/ibji/app-ibji/issues

---

**Versão:** 1.0.0  
**Última atualização:** Março 2026
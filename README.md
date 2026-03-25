# App IBJI - Sistema de Gestão Eclesiástica

O **App IBJI** é um sistema de gestão eclesiástica completo desenvolvido para auxiliar na administração de membros, ministérios, escalas, recados, pedidos de oração e aniversariantes da igreja.

## 🚀 Tecnologias Utilizadas

O projeto é dividido em um ecossistema com Backend, Frontend e Banco de Dados:

### Backend (`ibji-api`)
- **Java 21+** com **Spring Boot**
- **Spring Security + JWT** para autenticação e autorização
- **Spring Data JPA / Hibernate** para ORM
- **PostgreSQL** como banco de dados relacional
- **Swagger / OpenAPI** para documentação da API
- **Maven** para gerenciamento de dependências

### Frontend (`ibji-web`)
- **React 18+** com Functional Components e Hooks
- **Axios** para chamadas HTTP
- **Context API** para gerenciamento de estado global
- **CSS** customizado (módulos e variáveis)

### Infraestrutura
- **Docker e Docker Compose** para conteinerização do banco de dados e backend

## 📂 Estrutura do Projeto

```text
IBJI/
├── ibji-api/          # Backend em Spring Boot (porta 8080)
├── ibji-web/          # Frontend em React (porta 3000)
├── docker-compose.yml # Orquestração dos containers (Postgres + Backend)
├── EXECUCAO_GUIA.md   # Guia detalhado de instalação e execução
├── PLANO_IMPLEMENTACAO.md # Especificações técnicas e arquiteturais
├── setup.sh           # Script de configuração inicial (Linux/Mac)
└── setup.bat          # Script de configuração inicial (Windows)
```

## 🛠️ Como Executar o Projeto

Existem duas formas principais de executar o projeto: via **Docker** (recomendado para testes e infra) ou **Manualmente** para desenvolvimento.

### Opção 1: Usando Docker (Mais fácil)

Com o Docker e Docker Compose instalados, execute o comando na raiz do projeto:

```bash
docker-compose up -d --build
```
Isso iniciará:
- O banco de dados PostgreSQL (`db_ibji`) na porta `5432` automaticamente configurado e populado.
- O backend (`ibji-api`) na porta `8080`.

*(Para iniciar o frontend, siga os passos da Opção 2 para o Frontend)*

### Opção 2: Execução Manual (Desenvolvimento)

Para rodar todo o ambiente manualmente, siga os passos abaixo.

#### Pré-requisitos
- Java JDK 21+
- Node.js 18+ e npm
- PostgreSQL 14+

#### 1. Banco de Dados
Siga os passos em [Guia de Execução](EXECUCAO_GUIA.md) para instalar o PostgreSQL e rodar o script DDL (presente na API ou no Plano de Implementação).

#### 2. Iniciando o Backend
```bash
cd ibji-api
./mvnw clean spring-boot:run   # Linux/Mac
# ou
mvnw.cmd clean spring-boot:run # Windows
```
O backend estará disponível em: `http://localhost:8080`
Documentação Swagger: `http://localhost:8080/swagger-ui.html`

#### 3. Iniciando o Frontend
Em outro terminal:
```bash
cd ibji-web
npm install
npm start
```
O frontend estará disponível em: `http://localhost:3000`

## 📚 Documentação Detalhada

Para mais detalhes sobre as regras de negócio, modelagem do banco de dados, endpoints, resolução de problemas e tutoriais, consulte nossos documentos detalhados:

- 📖 **[Guia de Execução e Troubleshooting](EXECUCAO_GUIA.md)** - Passo a passo completo de como instalar, rodar, testar APIs e resolver problemas comuns.
- 📐 **[Plano de Implementação e Arquitetura](PLANO_IMPLEMENTACAO.md)** - Diagrama do banco de dados, estrutura de pacotes, decisões arquiteturais e scripts DDL.

## 🔐 Credenciais Padrão (Após a execução do schema)

- **Usuário:** `admin@ibji.org.br`
- **Senha:** `admin123`
- **Nível de Acesso:** ADM

> **⚠️ Importante:** Lembre-se de alterar as credenciais em ambiente de produção!

---
*Desenvolvido para gestão eclesiástica. Para dúvidas ou suporte, consulte a documentação inclusa no projeto.*

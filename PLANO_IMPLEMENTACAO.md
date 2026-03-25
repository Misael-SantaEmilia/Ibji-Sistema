# App IBJI - Plano de Implementação

## Etapa 1: Artifacts de Planejamento

---

## 1. Script DDL - PostgreSQL

```sql
-- ============================================
-- BANCO DE DADOS: db_ibji
-- ============================================
CREATE DATABASE db_ibji;

-- ============================================
-- TABELA: membros
-- ============================================
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
    nivel_acesso VARCHAR(10) NOT NULL DEFAULT 'MEMBRO'
        CHECK (nivel_acesso IN ('MEMBRO', 'LIDER', 'ADM')),
    status VARCHAR(10) NOT NULL DEFAULT 'ATIVO'
        CHECK (status IN ('ATIVO', 'INATIVO')),
    foto_url VARCHAR(500),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_membros_status ON membros(status);
CREATE INDEX idx_membros_nivel_acesso ON membros(nivel_acesso);
CREATE INDEX idx_membros_data_nascimento ON membros(data_nascimento);
CREATE INDEX idx_membros_cpf ON membros(cpf);

-- ============================================
-- TABELA: ministerios
-- ============================================
CREATE TABLE ministerios (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(80) NOT NULL,
    lider_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'ATIVO'
        CHECK (status IN ('ATIVO', 'INATIVO')),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ministerio_lider
        FOREIGN KEY (lider_id) REFERENCES membros(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_ministerios_lider ON ministerios(lider_id);
CREATE INDEX idx_ministerios_status ON ministerios(status);

-- ============================================
-- TABELA: ministerio_membros (relacional)
-- ============================================
CREATE TABLE ministerio_membros (
    id BIGSERIAL PRIMARY KEY,
    ministerio_id BIGINT NOT NULL,
    membro_id BIGINT NOT NULL,
    data_entrada DATE NOT NULL DEFAULT CURRENT_DATE,
    data_saida DATE,
    status VARCHAR(10) NOT NULL DEFAULT 'ATIVO'
        CHECK (status IN ('ATIVO', 'INATIVO')),

    CONSTRAINT fk_mm_ministerio
        FOREIGN KEY (ministerio_id) REFERENCES ministerios(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_mm_membro
        FOREIGN KEY (membro_id) REFERENCES membros(id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_ministerio_membro
        UNIQUE (ministerio_id, membro_id)
);

CREATE INDEX idx_mm_ministerio ON ministerio_membros(ministerio_id);
CREATE INDEX idx_mm_membro ON ministerio_membros(membro_id);

-- ============================================
-- TABELA: pedidos_oracao
-- ============================================
CREATE TABLE pedidos_oracao (
    id BIGSERIAL PRIMARY KEY,
    membro_id BIGINT,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    publico BOOLEAN NOT NULL DEFAULT FALSE,
    aprovado_por BIGINT,
    data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP,

    CONSTRAINT fk_po_membro
        FOREIGN KEY (membro_id) REFERENCES membros(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_po_aprovador
        FOREIGN KEY (aprovado_por) REFERENCES membros(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_po_publico ON pedidos_oracao(publico);
CREATE INDEX idx_po_data_pedido ON pedidos_oracao(data_pedido);

-- ============================================
-- TABELA: recados
-- ============================================
CREATE TABLE recados (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    conteudo TEXT NOT NULL,
    publico_alvo VARCHAR(15) NOT NULL
        CHECK (publico_alvo IN ('IGREJA', 'MINISTERIO')),
    ministerio_id BIGINT,
    criado_por BIGINT NOT NULL,
    data_validade DATE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recados_ministerio
        FOREIGN KEY (ministerio_id) REFERENCES ministerios(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_recados_criador
        FOREIGN KEY (criado_por) REFERENCES membros(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_recados_publico_alvo ON recados(publico_alvo);
CREATE INDEX idx_recados_validade ON recados(data_validade);

-- ============================================
-- TABELA: escalas
-- ============================================
CREATE TABLE escalas (
    id BIGSERIAL PRIMARY KEY,
    ministerio_id BIGINT NOT NULL,
    data_escala DATE NOT NULL,
    horario_inicio TIME NOT NULL,
    horario_fim TIME NOT NULL,
    observacao VARCHAR(255),
    criado_por BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_escala_ministerio
        FOREIGN KEY (ministerio_id) REFERENCES ministerios(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_escala_criador
        FOREIGN KEY (criado_por) REFERENCES membros(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_escala_data ON escalas(data_escala);
CREATE INDEX idx_escala_ministerio ON escalas(ministerio_id);

-- ============================================
-- TABELA: escala_membros (quem está na escala)
-- ============================================
CREATE TABLE escala_membros (
    id BIGSERIAL PRIMARY KEY,
    escala_id BIGINT NOT NULL,
    membro_id BIGINT NOT NULL,

    CONSTRAINT fk_em_escala
        FOREIGN KEY (escala_id) REFERENCES escalas(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_em_membro
        FOREIGN KEY (membro_id) REFERENCES membros(id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_escala_membro
        UNIQUE (escala_id, membro_id)
);

-- ============================================
-- DADOS INICIAIS
-- ============================================
INSERT INTO membros (nome, cpf, email, nivel_acesso, status)
VALUES ('Administrador Sistema', '000.000.000-00', 'admin@ibji.org.br', 'ADM', 'ATIVO');
```

---

## 2. Estrutura de Pacotes - Spring Boot

```
com.ibji.app/
├── IbjiApplication.java                    # Main Spring Boot Application
│
├── config/
│   ├── SecurityConfig.java                 # Spring Security + JWT config
│   ├── CorsConfig.java                     # CORS configuration
│   └── OpenApiConfig.java                  # Swagger/OpenAPI config
│
├── controller/
│   ├── MembroController.java              # REST endpoints /api/membresia
│   ├── MinisterioController.java          # REST endpoints /api/ministerios
│   ├── AniversarianteController.java      # REST endpoints /api/aniversariantes
│   ├── PedidoOracaoController.java        # REST endpoints /api/pedidos-oracao
│   ├── RecadoController.java              # REST endpoints /api/recados
│   └── EscalaController.java              # REST endpoints /api/escalas
│
├── service/
│   ├── MembroService.java                 # Business logic for membros
│   ├── MinisterioService.java             # Business logic for ministerios
│   ├── AniversarianteService.java         # Business logic for aniversariantes
│   ├── PedidoOracaoService.java           # Business logic for pedidos_oracao
│   ├── RecadoService.java                 # Business logic for recados
│   └── EscalaService.java                 # Business logic for escalas
│
├── repository/
│   ├── MembroRepository.java              # JpaRepository + custom queries
│   ├── MinisterioRepository.java          # JpaRepository + custom queries
│   ├── MinisterioMembroRepository.java    # JpaRepository
│   ├── PedidoOracaoRepository.java        # JpaRepository + custom queries
│   ├── RecadoRepository.java              # JpaRepository + custom queries
│   ├── EscalaRepository.java              # JpaRepository + custom queries
│   └── EscalaMembroRepository.java        # JpaRepository
│
├── entity/
│   ├── Membro.java                        # @Entity - Tabela membros
│   ├── Ministerio.java                    # @Entity - Tabela ministerios
│   ├── MinisterioMembro.java              # @Entity - Tabela ministerio_membros
│   ├── PedidoOracao.java                  # @Entity - Tabela pedidos_oracao
│   ├── Recado.java                        # @Entity - Tabela recados
│   ├── Escala.java                        # @Entity - Tabela escalas
│   └── EscalaMembro.java                  # @Entity - Tabela escala_membros
│
├── dto/
│   ├── request/
│   │   ├── MembroRequestDTO.java
│   │   ├── MinisterioRequestDTO.java
│   │   ├── PedidoOracaoRequestDTO.java
│   │   ├── RecadoRequestDTO.java
│   │   └── EscalaRequestDTO.java
│   ├── response/
│   │   ├── MembroResponseDTO.java
│   │   ├── MinisterioResponseDTO.java
│   │   ├── AniversarianteResponseDTO.java
│   │   ├── PedidoOracaoResponseDTO.java
│   │   ├── RecadoResponseDTO.java
│   │   ├── EscalaResponseDTO.java
│   │   └── PagedResponseDTO.java
│   └── filter/
│       └── MembroFilterDTO.java
│
├── security/
│   ├── JwtTokenProvider.java              # JWT generation/validation
│   ├── JwtAuthenticationFilter.java       # JWT filter for Security
│   ├── UserDetailsServiceImpl.java        # Custom UserDetailsService
│   └── SecurityUser.java                  # Principal wrapper
│
├── exception/
│   ├── GlobalExceptionHandler.java         # @ControllerAdvice
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── ErrorDetails.java
│
├── mapper/
│   ├── MembroMapper.java                  # MapStruct or manual mapping
│   ├── MinisterioMapper.java
│   └── ...
│
├── enums/
│   ├── NivelAcesso.java                   # MEMBRO, LIDER, ADM
│   ├── Status.java                        # ATIVO, INATIVO
│   └── PublicoAlvo.java                   # IGREJA, MINISTERIO
│
└── util/
    ├── AniversarioUtil.java               # Date utilities for birthdays
    └── PageableUtil.java                  # Pagination utilities
```

---

## 3. Estrutura de Pastas - React (Front-end Web)

```
ibji-web/
├── public/
│   ├── index.html
│   ├── favicon.ico
│   └── assets/
│       └── images/
│
├── src/
│   ├── index.js
│   ├── App.js
│   ├── App.css
│   │
│   ├── api/
│   │   ├── apiClient.js                    # Axios instance with interceptors
│   │   ├── membroService.js                # API calls for /api/membresia
│   │   ├── ministerioService.js            # API calls for /api/ministerios
│   │   ├── aniversarianteService.js        # API calls for /api/aniversariantes
│   │   ├── pedidoOracaoService.js          # API calls for /api/pedidos-oracao
│   │   ├── recadoService.js                # API calls for /api/recados
│   │   └── escalaService.js                # API calls for /api/escalas
│   │
│   ├── components/
│   │   ├── common/
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── Footer.jsx
│   │   │   ├── Loading.jsx
│   │   │   ├── Modal.jsx
│   │   │   ├── Pagination.jsx
│   │   │   ├── SearchInput.jsx
│   │   │   ├── StatusBadge.jsx
│   │   │   └── ConfirmDialog.jsx
│   │   │
│   │   ├── membros/
│   │   │   ├── MembroList.jsx
│   │   │   ├── MembroForm.jsx
│   │   │   ├── MembroCard.jsx
│   │   │   └── MembroFilter.jsx
│   │   │
│   │   ├── ministerios/
│   │   │   ├── MinisterioList.jsx
│   │   │   ├── MinisterioForm.jsx
│   │   │   ├── MinisterioCard.jsx
│   │   │   └── MinisterioMembros.jsx
│   │   │
│   │   ├── escalas/
│   │   │   ├── EscalaList.jsx
│   │   │   ├── EscalaForm.jsx
│   │   │   └── EscalaDetalhe.jsx
│   │   │
│   │   ├── pedidos-oracao/
│   │   │   ├── PedidoList.jsx
│   │   │   ├── PedidoForm.jsx
│   │   │   └── PedidoAprovacao.jsx
│   │   │
│   │   └── recados/
│   │       ├── RecadoList.jsx
│   │       ├── RecadoForm.jsx
│   │       └── RecadoCard.jsx
│   │
│   ├── pages/
│   │   ├── Dashboard.jsx
│   │   ├── LoginPage.jsx
│   │   ├── MembrosPage.jsx
│   │   ├── MinisteriosPage.jsx
│   │   ├── EscalasPage.jsx
│   │   ├── PedidosOracaoPage.jsx
│   │   ├── RecadosPage.jsx
│   │   └── AniversariantesPage.jsx
│   │
│   ├── context/
│   │   ├── AuthContext.jsx                  # Authentication context
│   │   └── AppContext.jsx                   # Global app state
│   │
│   ├── hooks/
│   │   ├── useAuth.js
│   │   ├── useApi.js
│   │   └── usePagination.js
│   │
│   ├── routes/
│   │   ├── AppRoutes.jsx
│   │   └── PrivateRoute.jsx
│   │
│   ├── utils/
│   │   ├── dateUtils.js
│   │   ├── formatters.js
│   │   └── validators.js
│   │
│   └── styles/
│       ├── global.css
│       ├── variables.css
│       └── components/
│           ├── buttons.css
│           ├── forms.css
│           └── tables.css
│
├── .env
├── .env.example
├── package.json
└── README.md
```

---

## Resumo das Decisões Arquiteturais

| Aspecto | Decisão |
|---------|---------|
| **Banco de Dados** | PostgreSQL com sequences (BIGSERIAL) |
| **ORM** | JPA/Hibernate com Lombok |
| **DTO Pattern** | Separação Request/Response DTOs |
| **Autenticação** | Spring Security + JWT |
| **Validação** | Bean Validation (@Valid, @NotBlank, etc.) |
| **Soft Delete** | Campo `status` (ATIVO/INATIVO) em vez de DELETE |
| **Front-end** | React com Functional Components + Hooks |
| **HTTP Client** | Axios com interceptors |
| **CORS** | Configurado no backend |

---

**Status: Aguardando aprovação do usuário para prosseguir para Etapa 2.**

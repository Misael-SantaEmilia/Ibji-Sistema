-- ============================================
-- App IBJI - Script de Criação do Banco de Dados
-- Sistema de Gestão Eclesiástica
-- ============================================

-- Passo 1: Criar o banco de dados (executar como superusuário)
-- CREATE DATABASE db_ibji;

-- Passo 2: Conectar ao banco criado
-- \c db_ibji

-- ============================================
-- TABELA: membros
-- ============================================
CREATE TABLE IF NOT EXISTS membros (
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

CREATE INDEX IF NOT EXISTS idx_membros_status ON membros(status);
CREATE INDEX IF NOT EXISTS idx_membros_nivel_acesso ON membros(nivel_acesso);
CREATE INDEX IF NOT EXISTS idx_membros_data_nascimento ON membros(data_nascimento);
CREATE INDEX IF NOT EXISTS idx_membros_cpf ON membros(cpf);
CREATE INDEX IF NOT EXISTS idx_membros_email ON membros(email);

-- ============================================
-- TABELA: ministerios
-- ============================================
CREATE TABLE IF NOT EXISTS ministerios (
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

CREATE INDEX IF NOT EXISTS idx_ministerios_lider ON ministerios(lider_id);
CREATE INDEX IF NOT EXISTS idx_ministerios_status ON ministerios(status);

-- ============================================
-- TABELA: ministerio_membros (relacional)
-- ============================================
CREATE TABLE IF NOT EXISTS ministerio_membros (
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

CREATE INDEX IF NOT EXISTS idx_mm_ministerio ON ministerio_membros(ministerio_id);
CREATE INDEX IF NOT EXISTS idx_mm_membro ON ministerio_membros(membro_id);

-- ============================================
-- TABELA: pedidos_oracao
-- ============================================
CREATE TABLE IF NOT EXISTS pedidos_oracao (
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

CREATE INDEX IF NOT EXISTS idx_po_publico ON pedidos_oracao(publico);
CREATE INDEX IF NOT EXISTS idx_po_data_pedido ON pedidos_oracao(data_pedido);

-- ============================================
-- TABELA: recados
-- ============================================
CREATE TABLE IF NOT EXISTS recados (
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

CREATE INDEX IF NOT EXISTS idx_recados_publico_alvo ON recados(publico_alvo);
CREATE INDEX IF NOT EXISTS idx_recados_validade ON recados(data_validade);

-- ============================================
-- TABELA: escalas
-- ============================================
CREATE TABLE IF NOT EXISTS escalas (
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

CREATE INDEX IF NOT EXISTS idx_escala_data ON escalas(data_escala);
CREATE INDEX IF NOT EXISTS idx_escala_ministerio ON escalas(ministerio_id);

-- ============================================
-- TABELA: escala_membros (quem está na escala)
-- ============================================
CREATE TABLE IF NOT EXISTS escala_membros (
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

-- Usuário Administrador padrão
-- Email: admin@ibji.org.br | Senha: admin123
-- NOTA: A senha deve ser criptografada no backend (BCrypt)
INSERT INTO membros (nome, cpf, email, telefone, nivel_acesso, status)
VALUES ('Administrador Sistema', '000.000.000-00', 'admin@ibji.org.br', '(00) 00000-0000', 'ADM', 'ATIVO')
ON CONFLICT (cpf) DO NOTHING;

-- Exemplo de Líder
INSERT INTO membros (nome, cpf, email, nivel_acesso, status)
VALUES ('João Líder', '111.111.111-11', 'lider@ibji.org.br', 'LIDER', 'ATIVO')
ON CONFLICT (cpf) DO NOTHING;

-- Exemplo de Membro comum
INSERT INTO membros (nome, cpf, email, data_nascimento, nivel_acesso, status)
VALUES 
    ('Maria Silva', '222.222.222-22', 'maria@email.com', '1990-03-15', 'MEMBRO', 'ATIVO'),
    ('Pedro Santos', '333.333.333-33', 'pedro@email.com', '1985-07-22', 'MEMBRO', 'ATIVO'),
    ('Ana Oliveira', '444.444.444-44', 'ana@email.com', '1992-12-01', 'MEMBRO', 'ATIVO')
ON CONFLICT (cpf) DO NOTHING;

-- ============================================
-- FIM DO SCRIPT
-- ============================================
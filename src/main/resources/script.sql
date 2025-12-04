CREATE TABLE veiculo (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(10) UNIQUE,
    modelo VARCHAR(100),
    ano INTEGER,
    status VARCHAR(20) DEFAULT 'disponivel',
    ativo BOOLEAN DEFAULT true
);

CREATE TABLE motorista (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    cnh VARCHAR(20) UNIQUE,
    validade_cnh DATE,
    ativo BOOLEAN DEFAULT true
);

CREATE TABLE viagem (
    id SERIAL PRIMARY KEY,
    veiculo_id INTEGER REFERENCES veiculo(id),
    motorista_id INTEGER REFERENCES motorista(id),
    destino VARCHAR(200),
    data_saida TIMESTAMP,
    data_retorno TIMESTAMP,
    ativo BOOLEAN DEFAULT true
);

CREATE TABLE abastecimento (
    id SERIAL PRIMARY KEY,
    veiculo_id INTEGER REFERENCES veiculo(id),
    data TIMESTAMP,
    litros NUMERIC(10, 2),
    valor_total NUMERIC(10, 2),
    ativo BOOLEAN DEFAULT true
);

CREATE TABLE manutencao (
    id SERIAL PRIMARY KEY,
    veiculo_id INTEGER REFERENCES veiculo(id),
    descricao TEXT,
    custo NUMERIC(10, 2),
    data TIMESTAMP,
    ativo BOOLEAN DEFAULT true
);

# Especificação de API – Sistema de Controle de Frota

**Escopo:** gerenciamento de veículos, motoristas, viagens, abastecimentos e manutenções.

---
## Texto padrão (reutilizável em outros projetos)
- Todos os recursos possuem **CRUD**, sendo que o **D significa desativar** (soft delete): o registro não é removido, apenas tem `ativo=false`.
- **Adicionar coluna `ativo`** (boolean, default `true`) em todas as tabelas.
- **GETs aceitam QueryParams opcionais** para filtro simples. Não haverá paginação ou ordenação neste documento.
- **Formatação de datas** em ISO 8601 (ex.: `2025-11-03` ou `2025-11-03T14:00:00Z`).
- **Códigos de resposta** (exemplos): `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`.

---
## Modelo de Dados (tabelas sugeridas)

### 1) Veiculo
- `id` (PK, inteiro)
- `placa` (string, único)
- `modelo` (string)
- `ano` (inteiro)
- `status` (enum: `disponivel`, `em_manutencao`, `em_viagem`, `inativo`)
- `ativo` (bool, default `true`)

### 2) Motorista
- `id` (PK, inteiro)
- `nome` (string)
- `cnh` (string, único)
- `validade_cnh` (date)
- `ativo` (bool, default `true`)

### 3) Viagem
- `id` (PK, inteiro)
- `veiculo_id` (FK -> Veiculo.id)
- `motorista_id` (FK -> Motorista.id)
- `destino` (string)
- `data_saida` (datetime)
- `data_retorno` (datetime | null enquanto em andamento)
- `ativo` (bool, default `true`)

### 4) Abastecimento
- `id` (PK, inteiro)
- `veiculo_id` (FK -> Veiculo.id)
- `data` (datetime)
- `litros` (decimal)
- `valor_total` (decimal)
- `ativo` (bool, default `true`)

### 5) Manutencao
- `id` (PK, inteiro)
- `veiculo_id` (FK -> Veiculo.id)
- `descricao` (string)
- `custo` (decimal)
- `data` (datetime)
- `ativo` (bool, default `true`)

---
## Endpoints – CRUD (com desativação)
> **Nota:** Todos os QueryParams em GET são **opcionais**.

### Veículos
**POST /veiculos**  
Body:
```json
{
  "placa": "ABC1D23",
  "modelo": "Doblò Cargo",
  "ano": 2022,
  "status": "disponivel"
}
```
**GET /veiculos** (filtros opcionais: `placa`, `modelo`, `ano`, `status`, `ativo`)  
Ex.: `/veiculos?status=disponivel`

**GET /veiculos/{id}**

**PUT /veiculos/{id}**  
Body (ex.):
```json
{ "modelo": "Doblò Cargo 1.8", "status": "em_manutencao" }
```

**PATCH /veiculos/{id}/desativar**  
Efeito: `ativo=false` (204).

---
### Motoristas
**POST /motoristas**  
Body:
```json
{ "nome": "João Silva", "cnh": "1234567890", "validade_cnh": "2026-08-31" }
```
**GET /motoristas** (filtros: `nome`, `cnh`, `validade_cnh_ate`, `ativo`)  
Ex.: `/motoristas?validade_cnh_ate=2026-12-31`

**GET /motoristas/{id}**

**PUT /motoristas/{id}**
```json
{ "nome": "João P. Silva", "validade_cnh": "2027-01-15" }
```
**PATCH /motoristas/{id}/desativar** → `ativo=false`.

---
### Viagens
**POST /viagens**  
Body:
```json
{
  "veiculo_id": 1,
  "motorista_id": 10,
  "destino": "Recife-PE",
  "data_saida": "2025-11-03T08:00:00Z"
}
```
**GET /viagens** (filtros: `veiculo_id`, `motorista_id`, `destino`, `data_ini`, `data_fim`, `em_andamento`, `ativo`)  
Ex.: `/viagens?em_andamento=true` (sem `data_retorno`).

**GET /viagens/{id}**

**PUT /viagens/{id}**  
Body:
```json
{ "data_retorno": "2025-11-05T18:30:00Z" }
```
**PATCH /viagens/{id}/desativar** → `ativo=false`.

---
### Abastecimentos
**POST /abastecimentos**  
Body:
```json
{ "veiculo_id": 1, "data": "2025-11-03T13:00:00Z", "litros": 45.7, "valor_total": 319.90 }
```
**GET /abastecimentos** (filtros: `veiculo_id`, `data_ini`, `data_fim`, `ativo`)

**GET /abastecimentos/{id}**

**PUT /abastecimentos/{id}**
```json
{ "litros": 47.0, "valor_total": 329.50 }
```
**PATCH /abastecimentos/{id}/desativar** → `ativo=false`.

---
### Manutenções
**POST /manutencoes**  
Body:
```json
{ "veiculo_id": 1, "descricao": "Troca de pastilhas de freio", "custo": 520.00, "data": "2025-11-02T10:15:00Z" }
```
**GET /manutencoes** (filtros: `veiculo_id`, `data_ini`, `data_fim`, `ativo`)

**GET /manutencoes/{id}**

**PUT /manutencoes/{id}**
```json
{ "descricao": "Troca de pastilhas + disco", "custo": 780.00 }
```
**PATCH /manutencoes/{id}/desativar** → `ativo=false`.

---
## Endpoints Específicos

### 1) Veículos disponíveis agora
**GET /relatorios/veiculos-disponiveis**  
Critério: `veiculo.status = 'disponivel' AND veiculo.ativo=true` **e** não constar em viagem com `data_retorno IS NULL`.

**Exemplo de resposta**
```json
[
  { "id": 1, "placa": "ABC1D23", "modelo": "Doblò Cargo", "ano": 2022 },
  { "id": 4, "placa": "XYZ9E88", "modelo": "Fiorino", "ano": 2021 }
]
```

### 2) Viagens por período
**GET /relatorios/viagens**  
QueryParams: `data_ini`, `data_fim`, `veiculo_id`, `motorista_id` (todos opcionais).  
Ex.: `/relatorios/viagens?data_ini=2025-11-01&data_fim=2025-11-30&motorista_id=10`

```json
{
  "periodo": { "ini": "2025-11-01", "fim": "2025-11-30" },
  "total": 7,
  "itens": [
    { "id": 12, "veiculo_id": 1, "motorista_id": 10, "destino": "Recife-PE", "data_saida": "2025-11-03T08:00:00Z", "data_retorno": "2025-11-05T18:30:00Z" }
  ]
}
```

### 3) Custos por veículo (manutenção + abastecimento)
**GET /relatorios/custos-veiculo**  
QueryParams: `veiculo_id` (opcional para listar todos), `data_ini`, `data_fim`.

```json
[
  {
    "veiculo_id": 1,
    "periodo": { "ini": "2025-11-01", "fim": "2025-11-30" },
    "abastecimento_total": 1875.40,
    "manutencao_total": 1320.00,
    "custo_total": 3195.40
  }
]
```

### 4) Próximas CNHs a vencer
**GET /relatorios/cnhs-a-vencer**  
QueryParams: `ate` (data limite, ex.: `2026-03-31`).  
Resposta:
```json
[
  { "motorista_id": 10, "nome": "João Silva", "validade_cnh": "2026-02-15" }
]
```

### 5) Abastecimentos por período (detalhe)
**GET /relatorios/abastecimentos**  
QueryParams: `veiculo_id`, `data_ini`, `data_fim`.
```json
{
  "veiculo_id": 1,
  "periodo": { "ini": "2025-11-01", "fim": "2025-11-30" },
  "total_registros": 5,
  "total_litros": 230.5,
  "total_gasto": 1589.30,
  "itens": [
    { "id": 90, "data": "2025-11-03T13:00:00Z", "litros": 45.7, "valor_total": 319.90 }
  ]
}
```

### 6) Manutenções por período (detalhe)
**GET /relatorios/manutencoes**  
QueryParams: `veiculo_id`, `data_ini`, `data_fim`.
```json
{
  "veiculo_id": 1,
  "periodo": { "ini": "2025-11-01", "fim": "2025-11-30" },
  "total_registros": 2,
  "total_custo": 1320.00,
  "itens": [
    { "id": 77, "data": "2025-11-02T10:15:00Z", "descricao": "Troca de pastilhas de freio", "custo": 520.00 }
  ]
}
```

### 7) Viagens em andamento
**GET /viagens/em-andamento**  
Retorna viagens com `data_retorno = null`.

```json
[
  { "id": 14, "veiculo_id": 4, "motorista_id": 12, "destino": "Caruaru-PE", "data_saida": "2025-11-03T07:30:00Z" }
]
```

---
## Regras e Observações
- Ao iniciar uma viagem, atualize o `status` do veículo para `em_viagem`; ao encerrar (definir `data_retorno`), volte para `disponivel`, salvo se houver manutenção registrada no mesmo dia (neste caso, `em_manutencao`).
- Registros `inativo` em `status` de veículo são diferentes de `ativo=false`:
  - `status=inativo` descreve a **condição operacional** (ex.: veículo aposentado),
  - `ativo=false` é **soft delete** administrativo (não deve aparecer em listagens padrão).
- Para filtros por período, considerar registros cujo campo `data`/`data_saida`/`data_retorno` **caia dentro** do intervalo `[data_ini, data_fim]`.

---
## Exemplos rápidos de respostas
**201 Created – Veículo**
```json
{ "id": 1, "placa": "ABC1D23", "modelo": "Doblò Cargo", "ano": 2022, "status": "disponivel", "ativo": true }
```
**204 No Content – Desativação** (sem body)

---
## Possíveis validações
- `placa` e `cnh` únicos.
- `validade_cnh` não pode estar vencida para iniciar nova viagem.
- `litros` > 0 e `valor_total` ≥ 0 em abastecimentos.
- `custo` ≥ 0 em manutenções.
- Não iniciar viagem com veículo `em_manutencao` ou `inativo`.

---
## Roadmap sugerido (opcional)
- Adicionar odômetro `km_atual`/`km_anterior` nos abastecimentos para calcular consumo.
- Vincular tipo de manutenção (preventiva/corretiva) e oficina.
- Registrar ocorrências (multas, avarias) por viagem.


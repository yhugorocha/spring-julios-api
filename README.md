# Expense API

API REST para controle de despesas e receitas, com autenticacao JWT e isolamento de dados por usuario.

## Funcionalidades

- Cadastro e login de usuarios com JWT.
- Gestao de categorias por usuario (ativacao e desativacao).
- Gestao de transacoes financeiras.
- Filtro de transacoes por mes no endpoint de listagem.
- Atualizacao de valor e exclusao de transacoes.
- Controle de acesso por perfil (`ADMIN` e `USER`).
- Migracoes de banco com Flyway.
- Observabilidade com Spring Boot Actuator, Micrometer, Prometheus e Grafana.

## Stack

- Java 21.
- Spring Boot 3.5.11.
- Spring Security + JWT (`jjwt`).
- Spring Data JPA.
- PostgreSQL.
- Flyway.
- Spring Boot Actuator.
- Micrometer + Prometheus Registry.
- Grafana.
- Maven.
- Lombok.

## Pre-requisitos

- Java 21+.
- Docker e Docker Compose (recomendado para ambiente local).

## Variaveis de ambiente da aplicacao

- `SPRING_DATASOURCE_URL`.
- `SPRING_DATASOURCE_USERNAME`.
- `SPRING_DATASOURCE_PASSWORD`.
- `JWT_SECRET`.
- `JWT_EXPIRATION_MS`.

Exemplo (PowerShell):

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/expense"
$env:SPRING_DATASOURCE_USERNAME="expense"
$env:SPRING_DATASOURCE_PASSWORD="expense"
$env:JWT_SECRET="sua-chave-secreta-com-pelo-menos-32-caracteres"
$env:JWT_EXPIRATION_MS="3600000"
```

## Subindo infraestrutura local com Docker

O `docker-compose.yml` deste projeto sobe:

- PostgreSQL em `localhost:5432`.
- Prometheus em `localhost:9090`.
- Grafana em `localhost:3000`.

Comando:

```powershell
docker compose up -d
```

Observacao importante:

- No estado atual, o compose nao sobe a API Spring Boot.
- O Prometheus esta configurado para coletar metricas da API em `host.docker.internal:8080`.
- Portanto, rode a API localmente na porta `8080` para que o scrape funcione.

Arquivos de monitoramento:

- `monitoring/prometheus.yml`.
- `monitoring/grafana/provisioning/datasources/datasource.yml`.

## Como executar a API

```powershell
.\mvnw.cmd spring-boot:run
```

Build:

```powershell
.\mvnw.cmd clean package
```

## Autenticacao e autorizacao

- Endpoints publicos: `/api/auth/**`.
- Endpoints de usuario (admin): `/api/users/**`.
- Demais endpoints exigem `Authorization: Bearer <token>`.
- Endpoints de actuator estao liberados em `/actuator/**`.

## Endpoints principais

### Auth

- `POST /api/auth/signup`.
- `POST /api/auth/login`.

### Usuarios (somente ADMIN)

- `POST /api/users`.
- `GET /api/users`.

### Categorias (usuario autenticado)

- `POST /api/categories`.
- `GET /api/categories`.
- `PATCH /api/categories/{categoryId}/activate`.
- `PATCH /api/categories/{categoryId}/deactivate`.

### Transacoes (usuario autenticado)

- `POST /api/transactions`.
- `GET /api/transactions`.
- `GET /api/transactions?month=yyyy-MM`.
- `PATCH /api/transactions/{transactionId}/amount`.
- `DELETE /api/transactions/{transactionId}`.

Filtro mensal de transacoes:

- Query param: `month` (opcional), formato `yyyy-MM`.
- Sem `month`, o backend usa o mes atual automaticamente.
- Exemplos:
  - `GET /api/transactions?month=2026-03`.
  - `GET /api/transactions?month=2026-02`.

### Observabilidade

- `GET /actuator/health`.
- `GET /actuator/info`.
- `GET /actuator/metrics`.
- `GET /actuator/prometheus`.

## Grafana

Acesso local:

- URL: `http://localhost:3000`.
- Usuario padrao: `admin`.
- Senha padrao: `admin`.

Credenciais podem ser sobrescritas por variaveis no compose:

- `GRAFANA_ADMIN_USER`.
- `GRAFANA_ADMIN_PASSWORD`.

## Regras de negocio importantes

- Cada usuario acessa apenas os proprios dados.
- Categoria nao e excluida fisicamente: apenas ativada/desativada.
- Apenas o dono da categoria pode ativar/desativar.
- Apenas o dono da transacao pode atualizar/excluir.
- Nao e permitido criar transacao com categoria inativa.

## Tipos e enums

- `CategoryType`: `INCOME`, `EXPENSE`.
- `UserRole`: `ADMIN`, `USER`.

## Formato de erro da API

As respostas de erro seguem o modelo:

```json
{
  "timestamp": "2026-03-02T18:30:00Z",
  "status": 400,
  "error": "Requisicao invalida",
  "message": "Falha na validacao da requisicao.",
  "path": "/api/transactions",
  "fieldErrors": [
    {
      "field": "amount",
      "message": "Valor deve ser maior que zero"
    }
  ]
}
```

## Migracoes

As migracoes ficam em `src/main/resources/db/migration`:

- `V1__create_single_tenant_schema.sql`.
- `V2__add_active_to_category.sql`.

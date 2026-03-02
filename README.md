# Expense API

API REST para controle de despesas e receitas, com autenticação JWT e isolamento de dados por usuário.

## Funcionalidades

- Cadastro e login de usuários com JWT.
- Gestão de categorias por usuário.
- Desativação de categoria (sem exclusão física).
- Gestão de transações financeiras.
- Atualização de valor e exclusão de transações.
- Controle de acesso por perfil (`ADMIN` e `USER`).
- Migrações de banco com Flyway.

## Stack

- Java 21
- Spring Boot 3.5.11
- Spring Security + JWT (`jjwt`)
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- Lombok

## Pré-requisitos

- Java 21+
- Docker e Docker Compose (opcional, recomendado para banco local)

## Configuração de ambiente

A aplicação usa as seguintes variáveis:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`

Exemplo (PowerShell):

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/expensedb"
$env:SPRING_DATASOURCE_USERNAME="user_expense"
$env:SPRING_DATASOURCE_PASSWORD="password_expense"
$env:JWT_SECRET="sua-chave-secreta-com-pelo-menos-32-caracteres"
$env:JWT_EXPIRATION_MS="3600000"
```

## Banco de dados local com Docker

```powershell
docker compose up -d
```

Isso inicia um PostgreSQL na porta `5432` com:

- banco: `expensedb`
- usuário: `user_expense`
- senha: `password_expense`

## Como executar

```powershell
.\mvnw.cmd spring-boot:run
```

Ou gerar build:

```powershell
.\mvnw.cmd clean package
```

## Autenticação

- Endpoints públicos: `/api/auth/**`
- Endpoints de usuário (admin): `/api/users/**`
- Demais endpoints exigem `Authorization: Bearer <token>`

## Endpoints principais

### Auth

- `POST /api/auth/signup`
- `POST /api/auth/login`

### Usuários (somente ADMIN)

- `POST /api/users`
- `GET /api/users`

### Categorias (usuário autenticado)

- `POST /api/categories`
- `GET /api/categories` (retorna categorias ativas do usuário autenticado)
- `PATCH /api/categories/{categoryId}/deactivate`

### Transações (usuário autenticado)

- `POST /api/transactions`
- `GET /api/transactions`
- `PATCH /api/transactions/{transactionId}/amount`
- `DELETE /api/transactions/{transactionId}`

## Regras de negócio importantes

- Cada usuário acessa apenas os próprios dados.
- Categoria não é excluída: apenas desativada.
- Apenas o dono da categoria pode desativá-la.
- Apenas o dono da transação pode atualizar/excluir.
- Não é permitido criar transação com categoria inativa.

## Tipos e enums

- `CategoryType`: `INCOME`, `EXPENSE`
- `UserRole`: `ADMIN`, `USER`

## Formato de erro da API

As respostas de erro seguem o modelo:

```json
{
  "timestamp": "2026-03-02T18:30:00Z",
  "status": 400,
  "error": "Requisição inválida",
  "message": "Falha na validação da requisição.",
  "path": "/api/transactions",
  "fieldErrors": [
    {
      "field": "amount",
      "message": "Valor deve ser maior que zero"
    }
  ]
}
```

## Migrações

As migrações ficam em `src/main/resources/db/migration`:

- `V1__create_single_tenant_schema.sql`
- `V2__add_active_to_category.sql`


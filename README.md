# Saldo Certo

Aplicacao web pessoal para controle financeiro mensal. O objetivo e substituir uma planilha simples e mostrar rapidamente quanto entrou, quanto saiu, o que ja foi pago, o que ainda esta pendente e quanto sobra no mes.

## Stack

- Backend: Java 21, Spring Boot, API REST em `/api/v1`
- Arquitetura backend: Hexagonal / Clean Architecture
- Banco: PostgreSQL com Flyway
- Frontend: Next.js, React, TypeScript, Tailwind CSS
- Graficos: Recharts
- Docker Compose: somente PostgreSQL

## Rodando localmente

Suba somente o PostgreSQL pelo Docker:

```bash
docker compose up -d
```

Banco local:

- Host: `localhost`
- Port: `5433`
- Database: `saldo_certo`
- User: `saldo`
- Password: `saldo`

Suba o backend pela IDE usando:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/saldo_certo
SPRING_DATASOURCE_USERNAME=saldo
SPRING_DATASOURCE_PASSWORD=saldo
```

Suba o frontend localmente em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

URLs:

- Frontend: http://localhost:3000
- Backend: http://localhost:8080/api/v1
- PostgreSQL: localhost:5433

O CORS local permite as origens:

```text
http://localhost:3000
http://127.0.0.1:3000
http://localhost:3001
http://127.0.0.1:3001
```

Para resetar completamente o banco local:

```bash
docker compose down -v
docker compose up -d
```

## Estrutura

```text
backend/
  src/main/java/br/com/meusaldomensal
    domain/          Regras e modelos centrais
    application/     Portas, comandos, queries e casos de uso
    adapters/in/     Controllers REST, DTOs e tratamento de erro
    adapters/out/    Persistencia JPA e adapters de repository
    infrastructure/  Configuracoes
  src/main/resources/db/migration

frontend/
  src/app            Rotas Next.js
  src/components     Componentes reutilizaveis
  src/lib            Cliente HTTP, tipos e formatadores
```

## Regras implementadas

- Receita e despesa precisam ter valor maior que zero.
- Despesa exige categoria e data de vencimento.
- Mes e ano sao obrigatorios para salario, receita e despesa.
- Despesa pendente com vencimento anterior ao dia atual aparece como atrasada.
- Dashboard calcula:
  - total de receitas = salario + receitas extras
  - saldo restante = total de receitas - total de despesas
  - percentual comprometido = total de despesas / total de receitas * 100
- Fechamento mensal nao permite mes sem lancamentos.
- Categorias iniciais sao criadas via Flyway.

## Endpoints principais

- `GET /api/v1/dashboard?month=6&year=2026`
- `POST /api/v1/salaries`
- `GET /api/v1/salaries?month=6&year=2026`
- `POST /api/v1/incomes`
- `GET /api/v1/incomes?month=6&year=2026`
- `POST /api/v1/expenses`
- `PATCH /api/v1/expenses/{id}/pay`
- `PATCH /api/v1/expenses/{id}/pending`
- `GET /api/v1/categories`
- `POST /api/v1/monthly-summaries/close`

Payloads de exemplo estao em [docs/api-examples.http](docs/api-examples.http).

## Fluxo rapido de teste

1. Suba o Postgres com `docker compose up -d`.
2. Suba o backend pela IDE.
3. Suba o frontend com `npm run dev` dentro de `frontend`.
4. Acesse http://localhost:3000.
5. Confira as categorias iniciais em "Categorias".
6. Cadastre salario, receitas e despesas.
7. Volte ao dashboard para conferir saldo, comprometimento e graficos.
8. Feche o mes em "Resumo".

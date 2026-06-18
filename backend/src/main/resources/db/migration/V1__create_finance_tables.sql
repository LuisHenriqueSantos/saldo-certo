CREATE TABLE categorias (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(20) NOT NULL,
    color VARCHAR(20) NOT NULL,
    icon VARCHAR(60) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE salarios (
    id UUID PRIMARY KEY,
    description VARCHAR(160) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    received_date DATE NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE receitas (
    id UUID PRIMARY KEY,
    description VARCHAR(160) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    income_date DATE NOT NULL,
    type VARCHAR(40) NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE despesas (
    id UUID PRIMARY KEY,
    categoria_id UUID NOT NULL REFERENCES categorias(id),
    description VARCHAR(160) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    installment_number INTEGER,
    total_installments INTEGER,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE resumos_mensais (
    id UUID PRIMARY KEY,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    total_salary NUMERIC(19, 2) NOT NULL,
    total_extra_income NUMERIC(19, 2) NOT NULL,
    total_income NUMERIC(19, 2) NOT NULL,
    total_expenses NUMERIC(19, 2) NOT NULL,
    total_paid NUMERIC(19, 2) NOT NULL,
    total_pending NUMERIC(19, 2) NOT NULL,
    final_balance NUMERIC(19, 2) NOT NULL,
    committed_percentage NUMERIC(8, 2) NOT NULL,
    closed_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_resumos_mensais_month_year UNIQUE (month, year)
);

CREATE INDEX idx_salarios_month_year ON salarios(month, year);
CREATE INDEX idx_receitas_month_year ON receitas(month, year);
CREATE INDEX idx_despesas_month_year ON despesas(month, year);
CREATE INDEX idx_despesas_due_date ON despesas(due_date);
CREATE INDEX idx_despesas_status ON despesas(status);
CREATE INDEX idx_categorias_type ON categorias(type);

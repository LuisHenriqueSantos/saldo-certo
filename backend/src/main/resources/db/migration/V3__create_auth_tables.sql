CREATE TABLE app_users (
    id UUID PRIMARY KEY,
    name VARCHAR(140) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tenant_accounts (
    id UUID PRIMARY KEY,
    owner_user_id UUID NOT NULL REFERENCES app_users(id),
    account_name VARCHAR(160) NOT NULL,
    schema_name VARCHAR(63) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE user_tenants (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_users(id),
    tenant_id UUID NOT NULL REFERENCES tenant_accounts(id),
    role VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_user_tenants_user_tenant UNIQUE (user_id, tenant_id)
);

CREATE INDEX idx_app_users_email ON app_users(email);
CREATE INDEX idx_tenant_accounts_owner ON tenant_accounts(owner_user_id);
CREATE INDEX idx_user_tenants_user ON user_tenants(user_id);
CREATE INDEX idx_user_tenants_tenant ON user_tenants(tenant_id);

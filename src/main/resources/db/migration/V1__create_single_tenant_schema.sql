CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    type VARCHAR(30) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_category_created_by FOREIGN KEY (created_by) REFERENCES app_user (id),
    CONSTRAINT uk_category_created_by_name_type UNIQUE (created_by, name, type)
);

CREATE INDEX idx_category_created_by ON category (created_by);

CREATE TABLE expense_transaction (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    category_id UUID NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_transaction_created_by FOREIGN KEY (created_by) REFERENCES app_user (id)
);

CREATE INDEX idx_transaction_created_by ON expense_transaction (created_by);
CREATE INDEX idx_transaction_date ON expense_transaction (transaction_date DESC);

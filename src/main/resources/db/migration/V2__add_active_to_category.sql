ALTER TABLE category
ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

CREATE INDEX idx_category_created_by_active ON category (created_by, active);

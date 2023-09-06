DROP EXTENSION IF EXISTS pgcrypto;
CREATE EXTENSION pgcrypto;

CREATE TABLE IF NOT EXISTS receipt_processor(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    retailer VARCHAR(128) NOT NULL,
    purchase_date TIMESTAMP WITH TIME ZONE NOT NULL,
    purchase_time VARCHAR(64),
    items JSON NOT NULL,
    total VARCHAR(63)
);
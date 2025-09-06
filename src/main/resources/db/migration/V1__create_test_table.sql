CREATE SCHEMA IF NOT EXISTS madres;

CREATE TABLE inquiry (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    zone_id TEXT NOT NULL,
    replied_to BOOLEAN NOT NULL DEFAULT FALSE,
    confirmation_processed BOOLEAN NOT NULL DEFAULT FALSE,
    replied_at TIMESTAMP WITH TIME ZONE,
    confirmation_processed_at TIMESTAMP WITH TIME ZONE,
    data BYTEA NOT NULL
);

-- Indexes for query performance
CREATE INDEX idx_inquiry_replied_to_updated_at
    ON madres.inquiry (replied_to, updated_at);

CREATE INDEX idx_inquiry_confirmation_processed_updated_at
    ON madres.inquiry (confirmation_processed, updated_at);

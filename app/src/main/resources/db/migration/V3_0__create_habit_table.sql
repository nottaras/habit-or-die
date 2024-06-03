CREATE TABLE IF NOT EXISTS habit
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    frequency   VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP(6),
    created_at TIMESTAMP(6)
)

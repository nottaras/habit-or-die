CREATE TABLE IF NOT EXISTS _user
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    firstname  VARCHAR(255) NOT NULL,
    lastname   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    updated_at TIMESTAMP(6),
    created_at TIMESTAMP(6)
)
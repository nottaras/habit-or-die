CREATE TABLE profile
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   INTEGER REFERENCES _user (id) ON DELETE CASCADE,
    firstname VARCHAR(255) NOT NULL,
    lastname   VARCHAR(255) NOT NULL,
    avatar_file_id VARCHAR(24),
    updated_at TIMESTAMP(6),
    created_at TIMESTAMP(6)
);

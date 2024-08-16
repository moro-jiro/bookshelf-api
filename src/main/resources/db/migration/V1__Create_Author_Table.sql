CREATE TABLE author
(
    id         SERIAL PRIMARY KEY,
    last_name  VARCHAR(100)                                              NOT NULL,
    first_name VARCHAR(100)                                              NOT NULL,
    birth_date DATE                                                      NOT NULL,
    gender     VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (last_name, first_name, birth_date)
);

CREATE INDEX idx_author_last_name ON author (last_name);
CREATE INDEX idx_author_first_name ON author (first_name);
CREATE INDEX idx_author_full_name ON author (last_name, first_name);
CREATE INDEX idx_author_birth_date ON author (birth_date);

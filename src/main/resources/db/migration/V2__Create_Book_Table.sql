CREATE TABLE book
(
    id               SERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    publication_date DATE         NOT NULL,
    author_id        INT          NOT NULL,
    publisher        VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES author (id)
);

CREATE INDEX idx_books_title ON book (title);
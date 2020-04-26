CREATE TABLE person (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255),
    last_updated timestamp not null,
    version INT NOT NULL
);

INSERT INTO person VALUES (1, 'peter', NOW(), 1);
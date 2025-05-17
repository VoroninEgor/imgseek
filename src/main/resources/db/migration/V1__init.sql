CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    username varchar,
    password varchar,
    role varchar
);

CREATE TABLE IF NOT EXISTS shop(
    id BIGSERIAL PRIMARY KEY,
    uid uuid,
    name varchar,
    locked boolean,
    callback varchar
);
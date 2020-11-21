DROP TABLE IF EXISTS user_roles cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS roles cascade;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq;

CREATE TABLE users
(
    login    VARCHAR PRIMARY KEY NOT NULL,
    name     VARCHAR             NOT NULL,
    password VARCHAR             NOT NULL
);
CREATE UNIQUE INDEX users_unique_login_idx ON users (login);

CREATE TABLE roles
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    role VARCHAR
);

CREATE TABLE user_roles
(
    login   VARCHAR NOT NULL
        CONSTRAINT user_roles_login_fk
            REFERENCES users
            ON DELETE CASCADE,
    role_id INTEGER NOT NULL
        CONSTRAINT user_roles_roles_id_fk
            REFERENCES roles
            ON DELETE CASCADE,
    CONSTRAINT user_role_pk
        PRIMARY KEY (login, role_id)
);
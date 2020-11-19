DELETE
FROM users;
DELETE
FROM roles;
DELETE
FROM user_roles;
ALTER SEQUENCE global_seq RESTART;

INSERT INTO users (name, login, password)
VALUES ('Petr', 'Ivanov', 'pass'),
       ('Sergey', 'Kolosov', 'pass'),
       ('Andrew', 'Petrov', 'pass');

INSERT INTO roles (role)
VALUES ('Админ'),
       ('Аналитик'),
       ('Оператор');

INSERT INTO user_roles (login, role_id)
VALUES ('Ivanov', 1),
       ('Ivanov', 2),
       ('Kolosov', 1),
       ('Kolosov', 3),
       ('Petrov', 2),
       ('Petrov', 3);

DELETE
FROM users;
DELETE
FROM roles;
DELETE
FROM user_roles;
ALTER SEQUENCE global_seq RESTART;

INSERT INTO users (name, login, password)
VALUES ('Petr', 'Ivanov', 'Pass123'),
       ('Sergey', 'Kolosov', 'Pass123'),
       ('Andrew', 'Petrov', 'Pass123');

INSERT INTO roles (role)
VALUES ('Admin'),
       ('Analyst'),
       ('Operator');

INSERT INTO user_roles (login, role_id)
VALUES ('Ivanov', 1),
       ('Ivanov', 2),
       ('Kolosov', 1),
       ('Kolosov', 3),
       ('Petrov', 2),
       ('Petrov', 3);

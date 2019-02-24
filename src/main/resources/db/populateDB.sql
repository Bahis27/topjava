DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
('2019-02-23 10:01:35','Завтрак', '500', 100000),
('2019-02-23 13:01:35','Обед', '1000', 100000),
('2019-02-23 18:01:35','Ужин', '500', 100000),
('2019-02-24 09:15:35','Завтрак', '1000', 100001),
('2019-02-24 14:01:35','Обед', '500', 100001),
('2019-02-24 19:01:35','Ужин', '510', 100001);
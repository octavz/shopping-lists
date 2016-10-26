# shopping SCHEMA
# --- !Ups

CREATE TABLE user_statuses (
  id          INTEGER PRIMARY KEY,
  description CHARACTER VARYING(100)
);

CREATE TABLE users (
  id             CHARACTER VARYING(40) PRIMARY KEY,
  login          CHARACTER VARYING(255)            NOT NULL UNIQUE,
  nick           CHARACTER VARYING(255)            NOT NULL,
  provider       INTEGER DEFAULT 0                 NOT NULL,
  provider_token CHARACTER VARYING(255),
  last_login     TIMESTAMP,
  status         INTEGER DEFAULT 0                 NOT NULL,
  password       CHARACTER VARYING(100) DEFAULT '' NOT NULL,
  created        TIMESTAMP DEFAULT now()           NOT NULL,
  updated        TIMESTAMP DEFAULT now()           NOT NULL
);

CREATE TABLE user_sessions (
  id      CHARACTER VARYING(254) PRIMARY KEY,
  user_id CHARACTER VARYING NOT NULL REFERENCES users
);

CREATE TABLE clients (
  id           CHARACTER VARYING(254) PRIMARY KEY,
  secret       CHARACTER VARYING(254),
  redirect_uri CHARACTER VARYING(254),
  scope        CHARACTER VARYING(254)
);

CREATE TABLE access_tokens (
  access_token  CHARACTER VARYING(254)  NOT NULL PRIMARY KEY,
  refresh_token CHARACTER VARYING(254),
  user_id       CHARACTER VARYING       NOT NULL REFERENCES users,
  scope         CHARACTER VARYING(254),
  expires_in    INTEGER                 NOT NULL,
  created       TIMESTAMP DEFAULT now() NOT NULL,
  client_id     CHARACTER VARYING       NOT NULL REFERENCES clients
);

CREATE TABLE auth_codes (
  authorization_code CHARACTER VARYING(254) PRIMARY KEY,
  user_id            CHARACTER VARYING      NOT NULL REFERENCES users,
  redirect_uri       CHARACTER VARYING(254),
  created_at         TIMESTAMP              NOT NULL,
  scope              CHARACTER VARYING(254),
  client_id          CHARACTER VARYING(254) NOT NULL REFERENCES clients,
  expires_in         INTEGER                NOT NULL
);

CREATE TABLE grant_types (
  id         INTEGER PRIMARY KEY,
  grant_type CHARACTER VARYING(254) NOT NULL
);

CREATE TABLE client_grant_types (
  client_id     CHARACTER VARYING(254) NOT NULL REFERENCES clients,
  grant_type_id INTEGER                NOT NULL REFERENCES grant_types,
  PRIMARY KEY (client_id, grant_type_id)
);

CREATE TABLE entity_types (
  id          CHARACTER VARYING(40) PRIMARY KEY,
  description CHARACTER VARYING(100)
);

CREATE TABLE list_defs (
  id             CHARACTER VARYING(40) PRIMARY KEY,
  user_id        CHARACTER VARYING(40)   NOT NULL,
  name           CHARACTER VARYING(255)  NOT NULL,
  description    TEXT,
  status         SMALLINT DEFAULT 0      NOT NULL,
  created_client TIMESTAMP DEFAULT now() NOT NULL,
  created        TIMESTAMP DEFAULT now() NOT NULL,
  updated        TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE lists (
  id             CHARACTER VARYING(40)   NOT NULL PRIMARY KEY,
  list_def_id    CHARACTER VARYING(40)   NOT NULL REFERENCES lists,
  user_id        CHARACTER VARYING(40)   NOT NULL REFERENCES users,
  created_client TIMESTAMP DEFAULT now() NOT NULL,
  status         INTEGER DEFAULT 0       NOT NULL,
  created        TIMESTAMP DEFAULT now() NOT NULL,
  updated        TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE lists_users (
  list_def_id CHARACTER VARYING(40) NOT NULL REFERENCES list_defs,
  user_id     CHARACTER VARYING(40) NOT NULL REFERENCES users,
  PRIMARY KEY (list_def_id, user_id)
);

CREATE TABLE labels (
  id             CHARACTER VARYING(40) PRIMARY KEY,
  lang           CHARACTER VARYING(10) NOT NULL,
  entity_id      CHARACTER VARYING(40) NOT NULL,
  entity_type_id CHARACTER VARYING(40) NOT NULL,
  label1         CHARACTER VARYING(1000),
  label2         CHARACTER VARYING(1000),
  label3         CHARACTER VARYING(1000)
);

CREATE TABLE products (
  id          CHARACTER VARYING(40) PRIMARY KEY,
  user_id     CHARACTER VARYING(40)   NOT NULL REFERENCES users,
  name        CHARACTER VARYING(255)  NOT NULL,
  description TEXT,
  status      SMALLINT DEFAULT 0      NOT NULL,
  created     TIMESTAMP DEFAULT now() NOT NULL,
  updated     TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE list_products (
  product_id  CHARACTER VARYING(40) PRIMARY KEY,
  list_id     CHARACTER VARYING(40)   NOT NULL REFERENCES lists,
  user_id     CHARACTER VARYING(40)   NOT NULL REFERENCES users,
  description TEXT,
  quantity    INT,
  bought      SMALLINT DEFAULT 0      NOT NULL,
  status      SMALLINT DEFAULT 0      NOT NULL,
  created     TIMESTAMP DEFAULT now() NOT NULL,
  updated     TIMESTAMP DEFAULT now() NOT NULL
);


INSERT INTO user_statuses (id, description) VALUES (0, 'inactive');
INSERT INTO user_statuses (id, description) VALUES (1, 'active');
INSERT INTO user_statuses (id, description) VALUES (2, 'pending');

INSERT INTO users (id, login, nick, provider, provider_token, last_login, PASSWORD)
VALUES ('1', 'aaa@aaa.com', 'aaa', 0, 'a', '2014-06-05 11:52:16.904', '123456');
INSERT INTO users (id, login, nick, provider, provider_token, last_login, PASSWORD)
VALUES ('2', 'test@test.com', 'bbb', 0, 'b', '2014-06-05 11:52:16.904', '123456');


INSERT INTO lists (id, user_id, NAME, description, status, created, updated)
VALUES ('1', '1', 'list name1', 'description', 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO lists (id, user_id, NAME, description, status, created, updated)
VALUES ('2', '1', 'list name2', 'description', 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO lists (id, user_id, NAME, description, status, created, updated)
VALUES ('3', '2', 'list name3', 'description', 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO entity_types (id, description) VALUES ('1', 'user');
INSERT INTO entity_types (id, description) VALUES ('2', 'list');

INSERT INTO grant_types (id, grant_type) VALUES (1, 'password');
INSERT INTO clients (id, secret) VALUES (1, 'secret');
INSERT INTO client_grant_types VALUES (1, 1);

# --- !Downs

DROP TABLE IF EXISTS labels;
DROP TABLE IF EXISTS list_instance_bought_products;
DROP TABLE IF EXISTS lists_instance;
DROP TABLE IF EXISTS entity_types;
DROP TABLE IF EXISTS client_grant_types;
DROP TABLE IF EXISTS grant_types;
DROP TABLE IF EXISTS list_products;
DROP TABLE IF EXISTS auth_codes;
DROP TABLE IF EXISTS access_tokens;
DROP TABLE IF EXISTS lists_users;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS lists;
DROP TABLE IF EXISTS user_sessions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_statuses;


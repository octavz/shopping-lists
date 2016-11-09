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
  last_login     INTEGER,
  status         INTEGER DEFAULT 0                 NOT NULL,
  password       CHARACTER VARYING(100) DEFAULT '' NOT NULL,
  created        INTEGER DEFAULT 0                 NOT NULL,
  updated        INTEGER DEFAULT 0                 NOT NULL
);

CREATE TABLE user_sessions (
  id      CHARACTER VARYING(254) PRIMARY KEY,
  user_id CHARACTER VARYING NOT NULL REFERENCES users,
  created        INTEGER DEFAULT 0                 NOT NULL,
  updated        INTEGER DEFAULT 0                 NOT NULL
);

CREATE TABLE clients (
  id           CHARACTER VARYING(254) PRIMARY KEY,
  secret       CHARACTER VARYING(254),
  redirect_uri CHARACTER VARYING(254),
  scope        CHARACTER VARYING(254)
);

CREATE TABLE access_tokens (
  access_token  CHARACTER VARYING(254) NOT NULL PRIMARY KEY,
  refresh_token CHARACTER VARYING(254),
  user_id       CHARACTER VARYING      NOT NULL REFERENCES users,
  scope         CHARACTER VARYING(254),
  expires_in    INTEGER                NOT NULL,
  created       INTEGER DEFAULT 0      NOT NULL,
  client_id     CHARACTER VARYING      NOT NULL REFERENCES clients
);

CREATE TABLE auth_codes (
  authorization_code CHARACTER VARYING(254) PRIMARY KEY,
  user_id            CHARACTER VARYING      NOT NULL REFERENCES users,
  redirect_uri       CHARACTER VARYING(254),
  created_at         INTEGER                NOT NULL,
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

CREATE TABLE list_defs (
  id             CHARACTER VARYING(40) PRIMARY KEY,
  user_id        CHARACTER VARYING(40)   NOT NULL,
  name           CHARACTER VARYING(255)  NOT NULL,
  description    TEXT,
  status         SMALLINT DEFAULT 0      NOT NULL,
  created_client INTEGER DEFAULT 0       NOT NULL,
  created        INTEGER DEFAULT 0       NOT NULL,
  updated        INTEGER DEFAULT 0       NOT NULL
);

CREATE TABLE lists_users (
  list_def_id CHARACTER VARYING(40) NOT NULL REFERENCES list_defs,
  user_id     CHARACTER VARYING(40) NOT NULL REFERENCES users,
  PRIMARY KEY (list_def_id, user_id)
);

CREATE TABLE products (
  id          CHARACTER VARYING(40) PRIMARY KEY,
  user_id     CHARACTER VARYING(40)   NOT NULL REFERENCES users,
  name        CHARACTER VARYING(255)  NOT NULL,
  description TEXT,
  status      SMALLINT DEFAULT 0      NOT NULL,
  created     INTEGER DEFAULT 0       NOT NULL,
  updated     INTEGER DEFAULT 0       NOT NULL
);

CREATE TABLE list_def_products (
  product_id  CHARACTER VARYING(40)   NOT NULL  REFERENCES products,
  list_def_id CHARACTER VARYING(40)   NOT NULL  NOT NULL REFERENCES list_defs,
  description TEXT,
  quantity    INT,
  bought      SMALLINT DEFAULT 0      NOT NULL,
  created     INTEGER DEFAULT 0       NOT NULL,
  updated     INTEGER DEFAULT 0       NOT NULL,
  PRIMARY KEY (product_id, list_def_id)
);

CREATE TABLE suppliers (
  id          CHARACTER VARYING(40) PRIMARY KEY,
  name        CHARACTER VARYING(255) NOT NULL,
  description TEXT,
  created     INTEGER DEFAULT 0       NOT NULL,
  updated     INTEGER DEFAULT 0       NOT NULL
);

CREATE TABLE product_prices (
  user_id     CHARACTER VARYING(40)    NOT NULL REFERENCES users,
  product_id  CHARACTER VARYING(40)    NOT NULL REFERENCES products,
  supplier_id CHARACTER VARYING(40)    NOT NULL REFERENCES suppliers,
  price       NUMERIC(10, 2) DEFAULT 0 NOT NULL,
  created     INTEGER DEFAULT 0       NOT NULL,
  updated     INTEGER DEFAULT 0       NOT NULL,
  PRIMARY KEY (product_id, supplier_id)
);


INSERT INTO user_statuses (id, description) VALUES (0, 'inactive');
INSERT INTO user_statuses (id, description) VALUES (1, 'active');
INSERT INTO user_statuses (id, description) VALUES (2, 'pending');

INSERT INTO users (id, login, nick, provider, provider_token, last_login, PASSWORD)
VALUES ('1', 'aaa@aaa.com', 'aaa', 0, 'a', 1477550565, '123456');
INSERT INTO users (id, login, nick, provider, provider_token, last_login, PASSWORD)
VALUES ('2', 'test@test.com', 'bbb', 0, 'b', 1477550565, '123456');


INSERT INTO list_defs (id, user_id, name, description, status, created, updated)
VALUES ('1', '1', 'list name1', 'description', 0, 1477550565, 1477550565);
INSERT INTO list_defs (id, user_id, name, description, status, created, updated)
VALUES ('2', '1', 'list name2', 'description', 0, 1477550565, 1477550565);
INSERT INTO list_defs (id, user_id, name, description, status, created, updated)
VALUES ('3', '2', 'list name3', 'description', 0, 1477550565, 1477550565);

INSERT INTO grant_types (id, grant_type) VALUES (1, 'password');
INSERT INTO clients (id, secret) VALUES (1, 'secret');
INSERT INTO client_grant_types VALUES (1, 1);

# --- !Downs

DROP TABLE IF EXISTS client_grant_types;
DROP TABLE IF EXISTS grant_types;
DROP TABLE IF EXISTS list_def_products;
DROP TABLE IF EXISTS auth_codes;
DROP TABLE IF EXISTS access_tokens;
DROP TABLE IF EXISTS lists_users;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS product_prices;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS list_defs;
DROP TABLE IF EXISTS user_sessions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_statuses;
DROP TABLE IF EXISTS suppliers;




# --- !Ups

CREATE TABLE user_statuses (
    id INTEGER PRIMARY KEY,
    description CHARACTER VARYING(100)
);

CREATE TABLE verbs (
    id INTEGER PRIMARY KEY,
    description CHARACTER VARYING(100)
);

CREATE TABLE users (
    id CHARACTER VARYING(40) PRIMARY KEY,
    login CHARACTER VARYING(255) NOT NULL UNIQUE,
    nick CHARACTER VARYING(255) NOT NULL UNIQUE,
    provider INTEGER DEFAULT 0 NOT NULL,
    provider_token CHARACTER VARYING(255),
    last_login TIMESTAMP WITHOUT TIME ZONE,
    status INTEGER DEFAULT 0 NOT NULL,
    password CHARACTER VARYING(100) DEFAULT '' NOT NULL,
    user_id CHARACTER VARYING(40) ,
    group_id CHARACTER VARYING(40),
    perm INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE user_sessions (
    id CHARACTER VARYING(254) PRIMARY KEY,
    user_id CHARACTER VARYING NOT NULL REFERENCES users
);

CREATE TABLE projects (
    id CHARACTER VARYING(40) PRIMARY KEY,
    user_id CHARACTER VARYING(40) NOT NULL,
    name CHARACTER VARYING(255) NOT NULL,
    description TEXT,
    parent_id CHARACTER VARYING(40),
    status SMALLINT DEFAULT 0 NOT NULL,
    perm INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    UNIQUE(user_id, NAME)
);

CREATE TABLE groups (
    id CHARACTER VARYING(40) PRIMARY KEY,
    project_id CHARACTER VARYING(40) NOT NULL REFERENCES projects,
    type SMALLINT DEFAULT 0 NOT NULL,
    name CHARACTER VARYING(200) NOT NULL,
    user_id CHARACTER VARYING(40) NOT NULL REFERENCES users,
    group_id CHARACTER VARYING(40) REFERENCES groups,
    perm INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    perm_project int DEFAULT 1 NOT NULL
);

CREATE TABLE clients (
    id CHARACTER VARYING(254) PRIMARY KEY,
    secret CHARACTER VARYING(254),
    redirect_uri CHARACTER VARYING(254),
    scope CHARACTER VARYING(254)
);

CREATE TABLE access_tokens (
    access_token CHARACTER VARYING(254) NOT NULL PRIMARY KEY,
    refresh_token CHARACTER VARYING(254),
    user_id CHARACTER VARYING NOT NULL REFERENCES users,
    scope CHARACTER VARYING(254),
    expires_in INTEGER NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    client_id CHARACTER VARYING NOT NULL REFERENCES clients
);

CREATE TABLE actions (
    id CHARACTER VARYING(40) NOT NULL PRIMARY KEY,
    description CHARACTER VARYING(100),
    url CHARACTER VARYING(2000) NOT NULL,
    verb_id INTEGER NOT NULL REFERENCES verbs,
    secured SMALLINT NOT NULL,
    user_id CHARACTER VARYING(40) NOT NULL REFERENCES users,
    group_id CHARACTER VARYING(40) NOT NULL REFERENCES groups,
    perm  INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    UNIQUE (url, verb_id)
);

CREATE TABLE auth_codes (
    authorization_code CHARACTER VARYING(254) PRIMARY KEY,
    user_id CHARACTER VARYING NOT NULL REFERENCES users,
    redirect_uri CHARACTER VARYING(254),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    scope CHARACTER VARYING(254),
    client_id CHARACTER VARYING(254) NOT NULL REFERENCES clients,
    expires_in INTEGER NOT NULL
);

CREATE TABLE grant_types (
    id INTEGER PRIMARY KEY,
    grant_type CHARACTER VARYING(254) NOT NULL
);

CREATE TABLE client_grant_types (
    client_id CHARACTER VARYING(254) NOT NULL REFERENCES clients,
    grant_type_id INTEGER NOT NULL REFERENCES grant_types,
    PRIMARY KEY (client_id, grant_type_id)
);

CREATE TABLE entity_types (
    id CHARACTER VARYING(40) PRIMARY KEY,
    description CHARACTER VARYING(100)
);

CREATE TABLE groups_users (
    group_id CHARACTER VARYING(40) NOT NULL REFERENCES groups,
    user_id CHARACTER VARYING(40) NOT NULL REFERENCES users,
    PRIMARY KEY (group_id, user_id)
);

CREATE TABLE labels (
    id CHARACTER VARYING(40) PRIMARY KEY,
    lang CHARACTER VARYING(10) NOT NULL,
    entity_id CHARACTER VARYING(40) NOT NULL,
    entity_type_id CHARACTER VARYING(40) NOT NULL,
    label1 CHARACTER VARYING(1000),
    label2 CHARACTER VARYING(1000),
    label3 CHARACTER VARYING(1000)
);

CREATE TABLE resources (
    id CHARACTER VARYING(40) PRIMARY KEY,
    content CHARACTER VARYING(1000),
    entity_type_id CHARACTER VARYING(40) NOT NULL,
    user_id CHARACTER VARYING(40) NOT NULL REFERENCES users,
    group_id CHARACTER VARYING(40) NOT NULL REFERENCES groups,
    perm INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE tasks (
    id CHARACTER VARYING(40) PRIMARY KEY,
    project_id CHARACTER VARYING(40) NOT NULL REFERENCES projects,
    user_id CHARACTER VARYING(40) NOT NULL REFERENCES users,
    group_id CHARACTER VARYING(40) NOT NULL REFERENCES groups,
    subject CHARACTER VARYING(255) NOT NULL,
    description TEXT,
    parent_id CHARACTER VARYING(40) REFERENCES tasks,
    status SMALLINT DEFAULT 0 NOT NULL,
    perm INT DEFAULT 448 NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL
);

INSERT INTO verbs(id, description) VALUES (1, 'get');
INSERT INTO verbs(id, description) VALUES (2, 'post');
INSERT INTO verbs(id, description) VALUES (3, 'put');
INSERT INTO verbs(id, description) VALUES (4, 'delete');

INSERT INTO user_statuses(id, description) VALUES (0, 'inactive');
INSERT INTO user_statuses(id, description) VALUES (1, 'active');

INSERT INTO users(id, login, nick, provider, provider_token, last_login, user_id, group_id, perm, PASSWORD) VALUES ('1', 'aaa@aaa.com', 'aaa', 0, 'a', '2014-06-05 11:52:16.904', NULL, NULL, 484,'123456');
INSERT INTO users(id, login, nick, provider, provider_token, last_login, user_id, group_id, perm, PASSWORD) VALUES ('2', 'test@test.com', 'bbb', 0, 'b', '2014-06-05 11:52:16.904', NULL, NULL, 484,'123456');

INSERT INTO projects(id,user_id, NAME, description, parent_id, status, perm, created, updated) VALUES ('1', '1', 'project name1', 'description', NULL, 0, 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO projects(id,user_id, NAME, description, parent_id, status, perm, created, updated) VALUES ('2', '1', 'project name2', 'description', NULL, 0, 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO projects(id,user_id, NAME, description, parent_id, status, perm, created, updated) VALUES ('3', '1', 'project name3', 'description', NULL, 0, 0, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('1', '1', 1, 'admin','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('11', '1', 2, 'users','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('2', '2', 1, 'admin','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('21', '2', 2, 'users','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('3', '3', 1, 'admin','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO groups(id, project_id, TYPE, NAME, user_id, group_id, perm, created, updated) VALUES ('32', '3', 2, 'users','1', NULL, 448, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO groups_users(group_id, user_id) VALUES ('1', '1');
INSERT INTO groups_users(group_id, user_id) VALUES ('11', '1');
INSERT INTO groups_users(group_id, user_id) VALUES ('2', '1');
INSERT INTO groups_users(group_id, user_id) VALUES ('21', '1');

INSERT INTO actions(id, description, url, verb_id, secured, user_id, group_id, perm, created, updated) VALUES ('1', 'login', '/login', 1, 0, '1', '1', 480, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

INSERT INTO entity_types(id, description) VALUES ('1', 'user');
INSERT INTO entity_types(id, description) VALUES ('2', 'project');
INSERT INTO entity_types(id, description) VALUES ('3', 'actions');
INSERT INTO entity_types(id, description) VALUES ('4', 'client routes');

INSERT INTO resources(id, CONTENT, entity_type_id, user_id, group_id, perm, created,updated) VALUES ('c8377c44be3db57b5478f9a1eb9c4803', 'route1', '4', '1', '1',  500, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources(id, CONTENT, entity_type_id, user_id, group_id, perm, created,updated) VALUES ('c70cb6f0a4d483ec9a04720d3b512211', 'route2', '4', '1', '1',  500, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources(id, CONTENT, entity_type_id, user_id, group_id, perm, created,updated) VALUES ('238d7475ace7128fa5395240eb6d8fe9', 'route3', '4', '1', '1',  500, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources(id, CONTENT, entity_type_id, user_id, group_id, perm, created,updated) VALUES ('27a2f1042f8cc4948b554d146300cf79', 'route5', '4', '1', '1',  500, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');
INSERT INTO resources(id, CONTENT, entity_type_id, user_id, group_id, perm, created,updated) VALUES ('2ef802d042a9b0d12d8853f731a14ec8', 'route5', '4', '1', '1',  500, '2014-06-05 11:52:16.904', '2014-06-05 11:52:16.904');

insert into grant_types(id, grant_type) values(1, 'password');
insert into clients(id, secret) values(1, 'secret');
insert into client_grant_types values(1,1);

# --- !Downs

DROP TABLE resources;
DROP TABLE labels;
DROP TABLE groups_users;
DROP TABLE entity_types;
DROP TABLE client_grant_types;
DROP TABLE grant_types;
DROP TABLE auth_codes;
DROP TABLE actions;
DROP TABLE access_tokens;
DROP TABLE clients;
DROP TABLE groups;
DROP TABLE tasks;
DROP TABLE projects;
DROP TABLE user_sessions;
DROP TABLE users;
DROP TABLE verbs;
DROP TABLE user_statuses;

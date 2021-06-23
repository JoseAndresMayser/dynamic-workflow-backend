/* 1 */

DROP TABLE IF EXISTS roles CASCADE;

DROP TABLE IF EXISTS actions CASCADE;

DROP TABLE IF EXISTS role_actions CASCADE;

CREATE TABLE roles
(
	id serial NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(256) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE actions
(
	id serial NOT NULL,
	code varchar(256) NOT NULL,
	description varchar(256) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE role_actions
(
	id serial NOT NULL,
	role_id int NOT NULL,
	action_id int NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (role_id) REFERENCES roles(id),
	FOREIGN KEY (action_id) REFERENCES actions(id)
);

INSERT INTO roles(name, description) VALUES('ADMINISTRATOR', 'Administrador del sistema');
INSERT INTO roles(name, description) VALUES('DEPARTMENT_BOSS', 'Jefe de departamento designado');
INSERT INTO roles(name, description) VALUES('ANALYST', 'Analista de solicitudes');
INSERT INTO roles(name, description) VALUES('REQUESTING_USER', 'Usuario solicitante');


INSERT INTO actions(code, description) VALUES('ACCESS_LOG_IN', 'Iniciar sesión');
INSERT INTO actions(code, description) VALUES('ACCESS_PASSWORD_RESTORE', 'Restaurar contraseña');
INSERT INTO actions(code, description) VALUES('ACCESS_PASSWORD_UPDATE', 'Actualizar contraseña');

INSERT INTO actions(code, description) VALUES('ROLE_REGISTER', 'Registrar rol');
INSERT INTO actions(code, description) VALUES('ROLE_UPDATE', 'Actualizar rol');
INSERT INTO actions(code, description) VALUES('ROLE_GET', 'Ver detalle de rol');
INSERT INTO actions(code, description) VALUES('ROLE_GET_ALL', 'Ver todos los roles');
INSERT INTO actions(code, description) VALUES('ROLE_ACTIONS_GET', 'Ver acciones de rol');

INSERT INTO actions(code, description) VALUES('USER_REGISTER_REQUESTING', 'Registrarse como solicitante');
INSERT INTO actions(code, description) VALUES('USER_REGISTER', 'Registrar usuario');
INSERT INTO actions(code, description) VALUES('USER_UPDATE', 'Actualizar usuario');
INSERT INTO actions(code, description) VALUES('USER_CURRENT_UPDATE', 'Actualizar perfil');
INSERT INTO actions(code, description) VALUES('USER_GET', 'Ver detalle de usuario');
INSERT INTO actions(code, description) VALUES('USER_CURRENT_GET', 'Ver perfil');
INSERT INTO actions(code, description) VALUES('USER_GET_ALL', 'Ver todos los usuarios');


INSERT INTO role_actions(role_id, action_id) VALUES(1, 3);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 4);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 5);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 6);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 7);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 8);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 10);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 11);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 12);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 13);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 14);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 15);

INSERT INTO role_actions(role_id, action_id) VALUES(2, 3);
INSERT INTO role_actions(role_id, action_id) VALUES(2, 12);
INSERT INTO role_actions(role_id, action_id) VALUES(2, 14);

INSERT INTO role_actions(role_id, action_id) VALUES(3, 3);
INSERT INTO role_actions(role_id, action_id) VALUES(3, 12);
INSERT INTO role_actions(role_id, action_id) VALUES(3, 14);

INSERT INTO role_actions(role_id, action_id) VALUES(4, 3);
INSERT INTO role_actions(role_id, action_id) VALUES(4, 12);
INSERT INTO role_actions(role_id, action_id) VALUES(4, 14);


/* 2 */

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS user_actions CASCADE;

CREATE TABLE users
(
	id serial NOT NULL,
	username varchar(50) NOT NULL,
	password varchar(256) NOT NULL,
	status varchar(50) NOT NULL,
	creation_date timestamp NOT NULL,
	last_modified_date timestamp NOT NULL,
	names varchar(256) NOT NULL,
	first_surname varchar(256) NOT NULL,
	second_surname varchar(256),
	email varchar(256) NOT NULL,
	phone_number varchar(50) NOT NULL,
	code varchar(50),
	PRIMARY KEY (id)
);

CREATE TABLE user_actions
(
	id serial NOT NULL,
	user_id int NOT NULL,
	action_id int NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (action_id) REFERENCES actions(id)
);

INSERT INTO users(username, password, status, creation_date, last_modified_date, names, first_surname, second_surname, email, phone_number) 
VALUES ('admin', '$2a$12$8ORLl44NoA72B3dbotKwXO4kqmNDIGE/wimvuo18s6DLU5SDoscjG', 'ENABLED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DYNAMIC', 'WORKFLOW', 'ADMINISTRATOR', 'dynamic.workflow.uagrm@gmail.com', '+59112345678');

INSERT INTO user_actions(user_id, action_id) VALUES(1, 3);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 4);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 5);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 6);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 7);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 8);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 10);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 11);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 12);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 13);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 14);
INSERT INTO user_actions(user_id, action_id) VALUES(1, 15);

DROP TABLE IF EXISTS events CASCADE;

CREATE TABLE events
(
	id serial NOT NULL,
	execution_date timestamp NOT NULL,
	affected_entity_id int,
	user_id int NOT NULL,
	action_id int NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (action_id) REFERENCES actions(id)
);
/* DROP TABLES IF EXISTS */

DROP TABLE IF EXISTS roles CASCADE;

DROP TABLE IF EXISTS actions CASCADE;

DROP TABLE IF EXISTS role_actions CASCADE;

/* CREATE TABLES */

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

/* INITIAL DATA */

INSERT INTO roles(name, description) VALUES('ADMINISTRATOR', 'Administrador del sistema');
INSERT INTO roles(name, description) VALUES('DEPARTMENT_BOSS', 'Jefe de departamento designado');
INSERT INTO roles(name, description) VALUES('ANALYST', 'Analista de solicitudes');
INSERT INTO roles(name, description) VALUES('REQUESTING_USER', 'Usuario solicitante');

INSERT INTO actions(code, description) VALUES('ROLE_REGISTER', 'Registrar nuevo rol');
INSERT INTO actions(code, description) VALUES('ROLE_UPDATE', 'Actualizar rol');
INSERT INTO actions(code, description) VALUES('ROLE_ACTIONS_UPDATE', 'Actualizar acciones de rol');
INSERT INTO actions(code, description) VALUES('ROLE_GET', 'Ver detalle de rol');
INSERT INTO actions(code, description) VALUES('ROLE_GET_ALL', 'Ver todos los roles');
INSERT INTO actions(code, description) VALUES('ROLE_ACTIONS_GET', 'Ver acciones de rol');
INSERT INTO actions(code, description) VALUES('USER_LOG_IN', 'Iniciar sesión');
INSERT INTO actions(code, description) VALUES('USER_REGISTER', 'Registrar nuevo usuario');
INSERT INTO actions(code, description) VALUES('USER_REGISTER_REQUESTING', 'Regsitro como solicitante');
INSERT INTO actions(code, description) VALUES('USER_PASSWORD_RESTORE', 'Restaurar contraseña');
INSERT INTO actions(code, description) VALUES('USER_PASSWORD_UPDATE', 'Actualizar contraseña');

INSERT INTO role_actions(role_id, action_id) VALUES(1, 1);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 2);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 3);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 4);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 5);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 6);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 8);
INSERT INTO role_actions(role_id, action_id) VALUES(1, 11);

INSERT INTO role_actions(role_id, action_id) VALUES(2, 11);

INSERT INTO role_actions(role_id, action_id) VALUES(3, 11);

INSERT INTO role_actions(role_id, action_id) VALUES(4, 11);
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
/* 1 - ROLES */

DROP TABLE IF EXISTS roles CASCADE;

DROP TABLE IF EXISTS actions CASCADE;

DROP TABLE IF EXISTS role_actions CASCADE;

CREATE TABLE roles
(
    id          SERIAL       NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(256) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE actions
(
    id          SERIAL       NOT NULL,
    code        VARCHAR(256) NOT NULL,
    description VARCHAR(256) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE role_actions
(
    id        SERIAL NOT NULL,
    role_id   INT    NOT NULL,
    action_id INT    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (action_id) REFERENCES actions (id)
);

INSERT INTO roles(name, description)
VALUES ('ADMINISTRATOR', 'Administrador del sistema');
INSERT INTO roles(name, description)
VALUES ('DEPARTMENT_BOSS', 'Jefe de departamento designado');
INSERT INTO roles(name, description)
VALUES ('ANALYST', 'Analista de solicitudes');
INSERT INTO roles(name, description)
VALUES ('REQUESTING_USER', 'Usuario solicitante');


INSERT INTO actions(code, description)
VALUES ('ACCESS_LOG_IN', 'Iniciar sesión');
INSERT INTO actions(code, description)
VALUES ('ACCESS_PASSWORD_RESTORE', 'Restaurar contraseña');
INSERT INTO actions(code, description)
VALUES ('ACCESS_PASSWORD_UPDATE', 'Actualizar contraseña');

INSERT INTO actions(code, description)
VALUES ('ROLE_REGISTER', 'Registrar rol');
INSERT INTO actions(code, description)
VALUES ('ROLE_UPDATE', 'Actualizar rol');
INSERT INTO actions(code, description)
VALUES ('ROLE_GET_ALL', 'Ver todos los roles');
INSERT INTO actions(code, description)
VALUES ('ROLE_DELETE', 'Eliminar rol');


INSERT INTO actions(code, description)
VALUES ('USER_REGISTER_REQUESTING', 'Registrarse como solicitante');
INSERT INTO actions(code, description)
VALUES ('USER_REGISTER', 'Registrar usuario');
INSERT INTO actions(code, description)
VALUES ('USER_UPDATE', 'Actualizar usuario');
INSERT INTO actions(code, description)
VALUES ('USER_CURRENT_UPDATE', 'Actualizar perfil');
INSERT INTO actions(code, description)
VALUES ('USER_GET_ALL', 'Ver todos los usuarios');

/* ID de acciones sin autenticación: 1, 2, 11 */
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 3);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 4);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 5);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 6);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 7);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 9);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 10);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 11);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 12);

INSERT INTO role_actions(role_id, action_id)
VALUES (2, 3);
INSERT INTO role_actions(role_id, action_id)
VALUES (2, 11);

INSERT INTO role_actions(role_id, action_id)
VALUES (3, 3);
INSERT INTO role_actions(role_id, action_id)
VALUES (3, 11);

INSERT INTO role_actions(role_id, action_id)
VALUES (4, 3);
INSERT INTO role_actions(role_id, action_id)
VALUES (4, 11);


/* 2 - USERS */

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS user_actions CASCADE;

CREATE TABLE users
(
    id                     SERIAL                   NOT NULL,
    username               VARCHAR(50)              NOT NULL,
    password               VARCHAR(256)             NOT NULL,
    status                 VARCHAR(50)              NOT NULL,
    creation_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    names                  VARCHAR(256)             NOT NULL,
    first_surname          VARCHAR(256)             NOT NULL,
    second_surname         VARCHAR(256),
    email                  VARCHAR(320)             NOT NULL,
    phone                  VARCHAR(50)              NOT NULL,
    identification_number  INT,
    photo_path             VARCHAR(512),
    code                   VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE user_actions
(
    id        SERIAL NOT NULL,
    user_id   INT    NOT NULL,
    action_id INT    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (action_id) REFERENCES actions (id)
);

INSERT INTO users(username, password, status, creation_timestamp, modification_timestamp, names, first_surname,
                  second_surname, email, phone, identification_number)
VALUES ('admin', '$2a$12$8ORLl44NoA72B3dbotKwXO4kqmNDIGE/wimvuo18s6DLU5SDoscjG', 'ENABLED', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 'DYNAMIC', 'WORKFLOW', 'ADMINISTRATOR', 'dynamic.workflow.uagrm@gmail.com', '+59178030122',
        123456);

INSERT INTO user_actions(user_id, action_id)
VALUES (1, 3);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 4);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 5);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 6);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 7);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 9);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 10);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 11);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 12);


/* 3 - DEPARTMENTS */

DROP TABLE IF EXISTS departments CASCADE;

DROP TABLE IF EXISTS department_members CASCADE;

CREATE TABLE departments
(
    id                     SERIAL                   NOT NULL,
    name                   VARCHAR(256)             NOT NULL,
    contact_email          VARCHAR(320)             NOT NULL,
    contact_phone          VARCHAR(50)              NOT NULL,
    location               VARCHAR(256)             NOT NULL,
    creation_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    status                 VARCHAR(50)              NOT NULL,
    parent_department_id   INT,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_department_id) REFERENCES departments (id)
);

CREATE TABLE department_members
(
    id                   SERIAL                   NOT NULL,
    is_department_boss   BOOLEAN                  NOT NULL,
    assignment_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active            BOOLEAN                  NOT NULL,
    user_id              INT                      NOT NULL,
    department_id        INT                      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

INSERT INTO actions(code, description)
VALUES ('DEPARTMENT_REGISTER', 'Registrar departamento');
INSERT INTO actions(code, description)
VALUES ('DEPARTMENT_UPDATE', 'Actualizar departamento');
INSERT INTO actions(code, description)
VALUES ('DEPARTMENT_UPDATE_MEMBERS', 'Actualizar miembros de departamento');
INSERT INTO actions(code, description)
VALUES ('DEPARTMENT_GET', 'Ver detalle de departamento');
INSERT INTO actions(code, description)
VALUES ('DEPARTMENT_GET_ALL', 'Ver todos los departamentos');


INSERT INTO role_actions(role_id, action_id)
VALUES (1, 13);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 14);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 15);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 16);
INSERT INTO role_actions(role_id, action_id)
VALUES (1, 17);


INSERT INTO role_actions(role_id, action_id)
VALUES (2, 13);
INSERT INTO role_actions(role_id, action_id)
VALUES (2, 14);
INSERT INTO role_actions(role_id, action_id)
VALUES (2, 15);
INSERT INTO role_actions(role_id, action_id)
VALUES (2, 16);
INSERT INTO role_actions(role_id, action_id)
VALUES (2, 17);


INSERT INTO user_actions(user_id, action_id)
VALUES (1, 13);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 14);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 15);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 16);
INSERT INTO user_actions(user_id, action_id)
VALUES (1, 17);


/* 4 - PROCESSES */

DROP TABLE IF EXISTS processes CASCADE;

DROP TABLE IF EXISTS process_schemas CASCADE;

DROP TABLE IF EXISTS process_activations CASCADE;

DROP TABLE IF EXISTS input_types CASCADE;

DROP TABLE IF EXISTS restrictions CASCADE;

DROP TABLE IF EXISTS restriction_defined_values CASCADE;

DROP TABLE IF EXISTS inputs CASCADE;

DROP TABLE IF EXISTS input_restrictions CASCADE;

DROP TABLE IF EXISTS input_restriction_defined_values CASCADE;

DROP TABLE IF EXISTS input_restriction_values CASCADE;

DROP TABLE IF EXISTS stages CASCADE;

DROP TABLE IF EXISTS stage_analysts CASCADE;

DROP TABLE IF EXISTS selection_input_values CASCADE;

DROP TABLE IF EXISTS trigger_sequences CASCADE;

CREATE TABLE processes
(
    id                     SERIAL                   NOT NULL,
    name                   VARCHAR(50)              NOT NULL,
    description            VARCHAR(256),
    creation_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    status                 VARCHAR(50)              NOT NULL,
    user_id                INT                      NOT NULL,
    department_id          INT                      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE process_schemas
(
    id                 SERIAL                   NOT NULL,
    is_active          BOOLEAN                  NOT NULL,
    creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    has_trigger        BOOLEAN                  NOT NULL,
    process_id         INT                      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (process_id) REFERENCES processes (id)
);

CREATE TABLE process_activations
(
    id                SERIAL  NOT NULL,
    is_indeterminate  BOOLEAN NOT NULL,
    is_active         BOOLEAN NOT NULL,
    start_timestamp   TIMESTAMP WITH TIME ZONE,
    finish_timestamp  TIMESTAMP WITH TIME ZONE,
    process_schema_id INT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (process_schema_id) REFERENCES process_schemas (id)
);

CREATE TABLE input_types
(
    id          SERIAL       NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(256) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE restrictions
(
    id                 SERIAL      NOT NULL,
    name               VARCHAR(50) NOT NULL,
    has_defined_values BOOLEAN     NOT NULL,
    input_type_id      INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (input_type_id) REFERENCES input_types (id)
);

CREATE TABLE restriction_defined_values
(
    id             SERIAL       NOT NULL,
    value          VARCHAR(128) NOT NULL,
    restriction_id INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restriction_id) REFERENCES restrictions (id)
);

CREATE TABLE inputs
(
    id                SERIAL      NOT NULL,
    name              VARCHAR(50) NOT NULL,
    description       VARCHAR(256),
    is_mandatory      BOOLEAN     NOT NULL,
    is_trigger        BOOLEAN     NOT NULL,
    process_schema_id INT         NOT NULL,
    input_type_id     INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (process_schema_id) REFERENCES process_schemas (id),
    FOREIGN KEY (input_type_id) REFERENCES input_types (id)
);

CREATE TABLE input_restrictions
(
    id             SERIAL       NOT NULL,
    value          VARCHAR(256) NOT NULL,
    input_id       INT          NOT NULL,
    restriction_id INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (input_id) REFERENCES inputs (id),
    FOREIGN KEY (restriction_id) REFERENCES restrictions (id)
);

CREATE TABLE stages
(
    id                   SERIAL      NOT NULL,
    name                 VARCHAR(50) NOT NULL,
    description          VARCHAR(256),
    approvals_required   SMALLINT    NOT NULL,
    has_conditional      BOOLEAN     NOT NULL,
    stage_index          SMALLINT    NOT NULL,
    previous_stage_index SMALLINT,
    next_stage_index     SMALLINT,
    previous_stage_id    INT,
    next_stage_id        INT,
    process_schema_id    INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (previous_stage_id) REFERENCES stages (id),
    FOREIGN KEY (next_stage_id) REFERENCES stages (id),
    FOREIGN KEY (process_schema_id) REFERENCES process_schemas (id)
);

CREATE TABLE stage_analysts
(
    id                    SERIAL  NOT NULL,
    requires_approval     BOOLEAN NOT NULL,
    approval_is_mandatory BOOLEAN NOT NULL,
    stage_id              INT     NOT NULL,
    department_member_id  INT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (stage_id) REFERENCES stages (id),
    FOREIGN KEY (department_member_id) REFERENCES department_members (id)
);

CREATE TABLE selection_input_values
(
    id       SERIAL       NOT NULL,
    value    VARCHAR(128) NOT NULL,
    input_id INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (input_id) REFERENCES inputs (id)
);

CREATE TABLE trigger_sequences
(
    id                       SERIAL   NOT NULL,
    has_next_stage           BOOLEAN  NOT NULL,
    current_stage_index      SMALLINT NOT NULL,
    next_stage_index         SMALLINT,
    selection_input_value_id INT      NOT NULL,
    current_stage_id         INT      NOT NULL,
    next_stage_id            INT,
    PRIMARY KEY (id),
    FOREIGN KEY (selection_input_value_id) REFERENCES selection_input_values (id),
    FOREIGN KEY (current_stage_id) REFERENCES stages (id),
    FOREIGN KEY (next_stage_id) REFERENCES stages (id)
);

INSERT INTO actions(code, description)
VALUES ('PROCESS_CREATE', 'Crear proceso');

INSERT INTO role_actions(role_id, action_id)
VALUES (1, 18);

INSERT INTO user_actions(user_id, action_id)
VALUES (1, 18);

INSERT INTO input_types(name, description)
VALUES ('TEXT', 'Campo de texto simple');

INSERT INTO input_types(name, description)
VALUES ('MULTIPLE_CHOICE', 'Casillas de opción múltiple');

INSERT INTO input_types(name, description)
VALUES ('SELECTION_BOX', 'Casillas de selección');

INSERT INTO input_types(name, description)
VALUES ('DEPLOYABLE_LIST', 'Lista desplegable');

INSERT INTO input_types(name, description)
VALUES ('UPLOAD_FILE', 'Cargado de archivo');

INSERT INTO input_types(name, description)
VALUES ('DATE', 'Campo de fecha');

INSERT INTO actions(code, description)
VALUES ('PROCESS_GET_ALL', 'Ver todos los procesos');

INSERT INTO role_actions(role_id, action_id)
VALUES (1, 19);

INSERT INTO user_actions(user_id, action_id)
VALUES (1, 19);

INSERT INTO actions(code, description)
VALUES ('REQUEST_REGISTER', 'Realizar nueva solicitud');

INSERT INTO role_actions(role_id, action_id)
VALUES (1, 20);

INSERT INTO user_actions(user_id, action_id)
VALUES (1, 20);


/* 5 - REQUESTS */

DROP TABLE IF EXISTS requests CASCADE;

DROP TABLE IF EXISTS request_input_values CASCADE;

DROP TABLE IF EXISTS request_stages CASCADE;

CREATE TABLE requests
(
    id                 SERIAL                   NOT NULL,
    shipping_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    finish_timestamp   TIMESTAMP WITH TIME ZONE,
    status             VARCHAR(50)              NOT NULL,
    code               VARCHAR(50),
    form_path          VARCHAR(512),
    process_id         INT                      NOT NULL,
    user_id            INT                      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (process_id) REFERENCES processes (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE request_input_values
(
    id         SERIAL NOT NULL,
    value      TEXT   NOT NULL,
    input_id   INT    NOT NULL,
    request_id INT    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (input_id) REFERENCES inputs (id),
    FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE request_stages
(
    id               SERIAL                   NOT NULL,
    entry_timestamp  TIMESTAMP WITH TIME ZONE NOT NULL,
    finish_timestamp TIMESTAMP WITH TIME ZONE,
    status           VARCHAR(50)              NOT NULL,
    request_id       INT                      NOT NULL,
    stage_id         INT                      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (request_id) REFERENCES requests (id),
    FOREIGN KEY (stage_id) REFERENCES stages (id)
);
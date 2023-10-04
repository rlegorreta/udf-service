DROP EXTENSION IF EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS microservices (
    id UUID DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    active boolean NOT NULL,
    author varchar(40) NOT NULL,
    creation_Date date NOT NULL,
    modification_Date date NOT NULL,
    PRIMARY KEY(id)
    );

CREATE TABLE IF NOT EXISTS udfs (
    id UUID DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    active boolean NOT NULL,
    json varchar(2048) NOT NULL,
    blockly_Blocks varchar(5120) NOT NULL,
    author varchar(40) NOT NULL,
    creation_Date date NOT NULL,
    modification_Date date NOT NULL,
    PRIMARY KEY(id)
    );


CREATE TABLE IF NOT EXISTS udf_fields (
    id UUID DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    type varchar(100) NOT NULL,
    id_udf UUID NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (id_udf) REFERENCES udfs(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS datasources (
    id UUID DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    id_microservice UUID NOT NULL,
    json varchar(2048) NOT NULL,
    blockly_Blocks varchar(5120) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (id_microservice) REFERENCES microservices(id)
);

CREATE TABLE IF NOT EXISTS datasource_fields (
    id UUID DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    type varchar(100) NOT NULL,
    id_datasource UUID NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (id_datasource) REFERENCES datasources(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mapping (
    id UUID DEFAULT uuid_generate_v4(),
    datasource varchar(255) NOT NULL,
    mapping varchar(2048) NOT NULL,
    microservice varchar(255) NOT NULL,
    id_udf UUID NOT NULL,
    code varchar(2048) NOT NULL,
    blockly_Blocks varchar(5120) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (id_udf) REFERENCES udfs(id) ON DELETE CASCADE
);



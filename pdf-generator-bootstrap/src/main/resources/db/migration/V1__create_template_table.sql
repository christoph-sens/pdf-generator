CREATE SEQUENCE template_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE template (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('template_id_seq'),
    name VARCHAR(255) NOT NULL,
    country_code CHAR(2) NOT NULL,
    content TEXT,
    constraint unique_name_country_code UNIQUE (name, country_code)
);
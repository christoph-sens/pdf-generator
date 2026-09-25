CREATE SEQUENCE translation_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE translation (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('translation_id_seq'),
    name VARCHAR(255) NOT NULL,
    value TEXT,
    language_code CHAR(2) NOT NULL,
    country_code CHAR(2) NOT NULL,
    template_name varchar(255) NOT NULL,
    constraint fk_template_ref foreign key (template_name, country_code) references template(name, country_code)
);
CREATE TABLE translation (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          value TEXT,
                          language_code CHAR(2) NOT NULL,
                          country_code CHAR(2) NOT NULL,
                          template_name varchar(255) NOT NULL,
                         constraint fk_template_ref
                             foreign key (template_name, country_code)
                                 references template(name, country_code)
);
CREATE TABLE template (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          country_code CHAR(2) NOT NULL,
                          content TEXT,
            constraint unique_name_country_code UNIQUE (name, country_code)
);
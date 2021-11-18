CREATE TABLE IF NOT EXISTS app_user(
    id VARCHAR(255) PRIMARY KEY ,
    username VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    middle_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    birth_date DATE,
    created_date TIMESTAMP,
    updated_date TIMESTAMP

)
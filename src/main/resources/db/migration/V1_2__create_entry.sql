CREATE TABLE IF NOT EXISTS entry(
    id VARCHAR(255) PRIMARY KEY ,
    title VARCHAR(255),
    content VARCHAR(255),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    user_id VARCHAR(255),
    CONSTRAINT fk_entry FOREIGN KEY(user_id) REFERENCES app_user(id)
)
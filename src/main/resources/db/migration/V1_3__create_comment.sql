CREATE TABLE IF NOT EXISTS comment(
    id VARCHAR(255) PRIMARY KEY ,
    comment VARCHAR(255),
    is_updated BOOLEAN,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    user_id VARCHAR(255),
    entry_id VARCHAR(255),
    CONSTRAINT fk_comment FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (entry_id) REFERENCES entry(id)

)
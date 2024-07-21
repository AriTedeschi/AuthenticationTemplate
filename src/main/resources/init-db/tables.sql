CREATE TABLE TB_USER (
    uuid VARCHAR(36) PRIMARY KEY,
    user_code VARCHAR(17) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE user_details (
    user_id INT AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    authorities ENUM("PROTECTED_AUTHORITY","READ_AUTHORITY","WRITE_AUTHORITY","UPDATE_AUTHORITY","DELETE_AUTHORITY") NOT NULL,
    PRIMARY KEY (user_id)
);

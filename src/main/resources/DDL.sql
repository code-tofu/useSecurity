CREATE TABLE IF NOT EXISTS user_details (
    user_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    authority ENUM("USER","SUPERUSER","ADMIN") NOT NULL, --changeMe
    PRIMARY KEY (user_id)
);
ALTER TABLE user_credentials AUTO_INCREMENT=10000; --changeMe

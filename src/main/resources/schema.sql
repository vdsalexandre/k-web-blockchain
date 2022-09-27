CREATE TABLE IF NOT EXISTS wiuser (
    id          INTEGER         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(25)     NOT NULL,
    email       VARCHAR(255)    NOT NULL,
    password    VARCHAR(255)     NOT NULL,
    PRIMARY KEY (id)
);
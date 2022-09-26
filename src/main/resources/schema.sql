CREATE TABLE IF NOT EXISTS wiuser (
    id          INTEGER         NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255)    NOT NULL,
    password    VARCHAR(41)     NOT NULL,
    PRIMARY KEY (id)
);
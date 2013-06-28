# --- !Ups

CREATE TABLE User (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    created_time timestamp NOT NULL,
    updated_time timestamp NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE User;
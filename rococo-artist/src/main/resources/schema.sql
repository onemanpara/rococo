create table if not exists artist
(
    id                     binary(16)    unique not null default (UUID_TO_BIN(UUID(), true)),
    name                   varchar(50)   unique not null,
    biography              text,
    photo                  longblob,
    primary key (id)
);
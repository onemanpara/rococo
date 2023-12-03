create table if not exists artist
(
    id                     binary(16)    unique not null default (UUID_TO_BIN(UUID(), true)),
    name                   varchar(50)   not null,
    biography              text          not null,
    photo                  longblob      not null,
    primary key (id)
);
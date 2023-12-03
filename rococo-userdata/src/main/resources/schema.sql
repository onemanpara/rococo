create table if not exists `user`
(
    id                      binary(16)    unique not null default (UUID_TO_BIN(UUID(), true)),
    username                varchar(50)   unique not null,
    firstname               varchar(50),
    lastname                varchar(50),
    avatar                  longblob,
    primary key (id)
);

create table if not exists painting
(
    id                      binary(16)    unique not null default (UUID_TO_BIN(UUID(), true)),
    title                   varchar(50)   not null,
    description             text  not null,
    content                 longblob      not null,
    museum_id               binary(16)    not null,
    artist_id               binary(16)    not null,
    primary key (id)
);

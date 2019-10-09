CREATE TABLE pattern
(
    id      UUID NOT NULL,
    uri     varchar(255),
    name    varchar(255),
    content jsonb,
    PRIMARY KEY (id)
);

CREATE TABLE pattern_language
(
    id      UUID NOT NULL,
    uri     varchar(255),
    name    varchar(255),
    content jsonb,
    PRIMARY KEY (id)
);
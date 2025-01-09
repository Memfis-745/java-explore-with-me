
create table if not exists users
(
    id    bigint generated by default as identity primary key,
    name  varchar(250) not null,
    email varchar(254) not null,
    constraint uq_user_email unique (email)
);

create table if not exists categories
(
    id   bigint generated by default as identity primary key,
    name varchar(50) not null,
    constraint uq_category_name unique (name)
);

create table if not exists locations
(
    id  bigint generated by default as identity primary key,
    lat float not null,
    lon float not null
);

create table if not exists events
(
    id                 bigint generated by default as identity primary key,
    initiator_id       bigint        not null
        constraint events_users_id_fk
            references users,
    annotation         varchar(2000) not null,
    category_id        bigint        not null
        constraint events_categories_id_fk
            references categories,
    description        varchar(7000) not null,
    event_date         timestamp without time zone,
    location_id        bigint        not null
        constraint events_locations_id_fk
            references locations,
    paid               boolean       not null,
    participant_limit  integer       not null,
    request_moderation boolean       not null,
    title              varchar(120)  not null,
    state              varchar(15)   not null,
    published_on       timestamp without time zone,
    created_on         timestamp without time zone
);

create table if not exists requests
(
    id           bigint generated by default as identity primary key,
    created      timestamp without time zone,
    event_id     bigint      not null
        constraint requests_events_id_fk
            references events,
    requester_id bigint      not null
        constraint requests_users_id_fk
            references users,
    status       varchar(15) not null
);

create table if not exists compilations
(
    id     bigint generated by default as identity primary key,
    title  varchar(50) not null,
    pinned boolean     not null
);

create table if not exists events_compilations
(
    event_id       bigint not null
        constraint events_compilations_events_id_fk
            references events on delete cascade,
    compilation_id bigint not null
        constraint events_compilations_compilations_id_fk
            references compilations on delete cascade
);


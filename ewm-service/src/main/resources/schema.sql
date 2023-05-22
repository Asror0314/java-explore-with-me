CREATE SCHEMA IF NOT EXISTS explore;

drop table if exists explore.compilation_event;
drop table if exists explore.compilation;
drop table if exists explore.request;
drop table if exists explore.event;
drop table if exists explore.location;
drop table if exists explore.users;
drop table if exists explore.category;

CREATE TABLE IF NOT EXISTS explore.users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username CHARACTER VARYING(50) NOT NULL,
    email CHARACTER VARYING(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS explore.category
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name CHARACTER VARYING(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS explore.location
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat NUMERIC(15,10) NOT NULL,
    lon NUMERIC(15,10) NOT NULL
);

CREATE TABLE IF NOT EXISTS explore.event
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation TEXT,
    category_id BIGINT REFERENCES explore.category (id),
    confirmed_requests INTEGER,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    description TEXT,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT REFERENCES explore.users (id),
    location_id BIGINT REFERENCES explore.location (id),
    paid BOOLEAN,
    participant_limit INTEGER,
    published_date TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state CHARACTER VARYING(50),
    title TEXT,
    limit_available BOOLEAN
);

CREATE TABLE IF NOT EXISTS explore.request
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT REFERENCES explore.event(id) NOT NULL,
    requester_id BIGINT REFERENCES explore.users(id) NOT NULL,
    status CHARACTER VARYING(20)
);

CREATE TABLE IF NOT EXISTS explore.compilation
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS explore.compilation_event
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES explore.event(id) NOT NULL,
    compilation_id BIGINT REFERENCES explore.compilation(id) NOT NULL
);
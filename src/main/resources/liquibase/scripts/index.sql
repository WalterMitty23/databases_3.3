-- liquibase formatted sql

-- changeset sql-hogwarts:1
CREATE INDEX st_name_idx ON student (name);

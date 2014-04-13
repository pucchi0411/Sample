# Comments schema

# --- !Ups
CREATE SEQUENCE thread_id_seq;
CREATE TABLE threads (
    id integer NOT NULL DEFAULT nextval('thread_id_seq'),
    name varchar(50) NOT NULL,
    created_at datetime,
    updated_at datetime default CURRENT_TIMESTAMP
);

CREATE SEQUENCE comment_id_seq;
CREATE TABLE comments (
    id integer NOT NULL DEFAULT nextval('comment_id_seq'),
    thread_id integer NOT NULL,
    name varchar(50) NULL,
    comment varchar(255),
    created_at datetime,
    updated_at datetime default CURRENT_TIMESTAMP,
    FOREIGN KEY(thread_id)
    REFERENCES threads(id)
    ON DELETE CASCADE
);

# --- !Downs

DROP TABLE comments;
DROP SEQUENCE comment_id_seq;
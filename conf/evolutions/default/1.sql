# Comments schema

# --- !Ups
CREATE SEQUENCE thread_id_seq;
CREATE TABLE threads (
    id integer NOT NULL DEFAULT nextval('thread_id_seq'),
    name varchar(50) NOT NULL
);

CREATE SEQUENCE comment_id_seq;
CREATE TABLE comments (
    id integer NOT NULL DEFAULT nextval('comment_id_seq'),
    thread_id integer NOT NULL,
    name varchar(50) NULL,
    comment varchar(255),
    FOREIGN KEY(tread_id) REFERENCES threads(id)
);

# --- !Downs

DROP TABLE comments;
DROP SEQUENCE comment_id_seq;
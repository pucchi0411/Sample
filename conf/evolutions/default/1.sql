# Comments schema

# --- !Ups
CREATE SEQUENCE IF NOT EXISTS board_id_seq;
CREATE TABLE boards (
    id integer NOT NULL DEFAULT nextval('board_id_seq'),
    name varchar(50) NOT NULL,
    message varchar(255) NOT NULL,
    created_at datetime,
    updated_at datetime default CURRENT_TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS thread_id_seq;
CREATE TABLE threads (
    id integer NOT NULL DEFAULT nextval('thread_id_seq'),
    board_id integer NOT NULL,
    name varchar(50) NOT NULL,
    message varchar(255) NOT NULL,
    created_at datetime,
    updated_at datetime default CURRENT_TIMESTAMP,
    FOREIGN KEY(board_id)
    REFERENCES boards(id)
    ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS comment_id_seq;
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

CREATE SEQUENCE IF NOT EXISTS account_id_seq;
CREATE TABLE accounts (
    id integer NOT NULL DEFAULT nextval('account_id_seq'),
    email text NOT NULL,
    password text NOT NULL,
    permission text NOT NULL
)

# --- !Downs

DROP TABLE comments;
DROP SEQUENCE comment_id_seq;
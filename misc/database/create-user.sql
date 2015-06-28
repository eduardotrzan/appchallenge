CREATE SEQUENCE system_user_id_seq;
CREATE TABLE system_user(
  system_user_id integer NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(200) NOT NULL,
  email varchar(100) NOT NULL,
  open_id varchar(255) NOT NULL,
  CONSTRAINT system_user_id PRIMARY KEY (system_user_id),
  CONSTRAINT unq_open_id UNIQUE (open_id)
);
ALTER SEQUENCE system_user_id_seq OWNED BY system_user.system_user_id;
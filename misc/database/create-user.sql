CREATE SEQUENCE sytem_user_id_seq;
CREATE TABLE sytem_user(
  sytem_user_id integer NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(200) NOT NULL,
  email varchar(100) NOT NULL,
  open_id varchar(255) NOT NULL,
  CONSTRAINT sytem_user_id PRIMARY KEY (sytem_user_id),
  CONSTRAINT unq_open_id UNIQUE (open_id)
);
ALTER SEQUENCE sytem_user_id_seq OWNED BY sytem_user.sytem_user_id;
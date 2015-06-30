DROP SEQUENCE IF EXISTS system_user_id_seq;
DROP SEQUENCE IF EXISTS market_place_id_seq;
DROP SEQUENCE IF EXISTS company_id_seq;

DROP TABLE IF EXISTS system_user;
DROP TABLE IF EXISTS market_place;
DROP TABLE IF EXISTS company;


CREATE SEQUENCE company_id_seq;
CREATE TABLE company(
  company_id integer NOT NULL,
  uuid varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  phone_number varchar(255) NOT NULL,
  website varchar(255) NOT NULL,
  status varchar(255) NOT NULL,
  edition_code integer NOT NULL,
  account_identifier varchar(255) NOT NULL,
  CONSTRAINT company_id PRIMARY KEY (company_id),
  CONSTRAINT unq_uuid UNIQUE (uuid)
);
ALTER SEQUENCE company_id_seq OWNED BY company.company_id;

CREATE SEQUENCE system_user_id_seq;
CREATE TABLE system_user(
  system_user_id integer NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(200) NOT NULL,
  email varchar(100) NOT NULL,
  open_id varchar(255) NOT NULL,
  company_id integer NOT NULL,
  profile integer NOT NULL,
  CONSTRAINT system_user_id PRIMARY KEY (system_user_id),
  CONSTRAINT unq_open_id UNIQUE (open_id),
  CONSTRAINT fk_company_id FOREIGN KEY (company_id) REFERENCES company (company_id)
);
ALTER SEQUENCE system_user_id_seq OWNED BY system_user.system_user_id;

CREATE SEQUENCE market_place_id_seq;
CREATE TABLE market_place(
  market_place_id integer NOT NULL,
  base_url varchar(255) NOT NULL,
  partner varchar(255) NOT NULL,
  company_id integer NOT NULL,
  CONSTRAINT market_place_id PRIMARY KEY (market_place_id),
  CONSTRAINT fk_company_id FOREIGN KEY (company_id) REFERENCES company (company_id)
);
ALTER SEQUENCE market_place_id_seq OWNED BY market_place.market_place_id;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO username;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO username;
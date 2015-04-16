--createdb stock
--psql -d stock

CREATE TABLE item (
  id bigserial primary key,
  name varchar(255)  NOT NULL,
  description TEXT,
  x integer,
  y integer,
  id_parent bigserial references item (id) ON DELETE CASCADE ,
  filename varchar(255) DEFAULT NULL,
  theblop BYTEA,
  ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted boolean
);

CREATE INDEX ts_idx ON item (ts);


-- Inserts
insert into item values (1, 'root', 'The root of all items', 0, 0, 1, NULL, NULL, now(), false);


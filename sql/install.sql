--createdb stock
--psql -d stock

CREATE TABLE player (
  id bigserial primary key,
  name varchar(255)  NOT NULL,
  email varchar(255)  NOT NULL
);

CREATE TABLE item (
  id bigserial primary key,
  symbol varchar(255)  NOT NULL,
  amount integer,
  price integer,
  idPlayer bigserial references player (id) ON DELETE CASCADE,
  ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ts_idx ON item (ts);

-- Inserts
insert into player values (1, 'test', 'test@test.com');
insert into item values (1, 'CASH', 10000, 1, 1, now());


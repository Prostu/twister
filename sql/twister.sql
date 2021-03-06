CREATE TABLE "user"(
  id      bigserial   PRIMARY KEY,
  handle  varchar(32) NOT NULL,
  name    varchar(64) NOT NULL,
  joined  timestamp   NOT NULL DEFAULT current_timestamp
);

CREATE TABLE "tweet"(
  id       bigserial    PRIMARY KEY,
  user_id  bigint       REFERENCES "user"(id),
  message  varchar(256) NOT NULL
);

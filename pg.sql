CREATE TABLE "film" (
  "film_id" integer,
  "name" string,
  "description" String,
  "release_date" date,
  "duration" integer,
  "mpa_id" Long
);

CREATE TABLE "mpa" (
  "mpa_id" integer,
  "name" String
);

CREATE TABLE "genre" (
  "genre_id" integer,
  "name" String
);

CREATE TABLE "film_genre" (
  "film_id" integer,
  "genre_id" integer
);

CREATE TABLE "user" (
  "user_id" integer,
  "name" String,
  "email" String,
  "login" String,
  "birthday" LocalDate
);

CREATE TABLE "friend" (
  "user_id" integer,
  "friend_id" integer,
  "approved" boolean
);

CREATE TABLE "like" (
  "film_id" integer,
  "user_id" integer
);

ALTER TABLE "film" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("mpa_id");

ALTER TABLE "user" ADD FOREIGN KEY ("user_id") REFERENCES "friend" ("user_id");

ALTER TABLE "user" ADD FOREIGN KEY ("user_id") REFERENCES "friend" ("friend_id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "film" ADD FOREIGN KEY ("film_id") REFERENCES "film_genre" ("film_id");

ALTER TABLE "film" ADD FOREIGN KEY ("film_id") REFERENCES "like" ("film_id");

ALTER TABLE "like" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

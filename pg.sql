CREATE TABLE "films" (
  "film_id" Long,
  "name" String,
  "description" String,
  "releaseDate" LocalDate,
  "duration" Integer,
  "mpa_id" Long
);

CREATE TABLE "mpa" (
  "mpa_id" Long,
  "name" String
);

CREATE TABLE "genres" (
  "genre_id" Long,
  "name" String
);

CREATE TABLE "film_genres" (
  "film_id" Long,
  "genre_id" Long
);

CREATE TABLE "users" (
  "user_id" Long,
  "name" String,
  "email" String,
  "login" String,
  "birthday" LocalDate
);

CREATE TABLE "friends" (
  "user_id" Long,
  "friend_id" Long,
  "approved" boolean
);

CREATE TABLE "likes" (
  "film_id" Long,
  "user_id" Long
);

ALTER TABLE "films" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("mpa_id");

ALTER TABLE "users" ADD FOREIGN KEY ("user_id") REFERENCES "friends" ("user_id");

ALTER TABLE "users" ADD FOREIGN KEY ("user_id") REFERENCES "friends" ("friend_id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("genre_id");

ALTER TABLE "films" ADD FOREIGN KEY ("film_id") REFERENCES "film_genres" ("film_id");

ALTER TABLE "films" ADD FOREIGN KEY ("film_id") REFERENCES "likes" ("film_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

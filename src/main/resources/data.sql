-- заполняем таблицу рейтингов

insert into MPAA values ( 1,'G' );
insert into MPAA values ( 2,'PG' );
insert into MPAA values ( 3,'PG-13' );
insert into MPAA values ( 4,'R' );
insert into MPAA values ( 5,'NC-17' );

-- заполняем таблицу жанров
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Комедия');
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Драма');
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Мультфильм');
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Триллер');
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Документальный');
INSERT INTO PUBLIC.GENRES (NAME) VALUES ('Боевик');
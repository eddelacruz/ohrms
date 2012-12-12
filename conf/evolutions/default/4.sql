# --- !Ups


ALTER TABLE dentists ADD image varchar(200);
ALTER TABLE dentists ADD status tinyint(1);
ALTER TABLE dentists ADD date_created TIMESTAMP,;
INSERT INTO dentists (image,status,date_created) VALUES ('1.png','1','2012-10-10 09:44:37') WHERE id ="71b8ecdd-33c9-4aaf-aa30-9d77419aeb95";
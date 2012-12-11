# --- !Ups


ALTER TABLE dentists ADD image varchar(200);
ALTER TABLE dentists ADD status tinyint(1);
ALTER TABLE dentists ADD date_created TIMESTAMP;
ALTER TABLE dentists ADD date_last_updated TIMESTAMP;
UPDATE dentists SET image='1.png', status=1, date_created='2012-10-10 09:44:37' WHERE id ='71b8ecdd-33c9-4aaf-aa30-9d77419aeb95';
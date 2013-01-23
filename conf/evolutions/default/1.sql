# --- !Ups

create table dental_services(
id char(36) NOT NULL,
name varchar(200) NOT NULL,
type varchar(200),
target tinyint(1),
price varchar(50),
date_created TIMESTAMP,
date_last_updated TIMESTAMP,
PRIMARY KEY(id));

create table users(
id char(36) NOT NULL,
user_name varchar(200) NOT NULL,
password varchar(200) NOT NULL,
role tinyint(1),
status tinyint(1),
date_created TIMESTAMP,
PRIMARY KEY(id));

create table dentist(
id char(36) NOT NULL,
first_name varchar(200),
middle_name varchar(200),
last_name varchar(200),
address varchar(200),
contact_no varchar (100),
prc_no varchar(100),
user_id char(36) NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY (user_id) REFERENCES users(id));

create table specialization(
dentist_id char(36) NOT NULL,
name varchar(200),
foreign key (dentist_id) references dentist(id));

create table patients(
id char(36) NOT NULL,
first_name varchar(200),
middle_name varchar(200),
last_name varchar(200),
address varchar(200),
contact_no varchar(100),
date_of_birth DATE,
image varchar(200),
medical_history varchar(400),
status tinyint(1),
date_created TIMESTAMP,
date_last_updated TIMESTAMP,
Primary key(id));

create table staff(
id char(36) NOT NULL,
first_name varchar(200),
middle_name varchar(200),
last_name varchar(200),
position varchar(200),
user_id char(36),
date_created TIMESTAMP,
Primary key(id),
FOREIGN KEY (user_id) REFERENCES users(id));

create table announcements(
id char(36) NOT NULL,
user_id char(36),
announcement varchar(200),
date_created TIMESTAMP,
PRIMARY KEY(id),
foreign key (user_id) references users(id));

create table payments(
id char(36) NOT NULL,
patient_id char(36),
payment_type tinyint(1),
amount_paid varchar(50),
balance varchar(50),
completion_date DATE,
date_created TIMESTAMP,
Primary key(id),
FOREIGN KEY (patient_id) REFERENCES patients(id));

insert into dental_services values ('ed1ea643-585a-4571-b751-a5e517a7bce2','Prophylaxis','Cleaning','1','690','2012-11-19 03:14:07','2012-11-20 01:18:05');
insert into dental_services values ('5dd97b8a-a821-4c65-82b0-88c18bd9de39','Prosthodontics','fixed bridgework','2','1060','2012-10-10 09:44:37','2012-11-20 01:18:05');
insert into dental_services values ('11e5fd54-5b4c-44be-9381-1a09ed659045','Root Canal','Root Canal','1','450','2012-11-27 07:34:17','2012-11-29 02:28:15');

insert into users values ('c7e5ef5d-07eb-4904-abbe-0aa73c13490f','cvbautista','12345','1','1','2012-11-27 07:34:17');
insert into users values ('b2c12f2d-0117-45ac-b791-d49557340a41','jbernardo','54321','1','1','2012-10-17 07:34:17');
insert into users values ('d0d7e4a5-ff89-4f80-85f6-a424c10ff690','jbuning','09876','1','1','2012-09-07 07:34:17');
insert into users values ('58d46d40-3794-4f92-a935-ca7d1ac24efd','jstamaria','abcdef','2','1','2012-09-09 07:34:17');

insert into announcements values ('gh68ha2m-7301-hb0o-h836-0aa73c13490f','d0d7e4a5-ff89-4f80-85f6-a424c10ff690','walang pasok sa monday at tuesday at wednesday at friday at sunday','2012-09-09 07:34:17');

insert into dentist values ('71b8ecdd-33c9-4aaf-aa30-9d77419aeb95','Johana','Mendoza','Bernardo','Apalit Pampanga','+639109672605','p08b86689898','b2c12f2d-0117-45ac-b791-d49557340a41');
insert into specialization values ('71b8ecdd-33c9-4aaf-aa30-9d77419aeb95','Periodontics');
insert into specialization values ('71b8ecdd-33c9-4aaf-aa30-9d77419aeb95', 'Orthodontics');

INSERT INTO patients VALUES (
'b2be3ffc-f16f-42c8-a2da-756a2576d13f',
'Billy',
'Protacio',
'Mallari',
'Bahay Pari, Candaba Pampanga',
'639359285037',
'2012-11-01',
'abcd.png',
'nagtae ng ginto, nagsuka ng ipot',
'1',
'2012-10-10 09:44:37',
'2012-10-10 09:44:37'
);

INSERT INTO patients VALUES (
'a5434d36-dd94-425e-87a3-3c859db7b130',
'Elizer',
'Dionisio',
'Delacruz',
'Balagtas, Bulacan',
'+639359280037',
'2012-11-09',
'dcba.png',
'hinimatay, allergy sa masasarap na pagkain, nagsuka ng tae',
'1',
'2012-10-11 09:44:37',
'2012-10-12 09:44:37'
);

INSERT INTO `staff`
(`id`,
`first_name`,
`middle_name`,
`last_name`,
`position`,
`user_id`,
`date_created`)
VALUES
(
'76817724-266c-4df8-9a3b-b464976366fd',
'Jodi',
'Protacia',
'Sta.Maria',
'Secretary',
'58d46d40-3794-4f92-a935-ca7d1ac24efd',
'2012-09-09 07:34:17'
);

INSERT INTO `payments`
(`id`,
`payment_type`,
`amount_paid`,
`balance`,
`completion_date`,
`patient_id`,
`date_created`)
VALUES
(
'd653be7a-c70a-4fbf-ac22-f74f463a1150',
'2',
'500',
'100',
'2012-11-27',
'a5434d36-dd94-425e-87a3-3c859db7b130',
'2012-09-09 07:34:17'
);

# --- !Downs

DROP TABLE IF EXISTS dental_services;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS dentist;
DROP TABLE IF EXISTS specialization;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS clinic;
DROP TABLE IF EXISTS announcements;

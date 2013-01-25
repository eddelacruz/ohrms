# --- !Ups

CREATE  TABLE `ohrms`.`clinic` (
  `id` CHAR(36) NOT NULL ,
  `clinic_name` VARCHAR(200) NULL ,
  `address` VARCHAR(200) NULL ,
  `image` VARCHAR(200) NULL ,
  PRIMARY KEY (`id`) );

INSERT INTO `ohrms`.`clinic` (id, clinic_name, address,image)
VALUES('52h8a0k0-3794-4f92-a935-v8nhao924efd', 'SaberTooth', 'Balagtas, Bulacan', 'a.png');

ALTER TABLE dental_services ADD COLUMN status tinyint(1);
UPDATE dental_services SET status = 1;

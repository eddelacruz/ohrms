# --- !Ups

ALTER TABLE patients DROP FOREIGN KEY `patients_ibfk_1`;
ALTER TABLE patients DROP COLUMN `medical_history_id` ,DROP INDEX `medical_history_id`;
ALTER TABLE medical_history CHANGE COLUMN `name` `description` VARCHAR(200) NULL DEFAULT NULL  , ADD COLUMN `patient_id` CHAR(36) NULL  AFTER `description` ;

ALTER TABLE medical_history
  ADD CONSTRAINT `patient_fk`
  FOREIGN KEY (`patient_id` )
  REFERENCES patients (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `medical_history_idx` (`patient_id` ASC);

# --- !Downs

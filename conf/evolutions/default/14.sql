# --- !Ups

CREATE  TABLE `ohrms`.`teeth_affected` (
  `id` CHAR(36) NOT NULL ,
  `treatment_plan_id` CHAR(36) NULL ,
  `name` VARCHAR(50) NULL COMMENT '1 - 32, A - T' ,
  `position` VARCHAR(50) NULL COMMENT 'Upper ; Maxillary, Lower ; Mandibular' ,
  `view` VARCHAR(50) NULL COMMENT 'f - Facial, m - Mid' ,
  `type` VARCHAR(45) NULL COMMENT 'a - Adult, c - Child' ,
  PRIMARY KEY (`id`) ,
  INDEX `a_idx` (`treatment_plan_id` ASC) ,
  CONSTRAINT `teeth_adult_ibfk_1`
    FOREIGN KEY (`treatment_plan_id` )
    REFERENCES `ohrms`.`treatment_plan` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

ALTER TABLE `ohrms`.`treatment_plan` ADD COLUMN `patient_id` CHAR(36) NULL  AFTER `date_performed` , ADD COLUMN `dentist_id` CHAR(36) NULL  AFTER `patient_id` , ADD COLUMN `teeth_name` CHAR(10) NULL  AFTER `dentist_id` , ADD COLUMN `status` INT(1) NULL  AFTER `teeth_name` , ADD COLUMN `image` VARCHAR(5000) NULL  AFTER `status` ;

ALTER TABLE `ohrms`.`treatment_plan`
  ADD CONSTRAINT `treatment_plan_ibfk_2`
  FOREIGN KEY (`patient_id` )
  REFERENCES `ohrms`.`patients` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
  ADD CONSTRAINT `treatment_plan_ibfk_3`
  FOREIGN KEY (`dentist_id` )
  REFERENCES `ohrms`.`dentists` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `treatment_plan_ibfk_1_idx1` (`patient_id` ASC)
, ADD INDEX `treatment_plan_ibfk_3_idx` (`dentist_id` ASC) ;

ALTER TABLE `ohrms`.`teeth_affected` DROP FOREIGN KEY `teeth_adult_ibfk_1` ;
ALTER TABLE `ohrms`.`teeth_affected` DROP COLUMN `treatment_plan_id`
, DROP INDEX `a_idx` ;

INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FA', 'FA', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FB', 'FB', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FC', 'FC', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FD', 'FD', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FE', 'FE', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FF', 'FF', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FG', 'FG', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FH', 'FH', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FI', 'FI', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FJ', 'FJ', 'upper', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FK', 'FK', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FL', 'FL', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FM', 'FM', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FN', 'FN', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FO', 'FO', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FP', 'FP', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FQ', 'FQ', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FR', 'FR', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FS', 'FS', 'lower', 'f', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('FT', 'FT', 'lower', 'f', 'c');

INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MA', 'MA', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MB', 'MB', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MC', 'MC', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MD', 'MD', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('ME', 'ME', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MF', 'MF', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MG', 'MG', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MH', 'MH', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MI', 'MI', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MJ', 'MJ', 'upper', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MK', 'MK', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('ML', 'ML', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MM', 'MM', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MN', 'MN', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MO', 'MO', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MP', 'MP', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MQ', 'MQ', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MR', 'MR', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MS', 'MS', 'lower', 'm', 'c');
INSERT INTO `ohrms`.`teeth_affected` (`id`, `name`, `position`, `view`, `type`) VALUES ('MT', 'MT', 'lower', 'm', 'c');

ALTER TABLE `ohrms`.`treatment_plan`
  ADD CONSTRAINT `treatment_plan_ibfk_4`
  FOREIGN KEY (`teeth_name` )
  REFERENCES `ohrms`.`teeth_affected` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `treatment_plan_ibfk_4_idx` (`teeth_name` ASC) ;

ALTER TABLE `ohrms`.`treatment_plan` DROP FOREIGN KEY `treatment_plan_ibfk_4` ;
ALTER TABLE `ohrms`.`treatment_plan` CHANGE COLUMN `teeth_name` `teeth_id` CHAR(10) NULL DEFAULT NULL  ,
  ADD CONSTRAINT `treatment_plan_ibfk_4`
  FOREIGN KEY (`teeth_id` )
  REFERENCES `ohrms`.`teeth_affected` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

# --- !Downs

DROP TABLE teeth_affected
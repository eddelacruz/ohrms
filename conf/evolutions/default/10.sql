# --- !Ups

CREATE  TABLE `ohrms`.`appointments` (
  `id` CHAR(36) NOT NULL ,
  `description` VARCHAR(200) NULL ,
  `first_name` VARCHAR(200) NULL ,
  `middle_name` VARCHAR(200) NULL ,
  `last_name` VARCHAR(200) NULL ,
  `dentist_id` CHAR(36) NULL ,
  `contact_no` VARCHAR(50) NULL ,
  `address` VARCHAR(200) NULL ,
  `status` INT(1) NULL ,
  `date_start` TIMESTAMP NULL ,
  `date_end` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `appointments_ibfk_1_idx` (`dentist_id` ASC) ,
  CONSTRAINT `appointments_ibfk_1`
    FOREIGN KEY (`dentist_id` )
    REFERENCES `ohrms`.`dentists` (`id` )
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
DEFAULT CHARACTER SET = latin1;

ALTER TABLE `ohrms`.`appointments` CHANGE COLUMN `status` `status` INT(1) NULL DEFAULT NULL COMMENT '1 - pending:blue, 2 - time_rescheduled:yellow,  3 - cancelled:red,  4 - completed:green'  ;

# --- !Downs

DROP TABLE IF EXISTS appointments;

# --- !Ups

CREATE  TABLE `ohrms`.`banned_dental_services` (
  `id` CHAR(36) NOT NULL ,
  `dental_service_id` CHAR(36) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `banned_dental_services_ibfk_1_idx` (`dental_service_id` ASC) ,
  CONSTRAINT `banned_dental_services_ibfk_1`
    FOREIGN KEY (`dental_service_id` )
    REFERENCES `ohrms`.`dental_services` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
DEFAULT CHARACTER SET = latin1;

DELETE FROM `ohrms`.`dental_services` WHERE `id`='ed1ea643-585a-4571-b751-a5e517a7bce2';
UPDATE `ohrms`.`dental_services` SET `code`='EXT', `date_created`='2013-1-22 14:22:00', `date_last_updated`='2013-1-22 14:22:00' WHERE `id`='2bf84e44-eb3c-46b6-8a92-57e8ed127f39';
DELETE FROM `ohrms`.`dental_services` WHERE `id`='5dd97b8a-a821-4c65-82b0-88c18bd9de39';
DELETE FROM `ohrms`.`dental_services` WHERE `id`='11e5fd54-5b4c-44be-9381-1a09ed659045';
INSERT INTO `ohrms`.`dental_services` (`id`, `name`, `code`, `type`, `target`, `price`, `color`, `date_created`, `date_last_updated`) VALUES ('06500656-f481-4d52-a662-7cde61ed8fe8', 'Pasta', 'PASTA', 'General Dentistry', '1', '100', 'cream', '2013-1-22 14:22:00', '2013-1-22 14:22:00');
UPDATE `ohrms`.`dental_services` SET `date_created`='2013-1-22 14:22:00', `date_last_updated`='2013-1-22 14:22:00' WHERE `id`='60a8a017-7fb6-40b6-ab97-af3b24255006';

ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `tool_type` INT(1) NULL DEFAULT NULL COMMENT '1 - paint, 2 - symbol'  ;

UPDATE `ohrms`.`dental_services` SET `name`='Dental Fillings' WHERE `id`='06500656-f481-4d52-a662-7cde61ed8fe8';

UPDATE `ohrms`.`dental_services` SET `color`='fff799' WHERE `id`='06500656-f481-4d52-a662-7cde61ed8fe8';
UPDATE `ohrms`.`dental_services` SET `color`='ff0000' WHERE `id`='2bf84e44-eb3c-46b6-8a92-57e8ed127f39';
UPDATE `ohrms`.`dental_services` SET `color`='00c6ff' WHERE `id`='60a8a017-7fb6-40b6-ab97-af3b24255006';

# --- !Downs

DROP TABLE IF EXISTS banned_dental_services;


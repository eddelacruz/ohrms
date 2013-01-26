# --- !Ups

INSERT INTO `ohrms`.`banned_dental_services` (`id`, `dental_service_id`) VALUES ('2bf84e44-eb3c-46b6-8a92-57e8ed127f39', '06500656-f481-4d52-a662-7cde61ed8fe8');

ALTER TABLE `ohrms`.`banned_dental_services` DROP FOREIGN KEY `banned_dental_services_ibfk_1` ;
ALTER TABLE `ohrms`.`banned_dental_services` CHANGE COLUMN `dental_service_id` `dental_service_id` CHAR(36) NOT NULL  ,
  ADD CONSTRAINT `banned_dental_services_ibfk_1`
  FOREIGN KEY (`dental_service_id` )
  REFERENCES `ohrms`.`dental_services` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP PRIMARY KEY
, ADD PRIMARY KEY (`id`, `dental_service_id`) ;

INSERT INTO `ohrms`.`banned_dental_services` (`id`, `dental_service_id`) VALUES ('2bf84e44-eb3c-46b6-8a92-57e8ed127f39', '60a8a017-7fb6-40b6-ab97-af3b24255006');

# --- !Downs

# --- !Ups

ALTER TABLE `ohrms`.`dental_services` ADD COLUMN `code` VARCHAR(50) NULL  AFTER `date_last_updated` ;
ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `code` `code` VARCHAR(50) NULL DEFAULT NULL  AFTER `name` ;
ALTER TABLE `ohrms`.`dental_services` ADD COLUMN `color` VARCHAR(45) NULL  AFTER `price` ;
ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` TINYINT(1) NULL DEFAULT NULL COMMENT '1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid'  ;

INSERT INTO `ohrms`.`dental_services` VALUES ('60a8a017-7fb6-40b6-ab97-af3b24255006','Oral Prophylaxis','OP','General Dentistry','1','100','red','2012-12-17 01:17:00','2012-12-17 01:17:00');
INSERT INTO `ohrms`.`dental_services` VALUES ('2bf84e44-eb3c-46b6-8a92-57e8ed127f39','Extraction','EX','General Dentistry','2','200','orange','2012-12-17 01:17:00','2012-12-17 01:17:00');

INSERT INTO `ohrms`.`treatment_plan` VALUES('06a59409-ceb9-4af5-9fbf-ce96c6083465','2bf84e44-eb3c-46b6-8a92-57e8ed127f39','2012-12-17 01:35:00');
INSERT INTO `ohrms`.`treatment_plan` VALUES('5dc0da01-b2c8-4361-9f0b-359ec38a1635','60a8a017-7fb6-40b6-ab97-af3b24255006','2012-12-17 01:39:00');

INSERT INTO ohrms.teeth_affected VALUES('1cea7a17-da7b-4410-9236-d9720159975d','06a59409-ceb9-4af5-9fbf-ce96c6083465','a','upper','facial','child');
INSERT INTO ohrms.teeth_affected VALUES('90b55520-e940-4a31-8e26-221c585adbd4','5dc0da01-b2c8-4361-9f0b-359ec38a1635','all','0','0','child');

ALTER TABLE `ohrms`.`teeth_affected` DROP FOREIGN KEY `teeth_affected_ibfk_1` ;
ALTER TABLE `ohrms`.`teeth_affected`
  ADD CONSTRAINT `teeth_affected_ibfk_1`
  FOREIGN KEY (`treatment_plan_id` )
  REFERENCES `ohrms`.`treatment_plan` (`id` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `ohrms`.`teeth_affected` DROP FOREIGN KEY `teeth_affected_ibfk_1` ;
ALTER TABLE `ohrms`.`teeth_affected`
  ADD CONSTRAINT `teeth_affected_ibfk_1`
  FOREIGN KEY (`treatment_plan_id` )
  REFERENCES `ohrms`.`treatment_plan` (`id` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;


# --- !Downs

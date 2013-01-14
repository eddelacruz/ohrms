# --- !Ups

ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` INT(1) NULL DEFAULT NULL COMMENT '1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid'  ;
ALTER TABLE `ohrms`.`users` CHANGE COLUMN `role` `role` INT(1) NULL DEFAULT NULL COMMENT '0 -> Staff, 1 -> Dentist'  ;

# --- !Downs

ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` TINYINT(1) NULL DEFAULT NULL COMMENT '1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid'  ;
ALTER TABLE `ohrms`.`users` CHANGE COLUMN `role` `role` TINYINT(1) NULL DEFAULT NULL COMMENT '0 -> Staff, 1 -> Dentist'  ;

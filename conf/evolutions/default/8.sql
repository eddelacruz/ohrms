# --- !Ups

ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` INT(1) NULL DEFAULT NULL COMMENT '1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid'  ;

# --- !Downs

ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` TINYINT(1) NULL DEFAULT NULL COMMENT '1 - Inclusive, 2 - Selective Fill, 3 - Selective Root, 4 - Selective Bridge, 5 - Selective Grid'  ;
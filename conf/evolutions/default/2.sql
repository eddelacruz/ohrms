# --- !Ups

ALTER TABLE `ohrms`.`dentist` RENAME TO  `ohrms`.`dentists` ;
ALTER TABLE `ohrms`.`staff` RENAME TO  `ohrms`.`staffs` ;
ALTER TABLE `ohrms`.`specialization` RENAME TO  `ohrms`.`specializations` ;
ALTER TABLE `ohrms`.`dental_services` CHANGE COLUMN `target` `target` TINYINT(1) NULL DEFAULT NULL COMMENT '1 -> Selective, 2 -> Inclusive'  ;
ALTER TABLE `ohrms`.`patients` CHANGE COLUMN `status` `status` TINYINT(1) NULL DEFAULT NULL COMMENT '0 -> Inactive, 1 -> Active'  ;
ALTER TABLE `ohrms`.`payments` DROP FOREIGN KEY `payments_ibfk_1` ;
ALTER TABLE `ohrms`.`payments` CHANGE COLUMN `payment_type` `type` TINYINT(1) NULL COMMENT '1 -> Full Payment, 2 -> Partial Payment'  , CHANGE COLUMN `completion_date` `date_completion` DATE NULL DEFAULT NULL  , CHANGE COLUMN `patient_id` `patient_id` CHAR(36) NULL  ,
  ADD CONSTRAINT `payments_ibfk_1`
  FOREIGN KEY (`patient_id` )
  REFERENCES `ohrms`.`patients` (`id` );
ALTER TABLE `ohrms`.`users` CHANGE COLUMN `role` `role` TINYINT(1) NULL DEFAULT NULL COMMENT '0 -> Staff, 1 -> Dentist'  , CHANGE COLUMN `status` `status` TINYINT(1) NULL DEFAULT NULL COMMENT '0 -> Inactive, 1 -> Active'  ;


# --- !Downs

ALTER TABLE `ohrms`.`dentists` RENAME TO  `ohrms`.`dentist` ;
ALTER TABLE `ohrms`.`staffs` RENAME TO  `ohrms`.`staff` ;
ALTER TABLE `ohrms`.`specializations` RENAME TO  `ohrms`.`specialization` ;




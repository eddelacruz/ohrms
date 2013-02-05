# --- !Ups

ALTER TABLE `ohrms`.`treatment_plan` ADD COLUMN `patient_id` CHAR(36) NULL AFTER `date_performed` ;
ALTER TABLE `ohrms`.`treatment_plan` ADD CONSTRAINT `treatment_plan_ibfk_2`FOREIGN KEY (`patient_id` )REFERENCES `ohrms`.`patients` (`id` )
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE `ohrms`.`payments` ADD COLUMN `dentist_id` CHAR(36) NULL AFTER `date_created` ;
ALTER TABLE `ohrms`.`payments` ADD CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`dentist_id` ) REFERENCES `ohrms`.`dentists` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION;
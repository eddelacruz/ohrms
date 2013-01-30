# --- !Ups

CREATE  TABLE `ohrms`.`treatment_plan` (
  `id` CHAR(36) NOT NULL ,
  `service_id` CHAR(36)  ,
  `date_performed` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;

ALTER TABLE `ohrms`.`treatment_plan`
  ADD CONSTRAINT `treatment_plan_ibfk_1`
  FOREIGN KEY (`service_id` )
  REFERENCES `ohrms`.`dental_services` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `treatment_plan_ibfk_1_idx` (`service_id` ASC) ;

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

ALTER TABLE `ohrms`.`teeth_affected` DROP FOREIGN KEY `teeth_adult_ibfk_1` ;
ALTER TABLE `ohrms`.`teeth_affected`
  ADD CONSTRAINT `teeth_affected_ibfk_1`
  FOREIGN KEY (`treatment_plan_id` )
  REFERENCES `ohrms`.`treatment_plan` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE  TABLE `ohrms`.`teeth_grid` (
  `id` CHAR(36) NOT NULL ,
  `teeth_affected_id` CHAR(36) NULL ,
  `11` TINYINT NULL ,
  `12` TINYINT NULL ,
  `13` TINYINT NULL ,
  `21` TINYINT NULL ,
  `22` TINYINT NULL ,
  `23` TINYINT NULL ,
  `31` TINYINT NULL ,
  `32` TINYINT NULL ,
  `33` TINYINT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `teeth_affected_ibfk_1_idx` (`teeth_affected_id` ASC) ,
  CONSTRAINT `teeth_grid_ibfk_1`
    FOREIGN KEY (`teeth_affected_id` )
    REFERENCES `ohrms`.`teeth_affected` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

# --- !Downs

DROP TABLE ohrms.treatment_plan
DROP TABLE ohrms.teeth_affected
DROP TABLE ohrms.teeth_grid

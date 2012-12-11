# --- !Ups

CREATE  TABLE `ohrms`.`audit_log` (
  `id` CHAR(36) NOT NULL ,
  `task` VARCHAR(45) NULL DEFAULT NULL ,
  `user_id` CHAR(36) NULL DEFAULT NULL ,
  `description` VARCHAR(500) NULL DEFAULT NULL ,
  `date_created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `user_idx` (`user_id` ASC) ,
  CONSTRAINT `audit_log_ibfk_2`
    FOREIGN KEY (`user_id` )
    REFERENCES `ohrms`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

# --- !Downs

drop table `ohrms`.`audit_log`;
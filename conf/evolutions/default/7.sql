# --- !Ups

ALTER TABLE staffs ADD COLUMN date_last_updated TIMESTAMP  AFTER date_created;
ALTER TABLE staffs ADD COLUMN status TINYINT(1) AFTER user_id;
ALTER TABLE staffs ADD COLUMN contact_no VARCHAR(200) AFTER last_name;
ALTER TABLE staffs ADD COLUMN address VARCHAR(200) AFTER contact_no;
INSERT INTO staffs VALUES('58d46d40-3794-4f92-a935-ca7d1ac24efc','Ana Marie','Protacia','Robielos','908679829122','Tabing Ilog', 'Secretary', '58d46d40-3794-4f92-a935-ca7d1ac24efd','1','2012-11-27 07:34:17','2012-11-27 07:34:17');
UPDATE dental_services SET color="green", code="C" where type="Cleaning";
UPDATE dental_services SET color="green", code="PROS" where type="fixed bridgework";
UPDATE dental_services SET color="green", code="RC" where type ="Root Canal";

# --- !Downs

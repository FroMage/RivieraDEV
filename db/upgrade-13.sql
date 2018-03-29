alter table speaker drop column isJUDConSpeaker;
alter table track add column isJUDCon bool;
update track set isJUDCon = false;
alter table track alter column isJUDCon set not null;

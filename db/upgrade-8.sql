update Speaker set isJUDConSpeaker = false where isJUDConSpeaker is null;
alter table Speaker alter column isJUDConSpeaker set not null;
alter table Speaker drop column year;
 
alter table Track add column position int4;
update Track set position = 0;
alter table Track alter column position set not null;

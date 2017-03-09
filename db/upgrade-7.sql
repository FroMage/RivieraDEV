alter table speaker add column isJUDConSpeaker bool;
update speaker set isJUDConSpeaker = false;
alter table speaker alter column isJUDConSpeaker set not null;

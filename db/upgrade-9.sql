alter table Talk add column isHiddenInTalksPage bool;
update Talk set isHiddenInTalksPage = false;
alter table Talk alter column isHiddenInTalksPage set not null;

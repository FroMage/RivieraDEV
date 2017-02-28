drop table PreviousSpeaker cascade;
 
create table PreviousSpeaker (
    id int8 not null,
    company varchar(255),
    firstName varchar(255),
    lastName varchar(255),
    photo varchar(255),
    year int4,
    primary key (id)
);

alter table Speaker add column star bool;
update Speaker set star = false;
alter table Speaker alter column star set not null;

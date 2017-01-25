drop table TemporarySlot cascade;

alter table Slot drop column labelEN;
alter table Slot drop column labelFR;
create table TemporarySlot (
    id int8 not null,
    endDate timestamp,
    labelEN varchar(255),
    labelFR varchar(255),
    startDate timestamp,
    primary key (id)
);

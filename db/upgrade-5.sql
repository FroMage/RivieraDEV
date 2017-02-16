     alter table Talk 
        drop constraint FK27A8CCADDAF92A;
    drop table TalkTheme cascade;

alter table Talk add column descriptionEN text;
alter table Talk add column descriptionFR text;
update Talk set descriptionFR = description;
alter table Talk drop column description;

alter table Talk add column titleEN text;
alter table Talk add column titleFR text;
update Talk set titleFR = title;
alter table Talk drop column title;

alter table Talk add column level varchar(255);
alter table Talk add column theme_id int8;

alter table Talk add column isBreak2 varchar(255);
update Talk set isBreak2 = "NotABreak" where isBreak = false;
update Talk set isBreak2 = "CofeeBreak" where isBreak = true;
alter table Talk drop column isBreak;
alter table Talk rename column isBreak2 to isBreak;
 
    create table TalkTheme (
        id int8 not null,
        color varchar(255),
        theme varchar(255),
        primary key (id)
    );

     alter table Talk 
        add constraint FK27A8CCADDAF92A 
        foreign key (theme_id) 
        references TalkTheme;


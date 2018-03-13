alter table talk add column type_id int8;
 
create table TalkType (
    id int8 not null,
    typeEN varchar(255),
    typeFR varchar(255),
    primary key (id)
);
 
alter table Talk 
   add constraint FK27A8CC443DE56A 
   foreign key (type_id) 
   references TalkType;

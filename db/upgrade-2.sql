    drop table Configuration cascade;

    drop table Organiser cascade;
 
    create table Configuration (
        id int8 not null,
        key varchar(255),
        value varchar(255),
        primary key (id)
    );

    create table Organiser (
        id int8 not null,
        biography text,
        blogURL varchar(255),
        cfp bool not null,
        company varchar(255),
        companyURL varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        orga bool not null,
        photo varchar(255),
        title varchar(255),
        twitterAccount varchar(255),
        primary key (id)
    );


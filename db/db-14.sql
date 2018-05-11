    alter table Talk 
        drop constraint FK27A8CCADDAF92A;

    alter table Talk 
        drop constraint FK27A8CC443DE56A;

    alter table Talk 
        drop constraint FK27A8CCF6F65936;

    alter table Talk 
        drop constraint FK27A8CCD1E27E1E;

    alter table talk_speaker 
        drop constraint FK394CF6C2CE6941;

    alter table talk_speaker 
        drop constraint FK394CF6CF3D5575E;

    drop table Configuration cascade;

    drop table News cascade;

    drop table Organiser cascade;

    drop table PreviousSpeaker cascade;

    drop table Slot cascade;

    drop table Speaker cascade;

    drop table Sponsor cascade;

    drop table Talk cascade;

    drop table TalkTheme cascade;

    drop table TalkType cascade;

    drop table TemporarySlot cascade;

    drop table Track cascade;

    drop table talk_speaker cascade;

    drop table user_table cascade;

    drop sequence hibernate_sequence;

    create table Configuration (
        id int8 not null,
        key varchar(255),
        value varchar(255),
        primary key (id)
    );

    create table News (
        id int8 not null,
        contents text,
        publishedDate timestamp,
        title varchar(255),
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

    create table PreviousSpeaker (
        id int8 not null,
        company varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        photo varchar(255),
        year int4,
        primary key (id)
    );

    create table Slot (
        id int8 not null,
        endDate timestamp,
        startDate timestamp,
        primary key (id)
    );

    create table Speaker (
        id int8 not null,
        biography text,
        blogURL varchar(255),
        company varchar(255),
        companyURL varchar(255),
        email varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        photo varchar(255),
        star bool not null,
        title varchar(255),
        twitterAccount varchar(255),
        primary key (id)
    );

    create table Sponsor (
        id int8 not null,
        about text,
        aboutEN text,
        company varchar(255),
        companyURL varchar(255),
        height int4,
        level varchar(255),
        logo varchar(255),
        twitterAccount varchar(255),
        width int4,
        primary key (id)
    );

    create table Talk (
        id int8 not null,
        descriptionEN text,
        descriptionFR text,
        isBreak varchar(255),
        isHiddenInTalksPage bool not null,
        language varchar(255),
        level varchar(255),
        nbLikes int4,
        slidesUrl varchar(255),
        titleEN varchar(255),
        titleFR varchar(255),
        slot_id int8,
        theme_id int8,
        track_id int8,
        type_id int8,
        primary key (id)
    );

    create table TalkTheme (
        id int8 not null,
        color varchar(255),
        theme varchar(255),
        primary key (id)
    );

    create table TalkType (
        id int8 not null,
        typeEN varchar(255),
        typeFR varchar(255),
        primary key (id)
    );

    create table TemporarySlot (
        id int8 not null,
        endDate timestamp,
        labelEN varchar(255),
        labelFR varchar(255),
        startDate timestamp,
        primary key (id)
    );

    create table Track (
        id int8 not null,
        isJUDCon bool not null,
        position int4 not null,
        title varchar(255),
        primary key (id)
    );

    create table talk_speaker (
        talk_id int8 not null,
        speakers_id int8 not null
    );

    create table user_table (
        id int8 not null,
        firstName varchar(255),
        lastName varchar(255),
        password varchar(255),
        userName varchar(255),
        primary key (id)
    );

    alter table Talk 
        add constraint FK27A8CCADDAF92A 
        foreign key (theme_id) 
        references TalkTheme;

    alter table Talk 
        add constraint FK27A8CC443DE56A 
        foreign key (type_id) 
        references TalkType;

    alter table Talk 
        add constraint FK27A8CCF6F65936 
        foreign key (track_id) 
        references Track;

    alter table Talk 
        add constraint FK27A8CCD1E27E1E 
        foreign key (slot_id) 
        references Slot;

    alter table talk_speaker 
        add constraint FK394CF6C2CE6941 
        foreign key (speakers_id) 
        references Speaker;

    alter table talk_speaker 
        add constraint FK394CF6CF3D5575E 
        foreign key (talk_id) 
        references Talk;

    create sequence hibernate_sequence;

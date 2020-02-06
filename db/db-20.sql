    alter table Talk 
       drop constraint FKojjqgj882m094k8krq50l6s9f;

    alter table Talk 
       drop constraint FKa52ovciu4texkr2loi3gq8xns;

    alter table Talk 
       drop constraint FKab1vdtaevhgbtbu8cdu4en2wl;

    alter table Talk 
       drop constraint FKfkv0wo4nkx7d0qvqno8p86el4;

    alter table talk_speaker 
       drop constraint FKt0drkd7rfm8csipawpfa8pch2;

    alter table talk_speaker 
       drop constraint FKm4spa6xc9qpx4fqgj92del8f4;

    drop table if exists Configuration cascade;

    drop table if exists News cascade;

    drop table if exists Organiser cascade;

    drop table if exists PreviousSpeaker cascade;

    drop table if exists PricePack cascade;

    drop table if exists PricePackDate cascade;

    drop table if exists Slot cascade;

    drop table if exists Speaker cascade;

    drop table if exists Sponsor cascade;

    drop table if exists Talk cascade;

    drop table if exists talk_speaker cascade;

    drop table if exists TalkTheme cascade;

    drop table if exists TalkType cascade;

    drop table if exists TemporarySlot cascade;

    drop table if exists Track cascade;

    drop table if exists user_table cascade;

    drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;

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
        cfp boolean not null,
        company varchar(255),
        companyURL varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        orga boolean not null,
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

    create table PricePack (
       id int8 not null,
        blindBirdPrice int4,
        earlyBirdPrice int4,
        regularPrice int4,
        soldOut boolean,
        studentPrice int4,
        type varchar(255),
        primary key (id)
    );

    create table PricePackDate (
       id int8 not null,
        blindBirdEndDate timestamp,
        earlyBirdEndDate timestamp,
        regularEndDate timestamp,
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
        importId varchar(255),
        lastName varchar(255),
        phone varchar(255),
        photo varchar(255),
        star boolean not null,
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
        linkedInAccount varchar(255),
        logo varchar(255),
        otherURL varchar(255),
        twitterAccount varchar(255),
        width int4,
        primary key (id)
    );

    create table Talk (
       id int8 not null,
        descriptionEN text,
        descriptionFR text,
        importId varchar(255),
        isBreak varchar(255),
        isHiddenInTalksPage boolean not null,
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

    create table talk_speaker (
       talk_id int8 not null,
        speakers_id int8 not null
    );

    create table TalkTheme (
       id int8 not null,
        color varchar(255),
        importId varchar(255),
        theme varchar(255),
        primary key (id)
    );

    create table TalkType (
       id int8 not null,
        importId varchar(255),
        typeEN varchar(255),
        typeFR varchar(255),
        primary key (id)
    );

    create table TemporarySlot (
       id int8 not null,
        endDate timestamp,
        isBreak varchar(255),
        labelEN varchar(255),
        labelFR varchar(255),
        startDate timestamp,
        primary key (id)
    );

    create table Track (
       id int8 not null,
        isJUDCon boolean not null,
        position int4 not null,
        title varchar(255),
        primary key (id)
    );

    create table user_table (
       id int8 not null,
        firstName varchar(255),
        isBCrypt boolean,
        lastName varchar(255),
        password varchar(255),
        userName varchar(255),
        primary key (id)
    );

    alter table Talk 
       add constraint FKojjqgj882m094k8krq50l6s9f 
       foreign key (slot_id) 
       references Slot;

    alter table Talk 
       add constraint FKa52ovciu4texkr2loi3gq8xns 
       foreign key (theme_id) 
       references TalkTheme;

    alter table Talk 
       add constraint FKab1vdtaevhgbtbu8cdu4en2wl 
       foreign key (track_id) 
       references Track;

    alter table Talk 
       add constraint FKfkv0wo4nkx7d0qvqno8p86el4 
       foreign key (type_id) 
       references TalkType;

    alter table talk_speaker 
       add constraint FKt0drkd7rfm8csipawpfa8pch2 
       foreign key (speakers_id) 
       references Speaker;

    alter table talk_speaker 
       add constraint FKm4spa6xc9qpx4fqgj92del8f4 
       foreign key (talk_id) 
       references Talk;

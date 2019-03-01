create table PricePack (
    id int8 not null,
    blindBirdPrice int4,
    earlyBirdPrice int4,
    regularPrice int4,
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

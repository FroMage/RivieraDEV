alter table user_table add column isBCrypt boolean;

update user_table set isBCrypt = false;

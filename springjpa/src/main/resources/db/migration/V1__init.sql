DROP TABLE if exists account;
DROP sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table account (id bigint not null, username VARCHAR(255), email VARCHAR(255), password VARCHAR(255), PRIMARY KEY (id));
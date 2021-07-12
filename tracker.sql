drop database tracker;
drop user tracker;
create user tracker with password 'trackerpassword';
create database tracker with template=template0 owner=tracker;
\connect tracker;
alter default privileges grant all on tables to tracker;
alter default privileges grant all on sequences to tracker;

create table track_users(
    user_id integer primary key not null,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(30) not null,
    password text not null,
    image text not null
);
create table track_categories(
    category_id integer primary key not null,
    user_id integer not null,
    title varchar(20) not null,
    description varchar(50) not null
);
alter table track_categories add constraint cat_users_fk
foreign key (user_id) references track_users(user_id);

create table track_transactions(
    transaction_id integer primary key not null,
    category_id integer not null,
    user_id integer not null,
    amount numeric(10,2) not null,
    note varchar(50) not null,
    transaction_date bigint not null
);
alter table track_transactions add constraint  trans_cat_fk
foreign key (category_id) references track_categories(category_id);
alter table track_transactions add constraint trans_users_fk
foreign key (user_id) references track_users(user_id);

create sequence track_users_seq increment 1 start 1;
create sequence track_categories_seq increment 1 start 1;
create sequence track_transactions_seq increment 1 start 1000;
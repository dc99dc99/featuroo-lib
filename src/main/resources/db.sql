create table Experiment(
id varchar(200) NOT NULL PRIMARY KEY,
name varchar(255) NOT NULL UNIQUE,
description varchar(255),
winner varchar(255),
traffic_fraction Double NOT NULL,
is_archived bit NOT NULL,
strategy varchar(255) NOT NULL
);

create table Alternative(
id int auto_increment PRIMARY KEY,
experiment_id varchar(200) NOT NULL,
name varchar(255) NOT NULL,
idx int
);

create table Participation(
id int auto_increment PRIMARY KEY,
client_id varchar(255) NOT NULL,
experiment_id varchar(200) NOT NULL,
alternative_name  varchar(255) NOT NULL,
);
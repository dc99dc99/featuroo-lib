create table Experiment(
id varchar(200),
name varchar(255),
description varchar(255),
winner varchar(255),
traffic_fraction Double,
is_archived bit,
strategy varchar(255)
);

create table Alternative(
experimentId varchar(200),
name varchar(255),
);
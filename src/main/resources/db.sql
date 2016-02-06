create table Experiment(
id varchar(200) NOT NULL,
name varchar(255) NOT NULL,
description varchar(255),
winner varchar(255),
traffic_fraction Double NOT NULL,
is_archived bit NOT NULL,
strategy varchar(255)
);

create table Alternative(
experiment_id varchar(200) NOT NULL,
name varchar(255) NOT NULL,
idx int
);
create table ${achivator_db_table_prefix}CONFIGURATION (
    ID varchar(100) not null,
    VALUE varchar(100),
    PRIMARY KEY (ID)
);
create table ${achivator_db_table_prefix}BACKUPS (
    ID  varchar(255) NOT NULL,
    startedAt datetime,
    finishedAt datetime,
    initiator varchar(255),
    backupFile varchar(255),
    createdAt datetime NOT NULL,
    PRIMARY KEY (ID)
);

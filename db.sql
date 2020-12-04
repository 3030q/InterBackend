create table access_data
(
    access_data_id int auto_increment
        primary key,
    email          varchar(100)                       not null,
    role           enum ('INTERN', 'MENTOR', 'ADMIN') not null,
    constraint access_data_email_uindex
        unique (email)
);

create table hibernate_sequence
(
    next_val bigint null
);

create table mentorship_data
(
    mentor_user_id int not null,
    intern_user_id int not null
);

create table token_data
(
    token              varchar(255)                          not null
        primary key,
    user_id            int                                   not null,
    creation_timestamp timestamp default current_timestamp() not null on update current_timestamp()
);

create table user_data
(
    user_id        int auto_increment
        primary key,
    role           enum ('INTERN', 'MENTOR', 'ADMIN') not null,
    full_name      varchar(100)                       not null,
    email          varchar(100)                       not null,
    about_text     varchar(500)                       null,
    access_data_id int                                null,
    constraint user_data_email_uindex
        unique (email),
    constraint user_data_access_data_access_data_id_fk
        foreign key (access_data_id) references access_data (access_data_id)
);

create table login_data
(
    login       varchar(100) not null,
    bcrypt_pswd varchar(100) not null,
    user_id     int          null,
    constraint login
        unique (login),
    constraint login_data_user_data_user_id_fk
        foreign key (user_id) references user_data (user_id)
);

alter table login_data
    add primary key (login);


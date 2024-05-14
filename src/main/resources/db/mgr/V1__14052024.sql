CREATE table users(
                      id serial primary key,
                      first_name varchar(64) not null,
                      last_name varchar(64) not null,
                      organization varchar(64) not null,
                      org_position varchar(64) not null,
                      email varchar(64),
                      phone_number varchar(64),
                      information varchar(2048)
);
Create table actors(
    id SERIAL PRIMARY KEY,
    subject_id serial not null references users(id),
    username varchar(64) not null UNIQUE,
    password varchar(2048) not null ,
    role varchar(64),
    enabled boolean default false,
    created_at timestamp,
    updated_at timestamp
);
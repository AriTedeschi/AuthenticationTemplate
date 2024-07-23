CREATE TABLE TB_USER (
    uuid VARCHAR(36) PRIMARY KEY,
    user_code VARCHAR(17) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE TB_ROLE (
    id integer PRIMARY KEY,
    name varchar(100) not null
);

insert into TB_ROLE
select
0,
'ADMIN'
WHERE
NOT EXISTS (SELECT 1 from TB_ROLE where name='ADMIN');
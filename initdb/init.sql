CREATE TABLE task(
    id uuid default gen_random_uuid(),
    name VARCHAR(50),
    description VARCHAR(50),
    priority VARCHAR(50),
    primary key (id)
);
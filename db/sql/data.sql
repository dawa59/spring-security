
insert into customers(email, pwd) values
    ('account@debuggeando.com', 'to_be_encoded'),
    ('cards@debuggeando.com', 'to_be_encoded'),
    ('loans@debuggeando.com', 'to_be_encoded'),
    ('balance@debuggeando.com', 'to_be_encoded');

insert into roles (role_name, description, id_customer) values
    ('ROLE_ADMIN', 'cant view account endpoint', 1),
    ('ROLE_ADMIN', 'cant view account endpoint', 2),
    ('ROLE_USER', 'cant view account endpoint', 3),
    ('ROLE_USER', 'cant view account endpoint', 4);

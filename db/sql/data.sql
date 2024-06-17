
insert into customers(email, pwd) values
    ('account@debuggeando.com', 'to_be_encoded'),
    ('cards@debuggeando.com', 'to_be_encoded'),
    ('loans@debuggeando.com', 'to_be_encoded'),
    ('balance@debuggeando.com', 'to_be_encoded')

insert into roles (role_name, description, id_customer) values
    ('VIEW_ACCOUNT', 'cant view account endpoint', 1),
    ('VIEW_CARDS', 'cant view account endpoint', 2),
    ('VIEW_LOANS', 'cant view account endpoint', 3),
    ('VIEW_BALANCE', 'cant view account endpoint', 4),
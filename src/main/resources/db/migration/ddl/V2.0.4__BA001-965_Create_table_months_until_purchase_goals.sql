CREATE table months_until_purchase_goals (id bigint not null primary key, target_months int not null, constraint months_until_purchase_to_goals_fk foreign key (id) references goals (id));
DROP TABLE income_breakdowns;

CREATE TABLE income_breakdowns
(
    id                                bigint         not null auto_increment,
    date_created                      datetime(6),
    last_updated                      datetime(6),
    version                           integer,
    annual_bonus_amount               decimal(19, 2) not null,
    annual_bonus_currency_code        varchar(255)   not null,
    annual_gross_salary_amount        decimal(19, 2) not null,
    annual_gross_salary_currency_code varchar(255)   not null,
    employment_status                 integer,
    monthly_net_salary_amount         decimal(19, 2) not null,
    monthly_net_salary_currency_code  varchar(255)   not null,
    user_id                           varchar(255)   not null,
    primary key (id)
);

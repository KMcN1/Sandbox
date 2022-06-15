CREATE table affordability_projections (id bigint(20) NOT NULL auto_increment, date_created datetime(6) DEFAULT NULL, last_updated datetime(6) DEFAULT NULL, version int(11) DEFAULT NULL, months_until_affordable int(11) DEFAULT NULL, target_deposit_amount decimal(19,2) NOT NULL, target_deposit_currency_code varchar(255) NOT NULL, user_id varchar(255) NOT NULL, fees_and_costs_amount decimal(19, 2) NOT NULL, fees_and_costs_currency_code varchar(255) NOT NULL, PRIMARY KEY (id), UNIQUE KEY UK_p1tq1d5vgny9g22apsoti18qj (user_id));
CREATE table current_deposits (id bigint(20) NOT NULL auto_increment, date_created datetime(6) DEFAULT NULL, last_updated datetime(6) DEFAULT NULL, version int(11) DEFAULT NULL, external_capital_amount decimal(19,2) NOT NULL, external_capital_currency_code varchar(255) NOT NULL, external_capital_description varchar(255) DEFAULT NULL, total_current_deposit_amount decimal(19,2) NOT NULL, total_current_deposit_currency_code varchar(255) NOT NULL, user_id varchar(255) NOT NULL, PRIMARY KEY (id), UNIQUE KEY UK_e3ggxtrdt3b5l434v1eajxm6n (user_id));
CREATE table income_breakdowns (id bigint(20) NOT NULL auto_increment, date_created datetime(6) DEFAULT NULL, last_updated datetime(6) DEFAULT NULL, version int(11) DEFAULT NULL, annual_gross_income_amount decimal(19,2) NOT NULL, annual_gross_income_currency_code varchar(255) NOT NULL, annual_net_bonus_amount decimal(19,2) NOT NULL, annual_net_bonus_currency_code varchar(255) NOT NULL, annual_net_income_amount decimal(19,2) NOT NULL, annual_net_income_currency_code varchar(255) NOT NULL, annual_net_salary_amount decimal(19,2) NOT NULL, annual_net_salary_currency_code varchar(255) NOT NULL, employment_status int(11) DEFAULT NULL, user_id varchar(255) NOT NULL, PRIMARY KEY (id), UNIQUE KEY UK_fyxsmpu046s1f6s7up265h6ki (user_id));
CREATE table mortgage_maximums (id bigint(20) NOT NULL auto_increment, date_created datetime(6) DEFAULT NULL, last_updated datetime(6) DEFAULT NULL, version int(11) DEFAULT NULL, current_max_affordable_mortgage_amount decimal(19,2) NOT NULL, current_max_affordable_mortgage_currency_code varchar(255) NOT NULL, max_affordable_amount decimal(19,2) NOT NULL, max_affordable_currency_code varchar(255) NOT NULL, max_borrowable_amount decimal(19,2) NOT NULL, max_borrowable_currency_code varchar(255) NOT NULL, max_mortgage_amount decimal(19,2) NOT NULL, max_mortgage_currency_code varchar(255) NOT NULL, max_property_value_amount decimal(19,2) NOT NULL, max_property_value_currency_code varchar(255) NOT NULL, repayment_capacity_amount decimal(19,2) NOT NULL, repayment_capacity_currency_code varchar(255) NOT NULL, user_id varchar(255) NOT NULL, PRIMARY KEY (id), UNIQUE KEY UK_e324yvassihpbcci7v270qba7 (user_id));
CREATE table spending_information (id bigint(20) NOT NULL auto_increment, date_created datetime(6) DEFAULT NULL, last_updated datetime(6) DEFAULT NULL, version int(11) DEFAULT NULL, average_monthly_discretionary_spend_amount decimal(19,2) NOT NULL, average_monthly_discretionary_spend_currency_code varchar(255) NOT NULL, average_monthly_non_discretionary_spend_amount decimal(19,2) NOT NULL, average_monthly_non_discretionary_spend_currency_code varchar(255) NOT NULL, average_monthly_rent_amount decimal(19,2) NOT NULL, average_monthly_rent_currency_code varchar(255) NOT NULL, current_month_discretionary_spend_amount decimal(19,2) NOT NULL, current_month_discretionary_spend_currency_code varchar(255) NOT NULL, user_id varchar(255) NOT NULL, PRIMARY KEY (id), UNIQUE KEY UK_dbwwc0bg0egj64dx1jku7wjy1 (user_id));
CREATE table ba_user (id bigint auto_increment primary key, user_id varchar (255) not null, identity_provider_user_id varchar (255) not null, username varchar (255) not null, date_of_birth datetime (6) default '1900-01-01 00:00:00.000000' not null, date_created datetime (6) null, last_updated datetime (6) null, version int null, email varchar (255) not null, first_name varchar (255) not null, last_name varchar (255) not null, phone varchar (255) null, status varchar (255) null, constraint email unique (email), constraint identity_provider_user_id unique (identity_provider_user_id), constraint user_id unique (user_id), constraint username unique (username));
CREATE index idx_identity_provider_user_id on ba_user (identity_provider_user_id);
CREATE index idx_user_id on ba_user (user_id);
CREATE index idx_username on ba_user (username);
CREATE table categories (id bigint not null primary key, name varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE table category_mapping_description (id bigint not null primary key, description varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE index idx_category_description on category_mapping_description (description);
CREATE table category_mappings (id bigint not null, category_id bigint not null, date_created datetime (6) null, last_updated datetime (6) null, version int null, primary key (id, category_id), constraint category_mappings_ibfk_1 foreign key (category_id) references categories (id) on DELETE cascade);
CREATE index category_id on category_mappings (category_id);
CREATE table external_deposit_capital (id bigint auto_increment primary key, user_id varchar (255) not null, amount decimal (19, 4) not null, currency_code varchar (50) not null, description varchar (255) null, date_created datetime (6) null, last_updated datetime (6) null, version int null, constraint external_deposit_capital_ibfk_1 foreign key (user_id) references ba_user (user_id) on DELETE cascade);
CREATE index idx_user_id on external_deposit_capital (user_id);
CREATE table goals (id bigint auto_increment primary key, date_created datetime (6) null, last_updated datetime (6) null, version int null, goal_type varchar (255) not null, parent_goal_id bigint null, state varchar (255) not null, user_id varchar (255) not null);
CREATE table category_spending_goals (id bigint not null primary key, category_id int not null, category_name varchar (255) not null, monthly_spending_target_amount decimal (19, 2) not null, monthly_spending_target_currency_code varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null, constraint FKrxarcsl9iyl2vs6p0dnaparsf foreign key (id) references goals (id));
CREATE table deposit_goals (id bigint not null primary key, loan_to_value decimal (19, 2) not null, constraint FKf0rcqi1ab0tdp42g9mklh4yjx foreign key (id) references goals (id));
CREATE table linked_account (id bigint auto_increment primary key, date_created datetime (6) null, last_updated datetime (6) null, version int null, account_id varchar (255) not null, linked_date_time datetime (6) null, institution_id varchar (255) not null, is_used_for_deposit bit null, user_id varchar (255) not null, constraint constraint_institution_account_user unique (institution_id, account_id, user_id));
CREATE table mortgage_limits (id bigint auto_increment primary key, current_affordability_amount decimal (19, 2) null, current_affordability_currency_code varchar (255) null, current_deposit_amount decimal (19, 2) null, current_deposit_currency_code varchar (255) null, current_external_capital_item_amount decimal (19, 2) null, current_external_capital_item_currency_code varchar (255) null, maximum_possible_mortgage_amount decimal (19, 2) null, maximum_possible_mortgage_currency_code varchar (255) null, repayment_capacity_amount decimal (19, 2) null, repayment_capacity_currency_code varchar (255) null);
CREATE table affordability (id bigint auto_increment primary key, current_month_discretionary_spend_amount decimal (19, 2) null, current_month_discretionary_spend_currency_code varchar (255) null, gross_annual_income_amount decimal (19, 2) null, gross_annual_income_currency_code varchar (255) null, gross_annual_income_user_override_amount decimal (19, 2) null, gross_annual_income_user_override_currency_code varchar (255) null, gross_monthly_income_amount decimal (19, 2) null, gross_monthly_income_currency_code varchar (255) null, mean_discretionary_spend_amount decimal (19, 2) null, mean_discretionary_spend_currency_code varchar (255) null, mean_non_discretionary_spend_amount decimal (19, 2) null, mean_non_discretionary_spend_currency_code varchar (255) null, months_until_affordable int null, net_monthly_income_amount decimal (19, 2) null, net_monthly_income_currency_code varchar (255) null, net_monthly_salary_amount decimal (19, 2) null, net_monthly_salary_currency_code varchar (255) null, target_deposit_amount decimal (19, 2) null, target_deposit_currency_code varchar (255) null, user_id varchar (255) not null, mortgage_limits_id bigint null, employment_status int null, net_annual_base_pay_user_override_amount decimal (19, 2) null, net_annual_base_pay_user_override_currency_code varchar (255) null, net_annual_bonus_user_override_amount decimal (19, 2) null, net_annual_bonus_user_override_currency_code varchar (255) null, net_annual_income_user_override_amount decimal (19, 2) null, net_annual_income_user_override_currency_code varchar (255) null, constraint UK_efk750l60twnwargk28sq91tj unique (user_id), constraint FK3860er2205vihnm9xyqul7t3u foreign key (mortgage_limits_id) references mortgage_limits (id));
CREATE table mortgage_offer_matrix (id bigint not null primary key, apr decimal (19, 2) null, ltv_max decimal (19, 2) null, ltv_min decimal (19, 2) null, mortgage_amount_max decimal (19, 2) null, mortgage_amount_min decimal (19, 2) null, mortgage_duration_years decimal (19, 2) null);
CREATE table onboarding_phase (id binary (255) not null primary key, end_date date null, prefix varchar (255) null);
CREATE table open_banking_consent (refresh_token varchar (255) not null primary key, credentials_id varchar (255) not null, user_id varchar (255) not null, provider_id varchar (255) not null, consent_status varchar (255) not null, consent_status_updated_at datetime (6) null, consent_created_at datetime (6) null, consent_expires_at datetime (6) null, date_created datetime (6) null, last_updated datetime (6) null, version int null, constraint open_banking_consent_unique_per_user unique (user_id, provider_id));
CREATE index idx_ob_consent_user_id on open_banking_consent (user_id);
CREATE table open_banking_consent_scope (refresh_token varchar (255) not null, scope_name varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null, primary key (refresh_token, scope_name), constraint open_banking_consent_scope_ibfk_1 foreign key (refresh_token) references open_banking_consent (refresh_token) on DELETE cascade);
CREATE table overall_spending_goals (id bigint not null primary key, monthly_spending_target_amount decimal (19, 2) not null, monthly_spending_target_currency_code varchar (255) not null, constraint FKgg1kwkbq064f8i6pslnkaa4j6 foreign key (id) references goals (id));
CREATE table property_goals (id bigint not null primary key, num_of_bedrooms int not null, property_postal_code varchar (255) not null, property_postal_town varchar (255) not null, property_type varchar (255) not null, property_value_amount decimal (19, 2) not null, property_value_currency_code varchar (255) not null, constraint FKk2c1ijuq9jn034ma8t06iayep foreign key (id) references goals (id));
CREATE table role (id bigint auto_increment primary key, description varchar (255) not null, name varchar (255) not null, constraint roleUniqueName unique (name));
CREATE table ba_user_roles (bauser_id bigint not null, roles_id bigint not null, constraint FKob5ica4vamh0cr3nynmssbacn foreign key (roles_id) references role (id) on UPDATE cascade on DELETE cascade, constraint FKqe4le7np77cvhii8rgldnjxej foreign key (bauser_id) references ba_user (id) on UPDATE cascade on DELETE cascade);
CREATE table savings_goals (id bigint not null primary key, monthly_savings_amount_amount decimal (19, 2) not null, monthly_savings_amount_currency_code varchar (255) not null, constraint FKmklohsdiofivw2ykfaa50gy68 foreign key (id) references goals (id));
CREATE table tax_band_names (id bigint not null primary key, band_name varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE table tax_frequency (id bigint not null primary key, frequency_name varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE table tax_region (id bigint not null primary key, region_name varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE table tax_type (id bigint not null primary key, type varchar (255) not null, date_created datetime (6) null, last_updated datetime (6) null, version int null);
CREATE table tax_bands (tax_region_id bigint not null, tax_type_id bigint not null, band_id bigint not null, frequency_id bigint not null, lower_limit decimal (19, 4) not null, upper_limit decimal (19, 4) not null, rate decimal (5, 2) null, date_created datetime (6) null, last_updated datetime (6) null, version int null, primary key (tax_region_id, tax_type_id, band_id, frequency_id), constraint tax_bands_ibfk_1 foreign key (tax_region_id) references tax_region (id) on DELETE cascade, constraint tax_bands_ibfk_2 foreign key (tax_type_id) references tax_type (id) on DELETE cascade, constraint tax_bands_ibfk_3 foreign key (band_id) references tax_band_names (id) on DELETE cascade, constraint tax_bands_ibfk_4 foreign key (frequency_id) references tax_frequency (id) on DELETE cascade);
CREATE index band_id on tax_bands (band_id);
CREATE index frequency_id on tax_bands (frequency_id);
CREATE index tax_type_id on tax_bands (tax_type_id);
CREATE table ts_and_cs_consent (id bigint auto_increment primary key, date_created datetime (6) null, last_updated datetime (6) null, version int null, acceptance_date_time datetime (6) not null, ts_and_cs_version varchar (255) not null, user_id varchar (255) not null);
CREATE table user_feedback (id char (36) not null primary key, version int null, date_created datetime null, last_updated datetime null, name varchar (255) null, email varchar (255) null, comment varchar (255) null, os_type varchar (255) null, app_version varchar (255) null, feedback_screen_name varchar (255) null, feedback_reason varchar (255) null);
CREATE table user_onboarding_status (id bigint auto_increment primary key, date_created datetime (6) null, last_updated datetime (6) null, version int null, user_id varchar (255) not null, last_step int null, constraint UK_7n74ux9mtew8hwxmk7lmkr6n4 unique (user_id));
CREATE table absolute_fees (id bigint not null auto_increment, date_created datetime(6), last_updated datetime(6), version integer, fee_amount decimal(19, 2) not null, fee_currency_code varchar(255) not null, fee_type varchar(255) not null, lower_bound_amount decimal(19, 2), lower_bound_currency_code varchar(255), upper_bound_amount decimal(19, 2), upper_bound_currency_code varchar(255), primary key (id));
CREATE table relative_fees (id bigint not null auto_increment, date_created datetime(6), last_updated datetime(6), version integer, fee_factor decimal(19, 2) not null, fee_type varchar(255) not null, lower_bound_amount decimal(19, 2), lower_bound_currency_code varchar(255), upper_bound_amount decimal(19, 2), upper_bound_currency_code varchar(255), primary key (id));
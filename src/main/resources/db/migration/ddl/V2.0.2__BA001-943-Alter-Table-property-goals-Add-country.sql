ALTER TABLE property_goals
    ADD COLUMN country    varchar(255),
    ADD COLUMN tax_region varchar(255);

ALTER TABLE property_goals
    MODIFY num_of_bedrooms int(11) NULL,
    MODIFY property_postal_code varchar(255) NULL,
    MODIFY property_postal_town varchar(255) NULL,
    MODIFY property_type varchar(255) NULL;
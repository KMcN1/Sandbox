DROP TABLE user_feedback;

CREATE table user_feedback
(
    id                   bigint not null auto_increment key,
    version              int null,
    date_created         datetime null,
    last_updated         datetime null,
    name                 varchar(255) null,
    email                varchar(255) null,
    comment              varchar(255) null,
    os_type              varchar(255) null,
    app_version          varchar(255) null,
    feedback_screen_name varchar(255) null,
    feedback_reason      varchar(255) null
);



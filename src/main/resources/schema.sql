create table blacklist
(
    blacklist_id  bigint      not null auto_increment,
    restaurant_id bigint      not null,
    user_id       bigint      not null,
    is_deleted    bit(1)      not null,
    created_date  datetime(6) not null,
    modified_date datetime(6) not null,
    primary key (blacklist_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table blacklist_demand
(
    blacklist_demand_id bigint       not null auto_increment,
    restaurant_id       bigint       not null,
    user_id             bigint       not null,
    description         varchar(200) not null,
    status              varchar(30)  not null,
    created_date        datetime(6)  not null,
    modified_date       datetime(6)  not null,
    primary key (blacklist_demand_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table category
(
    category_id bigint      not null auto_increment,
    parent_id   bigint,
    name        varchar(20) not null,
    primary key (category_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table category_restaurant
(
    category_restaurant_id bigint not null auto_increment,
    category_id            bigint not null,
    restaurant_id          bigint not null,
    primary key (category_restaurant_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table coupon_creator
(
    coupon_creator_id bigint       not null auto_increment,
    event_id          bigint       not null,
    name              varchar(100) not null,
    discount_price    integer      not null,
    discount_type     varchar(20)  not null,
    quantity          integer      not null,
    expire_date       datetime(6)  not null,
    is_deleted        bit(1)       not null,
    version           bigint       not null,
    created_date      datetime(6)  not null,
    modified_date     datetime(6)  not null,
    primary key (coupon_creator_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table event
(
    event_id         bigint       not null auto_increment,
    restaurant_id    bigint,
    name             varchar(30) not null,
    event_end_date   datetime(6)  not null,
    event_start_date datetime(6)  not null,
    is_deleted       bit(1)       not null,
    created_date     datetime(6)  not null,
    modified_date    datetime(6)  not null,
    primary key (event_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table lineup
(
    lineup_id      bigint      not null auto_increment,
    restaurant_id  bigint      not null,
    user_id        bigint      not null,
    waiting_number integer     not null,
    num_of_members integer     not null,
    status         varchar(20) not null,
    call_count     integer     not null,
    arrived_at     datetime(6),
    is_reviewed    bit(1)      not null,
    is_deleted     bit(1)      not null,
    created_date   datetime(6) not null,
    modified_date  datetime(6) not null,
    primary key (lineup_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table lineup_history
(
    lineup_history_id bigint      not null auto_increment,
    restaurant_id     bigint      not null,
    user_id           bigint      not null,
    waiting_number    integer     not null,
    num_of_members    integer     not null,
    status            varchar(20) not null,
    call_count        integer     not null,
    started_at        datetime(6) not null,
    arrived_at        datetime(6),
    is_reviewed       bit(1)      not null,
    is_deleted        bit(1)      not null,
    created_date      datetime(6) not null,
    modified_date     datetime(6) not null,
    primary key (lineup_history_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table menu
(
    menu_id       bigint      not null auto_increment,
    restaurant_id bigint      not null,
    name          varchar(50) not null,
    price         integer     not null,
    image_path        varchar(255),
    is_deleted    bit(1)      not null,
    created_date  datetime(6) not null,
    modified_date datetime(6) not null,
    primary key (menu_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table restaurant
(
    restaurant_id       bigint       not null auto_increment,
    user_id             bigint       not null,
    name                varchar(100) not null,
    image_paths         varchar(255),
    description         varchar(200) not null,
    capacity            integer      not null,
    address             varchar(50)  not null,
    detail_address      varchar(30)  not null,
    zip_code            varchar(5)   not null,
    phone_number        varchar(13)  not null,
    search_keywords     varchar(255),
    business_license_no varchar(12)  not null,
    latitude            double       not null,
    longitude           double       not null,
    is_deleted          bit(1)       not null,
    created_date        datetime(6)  not null,
    modified_date       datetime(6)  not null,
    primary key (restaurant_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table restaurant_info
(
    restaurant_info_id     bigint      not null auto_increment,
    restaurant_id          bigint      not null,
    current_waiting_number integer     not null,
    is_lineup_active       bit(1)      not null,
    rate                   float       not null,
    open_time              varchar(5),
    close_time             varchar(5),
    total_lineup           integer     not null,
    total_review           integer     not null,
    is_deleted             bit(1)      not null,
    created_date           datetime(6) not null,
    modified_date          datetime(6) not null,
    primary key (restaurant_info_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table review
(
    review_id     bigint       not null auto_increment,
    restaurant_id bigint       not null,
    user_id       bigint       not null,
    rate          integer      not null,
    image_paths   varchar(255),
    content       varchar(200) not null,
    is_deleted    bit(1)       not null,
    created_date  datetime(6)  not null,
    modified_date datetime(6)  not null,
    primary key (review_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table seller_management
(
    seller_management_id bigint       not null auto_increment,
    restaurant_name      varchar(100) not null,
    categories           varchar(255) not null,
    description          varchar(200) not null,
    name                 varchar(30)  not null,
    business_license_no  varchar(12) not null,
    username             varchar(20)  not null,
    email                varchar(255) not null,
    phone_number         varchar(13)  not null,
    address              varchar(50) not null,
    detail_address       varchar(30) not null,
    zip_code             varchar(5)   not null,
    status               varchar(20) not null,
    latitude             double       not null,
    longitude            double       not null,
    created_date         datetime(6)  not null,
    modified_date        datetime(6)  not null,
    primary key (seller_management_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table user
(
    user_id       bigint       not null auto_increment,
    username      varchar(20)  not null,
    password      varchar(255) not null,
    nickname      varchar(15),
    name          varchar(30)  not null,
    email         varchar(255) not null,
    phone_number  varchar(13)  not null,
    role          varchar(20) not null,
    is_banned     bit(1)       not null,
    is_deleted    bit(1)       not null,
    created_date  datetime(6)  not null,
    modified_date datetime(6)  not null,
    primary key (user_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table user_coupon
(
    user_coupon_id    bigint not null auto_increment,
    coupon_creator_id bigint not null,
    user_id           bigint not null,
    is_used           bit(1) not null,
    primary key (user_coupon_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table waiting_number
(
    waiting_number_id bigint  not null auto_increment,
    restaurant_id     bigint  not null,
    next_number       integer not null,
    version           bigint  not null,
    primary key (waiting_number_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


alter table blacklist
    add constraint fk_blacklist_restaurant_id
        foreign key (restaurant_id) references restaurant (restaurant_id),
    add constraint fk_blacklist_user_id
        foreign key (user_id) references user (user_id),
    add index fk_blacklist_restaurant_id (restaurant_id),
    add index fk_blacklist_user_id (user_id);

alter table blacklist_demand
    add constraint fk_blacklist_demand_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint fk_blacklist_demand_user_id
        foreign key (user_id)
            references user (user_id),
    add index fk_blacklist_demand_restaurant_id (restaurant_id),
    add index fk_blacklist_demand_user_id (user_id);

alter table category
    add constraint uk_category_name unique (name);

alter table category_restaurant
    add constraint fk_category_restaurant_category_id
        foreign key (category_id)
            references category (category_id),
    add constraint fk_category_restaurant_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add index fk_category_restaurant_category_id (category_id),
    add index fk_category_restaurant_restaurant_id (restaurant_id);

alter table coupon_creator
    add constraint fk_coupon_creator_event_id
        foreign key (event_id)
            references event (event_id),
    add index fk_coupon_creator_event_id (event_id);

alter table event
    add constraint fk_event_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add index fk_event_restaurant_id (restaurant_id);

alter table lineup
    add constraint fk_lineup_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint fk_lineup_user_id
        foreign key (user_id)
            references user (user_id),
    add index fk_lineup_user_id (user_id),
    add index fk_lineup_restaurant_id (restaurant_id);

alter table lineup_history
    add constraint fk_lineup_history_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint fk_lineup_history_user_id
        foreign key (user_id)
            references user (user_id),
    add index fk_lineup_history_user_id (user_id),
    add index fk_lineup_history_restaurant_id (restaurant_id);

alter table menu
    add constraint fk_menu_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add index fk_menu_restaurant_id (restaurant_id);

alter table restaurant
    add constraint fk_restaurant_user_id
        foreign key (user_id)
            references user (user_id),
    add constraint uk_restaurant_phone_number unique (phone_number);

alter table restaurant_info
    add constraint fk_restaurant_info_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint uk_restaurant_info_restaurant_id unique (restaurant_id);

alter table review
    add constraint fk_review_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint fk_review_user_id
        foreign key (user_id)
            references user (user_id),
    add index fk_review_user_id (user_id),
    add index fk_review_restaurant_id (restaurant_id);

alter table user
    add constraint uk_user_username unique (username),
    add constraint uk_user_nickname unique (nickname),
    add constraint uk_user_phone_number unique (phone_number),
    add constraint uk_user_email unique (email);

alter table user_coupon
    add constraint fk_user_coupon_coupon_creator_id
        foreign key (coupon_creator_id)
            references coupon_creator (coupon_creator_id),
    add constraint fk_user_coupon_user_id
        foreign key (user_id)
            references user (user_id),
    add index fk_user_coupon_coupon_creator_id (coupon_creator_id),
    add index fk_user_coupon_user_id (user_id);

alter table waiting_number
    add constraint fk_waiting_number_restaurant_id
        foreign key (restaurant_id)
            references restaurant (restaurant_id),
    add constraint uk_waiting_number_restaurant_id unique (restaurant_id);
CREATE
DATABASE shop default CHARACTER SET UTF8;

create table shop.item
(
    id            bigint auto_increment primary key,
    created_by    varchar(255) not null,
    created_date  datetime(6) not null,
    modified_by   varchar(255) not null,
    modified_date datetime(6) not null,
    description   varchar(255) null,
    image_path    varchar(255) null,
    name          varchar(50)  not null,
    price         bigint       not null
) engine=InnoDB;

create table shop.cart
(
    id            bigint auto_increment primary key,
    created_by    varchar(255) not null,
    created_date  datetime(6) not null,
    modified_by   varchar(255) not null,
    modified_date datetime(6) not null,
    member_id     bigint null,
    item_id       bigint null
) engine=InnoDB;

create table shop.member
(
    id            bigint auto_increment primary key,
    created_by    varchar(255) not null,
    created_date  datetime(6) not null,
    modified_by   varchar(255) not null,
    modified_date datetime(6) not null,
    email         varchar(255) not null,
    password      varchar(255) not null,
    role          varchar(255) not null,
    username      varchar(255) not null
) engine=InnoDB;

create table shop.`order`
(
    id                bigint auto_increment primary key,
    created_by        varchar(255) not null,
    created_date      datetime(6) not null,
    modified_by       varchar(255) not null,
    modified_date     datetime(6) not null,
    address           varchar(500) not null,
    bank_code         varchar(2) null,
    card_code         varchar(2) null,
    card_number       varchar(50) null,
    member_id         bigint       not null,
    order_status_code varchar(2)   not null,
    pay_type_code     varchar(2)   not null,
    price             bigint       not null
) engine=InnoDB;

create table shop.order_detail
(
    id       bigint auto_increment
        primary key,
    price    bigint not null,
    item_id  bigint null,
    order_id bigint null
) engine=InnoDB;

create table shop.out_box
(
    id                 bigint auto_increment primary key,
    created_by         varchar(255) not null,
    created_date       datetime(6) not null,
    modified_by        varchar(255) not null,
    modified_date      datetime(6) not null,
    fail_reason        varchar(255),
    outbox_status_code varchar(255) not null,
    outbox_type_code   varchar(255) not null,
    payload            TEXT
) engine=InnoDB;

alter table member
    add constraint uk_email unique (email);

alter table cart
    add constraint fk_cart_item_id
        foreign key (item_id)
            references item (id);

alter table order_detail
    add constraint fk_orderDetail_item_id
        foreign key (item_id)
            references item (id);

alter table order_detail
    add constraint fk_orderDetail_order_id
        foreign key (order_id)
            references `order` (id);

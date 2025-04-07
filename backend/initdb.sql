create table tbl_account
(
    id       bigint auto_increment
        primary key,
    user_id  bigint                                                                                                                                  null,
    password varchar(255)                                                                                                                            not null,
    role     enum ('ADMIN', 'CUSTOMER', 'DOCTOR', 'NURSE', 'STAFF')                                                                                  not null,
    status   enum ('ACTIVE', 'BLOCKED', 'DELETED', 'INACTIVE', 'PENDING_ACTIVATION', 'PENDING_BLOCKING', 'PENDING_DELETION', 'PENDING_VERIFICATION') null,
    constraint UKmfb3yc9ce1liugyq0kgqxxwnm
        unique (user_id)
);

create table tbl_admin
(
    admin_role    tinyint                          null,
    date_of_birth date                             null,
    id            bigint                           not null
        primary key,
    address       varchar(255)                     null,
    email         varchar(255)                     null,
    full_name     varchar(255)                     null,
    nation        varchar(255)                     null,
    phone         varchar(255)                     null,
    gender        enum ('FEMALE', 'MALE', 'OTHER') null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone),
    check (`admin_role` between 0 and 1)
);

create table tbl_customer
(
    date_of_birth date                                                               null,
    id            bigint                                                             not null
        primary key,
    address       varchar(255)                                                       null,
    career        varchar(255)                                                       null,
    email         varchar(255)                                                       null,
    full_name     varchar(255)                                                       null,
    nation        varchar(255)                                                       null,
    phone         varchar(255)                                                       null,
    gender        enum ('FEMALE', 'MALE', 'OTHER')                                   null,
    membership    enum ('BRONZE', 'DIAMOND', 'GOLD', 'NORMAL', 'PLATINUM', 'SILVER') null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone)
);

create table tbl_medical_specialty
(
    id    int auto_increment
        primary key,
    price double       null,
    name  text         null,
    note  varchar(255) null
);

create table tbl_doctor
(
    date_of_birth        date                             null,
    medical_specialty_id int                              null,
    id                   bigint                           not null
        primary key,
    address              varchar(255)                     null,
    email                varchar(255)                     null,
    full_name            varchar(255)                     null,
    nation               varchar(255)                     null,
    phone                varchar(255)                     null,
    position             varchar(255)                     null,
    qualification        varchar(255)                     null,
    gender               enum ('FEMALE', 'MALE', 'OTHER') null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone),
    constraint FKlbxcn7cxikrkljglmysjc3t4s
        foreign key (medical_specialty_id) references tbl_medical_specialty (id)
);

create table tbl_medicine
(
    price          double       null,
    id             bigint auto_increment
        primary key,
    medicine_usage varchar(255) null,
    name           varchar(255) null,
    strength       varchar(255) null
);

create table tbl_nurse
(
    date_of_birth date                             null,
    id            bigint                           not null
        primary key,
    address       varchar(255)                     null,
    email         varchar(255)                     null,
    full_name     varchar(255)                     null,
    nation        varchar(255)                     null,
    phone         varchar(255)                     null,
    position      varchar(255)                     null,
    qualification varchar(255)                     null,
    gender        enum ('FEMALE', 'MALE', 'OTHER') null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone)
);

create table tbl_patient
(
    date_of_birth date                             null,
    id            bigint auto_increment
        primary key,
    address       varchar(255)                     null,
    name          varchar(255)                     null,
    phone         varchar(255)                     null,
    gender        enum ('FEMALE', 'MALE', 'OTHER') null
);

create table tbl_medical_record
(
    id         bigint auto_increment
        primary key,
    patient_id bigint       null,
    barcode    varchar(255) null,
    constraint FKpk0p6aqwal3p1jaf6m6d5ea4m
        foreign key (patient_id) references tbl_patient (id)
);

create table tbl_prescription
(
    issue_date date    null,
    status     tinyint null,
    id         bigint auto_increment
        primary key,
    check (`status` between 0 and 2)
);

create table tbl_encounter
(
    visit_date        date         null,
    id                bigint auto_increment
        primary key,
    medical_record_id bigint       null,
    prescription_id   bigint       null,
    diagnosis         varchar(255) null,
    notes             varchar(255) null,
    treatment         varchar(255) null,
    constraint UKg5xc51vh9u0hp05d2xsyfv11c
        unique (prescription_id),
    constraint FKa0003p6u2xrrtdrc3nptvln9c
        foreign key (medical_record_id) references tbl_medical_record (id),
    constraint FKi7846mug4bgj14veaq167r17c
        foreign key (prescription_id) references tbl_prescription (id)
);

create table tbl_prescription_item
(
    quantity        int          null,
    id              bigint auto_increment
        primary key,
    medicine_id     bigint       null,
    prescription_id bigint       null,
    dosage          varchar(255) null,
    name            varchar(255) null,
    unit            varchar(255) null,
    constraint FKa2gl0hbtds9hshpw8vdy6ry07
        foreign key (prescription_id) references tbl_prescription (id),
    constraint FKhxus9135s3539xayk7aqko7wr
        foreign key (medicine_id) references tbl_medicine (id)
);

create table tbl_room_detail
(
    floor    int          null,
    id       int auto_increment
        primary key,
    building varchar(255) null,
    name     varchar(255) null
);

create table tbl_staff
(
    date_of_birth date                                    null,
    id            bigint                                  not null
        primary key,
    address       varchar(255)                            null,
    email         varchar(255)                            null,
    full_name     varchar(255)                            null,
    nation        varchar(255)                            null,
    phone         varchar(255)                            null,
    gender        enum ('FEMALE', 'MALE', 'OTHER')        null,
    staff_role    enum ('SECURITY', 'SERVICE', 'SUPPORT') null,
    constraint UK589idila9li6a4arw1t8ht1gx
        unique (phone)
);

create table tbl_time_slot
(
    end_time   time(6) null,
    id         int auto_increment
        primary key,
    start_time time(6) null
);

create table tbl_doctor_schedule
(
    booked_slots   int    null,
    date           date   null,
    max_slots      int    null,
    room_detail_id int    null,
    time_slot_id   int    null,
    doctor_id      bigint null,
    id             bigint auto_increment
        primary key,
    constraint FKhhnbh9rpr4fhep2i6cevnk4h5
        foreign key (time_slot_id) references tbl_time_slot (id),
    constraint FKs20axpfsoqt7q2rnmuedqepfd
        foreign key (room_detail_id) references tbl_room_detail (id),
    constraint FKtljixsf3pq4w2ml1y0ndpq66q
        foreign key (doctor_id) references tbl_doctor (id)
);

create table tbl_appointment
(
    waiting_number     int                                         null,
    doctor_schedule_id bigint                                      null,
    id                 bigint auto_increment
        primary key,
    medical_record_id  bigint                                      null,
    status             enum ('CANCELLED', 'COMPLETE', 'CONFIRMED') null,
    constraint FKdy5brsoiwlcvr5cosj4c4y8pm
        foreign key (medical_record_id) references tbl_medical_record (id),
    constraint FKogy0vgymnmgjy4m12oj9wnoky
        foreign key (doctor_schedule_id) references tbl_doctor_schedule (id)
);

create table tbl_payment
(
    amount         double                 null,
    payment_status tinyint                null,
    appointment_id bigint                 null,
    id             bigint auto_increment
        primary key,
    payment_date   datetime(6)            null,
    transaction_id varchar(255)           null,
    payment_method enum ('MOMO', 'VNPAY') null,
    constraint UKe9b0fsqkh2871s1f2idv7jkue
        unique (appointment_id),
    constraint FKhf5omma6r24fh55aexb10s3yq
        foreign key (appointment_id) references tbl_appointment (id),
    check (`payment_status` between 0 and 4)
);

create table user_seq
(
    next_val bigint null
);


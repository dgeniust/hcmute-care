create table tbl_medical_specialty
(
    id    int auto_increment
        primary key,
    price decimal(10, 2) null,
    name  varchar(255)   not null,
    note  varchar(255)   null
);

create table tbl_medicine
(
    price          decimal(10, 2) not null,
    id             bigint auto_increment
        primary key,
    medicine_usage varchar(255)   null,
    name           varchar(255)   not null,
    strength       varchar(255)   null
);

create table tbl_patient
(
    date_of_birth date                             null,
    id            bigint auto_increment
        primary key,
    address       varchar(255)                     null,
    career        varchar(255)                     null,
    cccd          varchar(255)                     null,
    email         varchar(255)                     null,
    name          varchar(255)                     null,
    nation        varchar(255)                     null,
    phone         varchar(255)                     null,
    gender        enum ('FEMALE', 'MALE', 'OTHER') null
);

create table tbl_room_detail
(
    floor    int          null,
    id       int auto_increment
        primary key,
    building varchar(255) null,
    name     varchar(255) null
);

create table tbl_time_slot
(
    end_time   time(6) null,
    id         int auto_increment
        primary key,
    start_time time(6) null
);

create table tbl_user
(
    date_of_birth date                             null,
    id            bigint auto_increment
        primary key,
    address       varchar(255)                     null,
    email         varchar(255)                     null,
    full_name     varchar(255)                     null,
    nation        varchar(255)                     null,
    phone         varchar(255)                     null,
    gender        enum ('FEMALE', 'MALE', 'OTHER') null,
    constraint UKd6tho5pxk6qd8xem6vwou8sdp
        unique (phone)
);

create table tbl_account
(
    id       bigint auto_increment
        primary key,
    user_id  bigint                                                                                                                                  null,
    password varchar(255)                                                                                                                            not null,
    role     enum ('ADMIN', 'CUSTOMER', 'DOCTOR', 'NURSE', 'STAFF')                                                                                  not null,
    status   enum ('ACTIVE', 'BLOCKED', 'DELETED', 'INACTIVE', 'PENDING_ACTIVATION', 'PENDING_BLOCKING', 'PENDING_DELETION', 'PENDING_VERIFICATION') null,
    constraint UKmfb3yc9ce1liugyq0kgqxxwnm
        unique (user_id),
    constraint FKnd41d2s8gtijiuqfm61q03ohw
        foreign key (user_id) references tbl_user (id)
);

create table tbl_admin
(
    admin_role tinyint null,
    id         bigint  not null
        primary key,
    constraint FKn6lak0tiqyoogj9a9nrv1pnsx
        foreign key (id) references tbl_user (id),
    check (`admin_role` between 0 and 1)
);

create table tbl_customer
(
    id         bigint                                                             not null
        primary key,
    membership enum ('BRONZE', 'DIAMOND', 'GOLD', 'NORMAL', 'PLATINUM', 'SILVER') null,
    constraint FKjxw20l0ap83d36ntskkmkafiu
        foreign key (id) references tbl_user (id)
);

create table tbl_doctor
(
    medical_specialty_id int          null,
    id                   bigint       not null
        primary key,
    position             varchar(255) null,
    qualification        varchar(255) null,
    constraint FK9rft32ecmrp22kgmx14ki1rnl
        foreign key (id) references tbl_user (id),
    constraint FKlbxcn7cxikrkljglmysjc3t4s
        foreign key (medical_specialty_id) references tbl_medical_specialty (id)
);

create table tbl_medical_record
(
    customer_id bigint       null,
    id          bigint auto_increment
        primary key,
    patient_id  bigint       null,
    barcode     varchar(255) null,
    constraint UKcx3k5wt1byr2g6t5cy5s5f22a
        unique (patient_id),
    constraint UKqwpmlp4vp2f6hqocqqlw2t5ii
        unique (barcode),
    constraint FKlkv1eiawbgbc6qj85gflu2l1d
        foreign key (customer_id) references tbl_customer (id),
    constraint FKpk0p6aqwal3p1jaf6m6d5ea4m
        foreign key (patient_id) references tbl_patient (id)
);

create table tbl_appointment
(
    created_at        datetime(6) null,
    id                bigint auto_increment
        primary key,
    medical_record_id bigint      null,
    updated_at        datetime(6) null,
    constraint FKdy5brsoiwlcvr5cosj4c4y8pm
        foreign key (medical_record_id) references tbl_medical_record (id)
);

create table tbl_encounter
(
    visit_date        date         null,
    id                bigint auto_increment
        primary key,
    medical_record_id bigint       null,
    diagnosis         varchar(255) null,
    notes             varchar(255) null,
    treatment         varchar(255) null,
    constraint FKa0003p6u2xrrtdrc3nptvln9c
        foreign key (medical_record_id) references tbl_medical_record (id)
);

create table tbl_medical_test
(
    encounter_id bigint       not null,
    id           bigint auto_increment
        primary key,
    evaluate     varchar(255) null,
    notes        varchar(255) null,
    constraint FK7xkfdl55rhuc65a7i1ke4yvbq
        foreign key (encounter_id) references tbl_encounter (id)
);

create table tbl_functional_tests
(
    is_invasive     bit          null,
    is_quantitative bit          null,
    record_duration int          null,
    id              bigint       not null
        primary key,
    organ_system    varchar(255) null,
    test_name       varchar(255) null,
    constraint FKidnbqi1mi7orykdvssaoy3po5
        foreign key (id) references tbl_medical_test (id)
);

create table tbl_cardiac_test
(
    id    bigint                                      not null
        primary key,
    image varchar(255)                                null,
    type  enum ('ECG', 'HolterMonitor', 'StressTest') null,
    constraint FKb78iwlk2507w08vmsh1r1vtna
        foreign key (id) references tbl_functional_tests (id)
);

create table tbl_digestive_tests
(
    duration int          null,
    id       bigint       not null
        primary key,
    image    varchar(255) null,
    constraint FKtq0k3y10uyf3pjwguf90pwb7a
        foreign key (id) references tbl_functional_tests (id)
);

create table tbl_imaging_test
(
    id         bigint       not null
        primary key,
    pdf_result varchar(255) null,
    constraint FK4emm9irvq5viofm93yf8bl75g
        foreign key (id) references tbl_medical_test (id)
);

create table tbl_laboratory_test
(
    gra  float  null,
    hct  float  null,
    hgb  float  null,
    lym  float  null,
    mch  float  null,
    mcv  float  null,
    momo float  null,
    plt  float  null,
    rbc  float  null,
    wbc  float  null,
    id   bigint not null
        primary key,
    constraint FKjn23hbr33rdeeb5u3159w7q20
        foreign key (id) references tbl_medical_test (id)
);

create table tbl_neuro_test
(
    id    bigint       not null
        primary key,
    image varchar(255) null,
    constraint FKte68av9wnbuqa55oqxf1olvsn
        foreign key (id) references tbl_functional_tests (id)
);

create table tbl_eeg
(
    channels        int    null,
    detects_seizure bit    null,
    id              bigint not null
        primary key,
    constraint FKcno51xe1yh6e33pfr4dqde3fu
        foreign key (id) references tbl_neuro_test (id)
);

create table tbl_emg
(
    id           bigint       not null
        primary key,
    muscle_group varchar(255) null,
    constraint FKkp2v0g0hg7g6ab5xpewk9vffh
        foreign key (id) references tbl_neuro_test (id)
);

create table tbl_notification
(
    is_read    bit                                                       null,
    created_at datetime(6)                                               null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6)                                               null,
    user_id    bigint                                                    null,
    content    varchar(255)                                              null,
    title      varchar(255)                                              null,
    type       enum ('APPOINTMENT', 'PAYMENT', 'PRESCRIPTION', 'SYSTEM') null,
    constraint FK17xlvi4d2o1r18carkq5kmd3c
        foreign key (user_id) references tbl_user (id)
);

create table tbl_nurse
(
    medical_specialty_id int          null,
    id                   bigint       not null
        primary key,
    position             varchar(255) null,
    qualification        varchar(255) null,
    constraint FKhnbwxio26a4qllnx2ben331r0
        foreign key (id) references tbl_user (id),
    constraint FKkuk4sauxai6rpm16j8h0vfh83
        foreign key (medical_specialty_id) references tbl_medical_specialty (id)
);

create table tbl_payment
(
    amount         decimal(10, 2)                                                   null,
    appointment_id bigint                                                           null,
    id             bigint auto_increment
        primary key,
    payment_date   datetime(6)                                                      null,
    transaction_id varchar(255)                                                     null,
    payment_method enum ('MOMO', 'VNPAY')                                           null,
    payment_status enum ('CANCELLED', 'COMPLETED', 'FAILED', 'PENDING', 'REFUNDED') null,
    constraint UKe9b0fsqkh2871s1f2idv7jkue
        unique (appointment_id),
    constraint FKhf5omma6r24fh55aexb10s3yq
        foreign key (appointment_id) references tbl_appointment (id)
);

create table tbl_prescription
(
    encounter_id bigint                                    null,
    id           bigint auto_increment
        primary key,
    issue_date   datetime(6)                               not null,
    status       enum ('CANCELLED', 'PENDING', 'RECEIVED') null,
    constraint FKr5prfsfs4y65t9wsbuxcy1bn2
        foreign key (encounter_id) references tbl_encounter (id)
);

create table tbl_prescription_item
(
    quantity        int          null,
    id              bigint auto_increment
        primary key,
    medicine_id     bigint       null,
    prescription_id bigint       null,
    dosage          varchar(255) null,
    unit            varchar(255) null,
    constraint FKa2gl0hbtds9hshpw8vdy6ry07
        foreign key (prescription_id) references tbl_prescription (id),
    constraint FKhxus9135s3539xayk7aqko7wr
        foreign key (medicine_id) references tbl_medicine (id)
);

create table tbl_respiratory_test
(
    id               bigint       not null
        primary key,
    patient_position varchar(255) null,
    test_environment varchar(255) null,
    constraint FKe7x4utgcksjmoeblegngau1uy
        foreign key (id) references tbl_functional_tests (id)
);

create table tbl_blood_gas_analysis
(
    pco2 float  null,
    ph   float  null,
    po2  float  null,
    id   bigint not null
        primary key,
    constraint FKl2e1x2t55te4yc7j1xt2cc0na
        foreign key (id) references tbl_respiratory_test (id)
);

create table tbl_nerve_conduction
(
    conduction_speed float        null,
    id               bigint       not null
        primary key,
    nerve            varchar(255) null,
    constraint FK5k25s9imkjr5osx822abmmgqq
        foreign key (id) references tbl_respiratory_test (id)
);

create table tbl_schedule
(
    date           date        null,
    max_slots      int         null,
    room_detail_id int         null,
    created_at     datetime(6) null,
    doctor_id      bigint      null,
    id             bigint auto_increment
        primary key,
    updated_at     datetime(6) null,
    constraint FK55yk8g8j89o9ackfn8d81km4p
        foreign key (room_detail_id) references tbl_room_detail (id),
    constraint FKh07cf18sxludbts9gvtbo7h8r
        foreign key (doctor_id) references tbl_doctor (id)
);

create table tbl_schedule_slot
(
    booked_slots int         null,
    time_slot_id int         null,
    created_at   datetime(6) null,
    id           bigint auto_increment
        primary key,
    schedule_id  bigint      null,
    updated_at   datetime(6) null,
    constraint FKf1bufx6pjpq5c856fhr6b7mr0
        foreign key (schedule_id) references tbl_schedule (id),
    constraint FKh0ca0h76554fa3g5u56gpyf63
        foreign key (time_slot_id) references tbl_time_slot (id)
);

create table tbl_spirometry
(
    fevl float  null,
    fvc  float  null,
    id   bigint not null
        primary key,
    constraint FK52hxqpfuty6m6sh800mbd9wfa
        foreign key (id) references tbl_respiratory_test (id)
);

create table tbl_staff
(
    id         bigint                                  not null
        primary key,
    staff_role enum ('SECURITY', 'SERVICE', 'SUPPORT') null,
    constraint FK18simt6qg1vlckulx8cdidtu4
        foreign key (id) references tbl_user (id)
);

create table tbl_post
(
    date_of_create date         null,
    id             bigint auto_increment
        primary key,
    staff_id       bigint       null,
    content        varchar(255) null,
    header         varchar(255) null,
    constraint FK9l2o8xiak0puo5rexdrssnwkc
        foreign key (staff_id) references tbl_staff (id)
);

create table tbl_post_image
(
    id        bigint auto_increment
        primary key,
    post_id   bigint       null,
    image_url varchar(255) null,
    constraint FKgu0k1ycm57rgt76r10atw8f8i
        foreign key (post_id) references tbl_post (id)
);

create table tbl_ticket
(
    waiting_number   int                                                                null,
    appointment_id   bigint                                                             not null,
    created_at       datetime(6)                                                        null,
    id               bigint auto_increment
        primary key,
    schedule_slot_id bigint                                                             not null,
    updated_at       datetime(6)                                                        null,
    ticket_code      varchar(255)                                                       null,
    status           enum ('CANCELLED', 'COMPLETED', 'CONFIRMED', 'NO_SHOW', 'PENDING') null,
    constraint UK1kr0921n5186tk94hanxak7w3
        unique (ticket_code),
    constraint FK1wt2tttbu3gr93nlimj8s6w0e
        foreign key (schedule_slot_id) references tbl_schedule_slot (id),
    constraint FKstww002h0rl7e5lgm8iv6pb26
        foreign key (appointment_id) references tbl_appointment (id)
);

-- Modified INSERT statements for tbl_time_slot with explicit IDs
INSERT INTO tbl_time_slot (id, start_time, end_time) VALUES
(1, '06:00:00', '07:00:00'), -- Buổi sáng
(2, '07:00:00', '08:00:00'),
(3, '08:00:00', '09:00:00'),
(4, '09:00:00', '10:00:00'),
(5, '10:00:00', '11:00:00'),
-- Giả sử nghỉ trưa từ 12:00 đến 13:30
(6, '13:30:00', '14:30:00'), -- Buổi chiều
(7, '14:30:00', '15:30:00'),
(8, '15:30:00', '16:30:00');


-- Modified INSERT statements for tbl_medical_specialty with explicit IDs
INSERT INTO tbl_medical_specialty (id, price, name, note) VALUES
(1, 200000.00, 'Khám tổng quát', 'Khám sức khỏe định kỳ và sàng lọc ban đầu'),
(2, 250000.00, 'Nội khoa', 'Chẩn đoán và điều trị các bệnh nội khoa tổng quát'),
(3, 300000.00, 'Ngoại tổng quát', 'Thực hiện các thủ thuật phẫu thuật thông thường'),
(4, 165000.00, 'Sản phụ khoa', 'Sức khỏe phụ nữ, mang thai và sinh nở'),
(5, 240000.00, 'Nhi khoa', 'Khám và điều trị các bệnh nhi khoa, chăm sóc trẻ em'),
(6, 210000.00, 'Răng hàm mặt', 'Khám và điều trị các bệnh về răng miệng'),
(7, 195000.00, 'Chấn thương chỉnh hình', 'Các vấn đề về hệ cơ xương khớp, xương, khớp, dây chằng'),
(8, 185000.00, 'Tâm thần', 'Chẩn đoán và điều trị rối loạn sức khỏe tâm thần'),
(9, 230000.00, 'Hô hấp', 'Khám và điều trị các bệnh về hệ hô hấp, phổi'),
(10, 250000.00, 'Tiết niệu', 'Các bệnh về đường tiết niệu và hệ sinh dục nam'),
(11, 240000.00, 'Thần kinh', 'Các bệnh về hệ thần kinh, bao gồm não và tủy sống'),
(12, 260000.00, 'Da liễu', 'Khám và điều trị các bệnh về da, tóc và móng'),
(13, 180000.00, 'Tai mũi họng', 'Khám và điều trị các bệnh về Tai, Mũi và Họng (TMH)'),
(14, 190000.00, 'Tim mạch', 'Khám và điều trị các bệnh về tim và mạch máu'),
(15, 170000.00, 'Tiêu hóa', 'Khám và điều trị các bệnh về hệ tiêu hóa'),
(16, 200000.00, 'Nội tiết', 'Khám và điều trị các bệnh nội tiết, tiểu đường, tuyến giáp'),
(17, 210000.00, 'Thận học', 'Khám và điều trị các bệnh về thận'),
(18, 250000.00, 'Ung bướu', 'Chẩn đoán và điều trị ung thư'),
(19, 160000.00, 'Nhãn khoa', 'Khám và điều trị các bệnh về mắt, phẫu thuật mắt'),
(20, 180000.00, 'Bệnh truyền nhiễm', 'Chẩn đoán và điều trị các bệnh nhiễm trùng');

-- 150 customers
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(1001, '1995-03-15', '101 Đường số 1, Quận 1, TP. HCM', 'customer1001@example.com', 'Nguyễn Văn A', 'Việt Nam', '0900001001', 'MALE'),
(1002, '1988-11-20', '102 Đường số 2, Quận 3, TP. HCM', 'customer1002@example.com', 'Trần Thị B', 'Việt Nam', '0900001002', 'FEMALE'),
(1003, '1992-07-01', '103 Đường số 3, Quận 5, TP. HCM', 'customer1003@example.com', 'Lê Văn C', 'Việt Nam', '0900001003', 'MALE'),
(1004, '2000-01-25', '104 Đường số 4, Quận 7, TP. HCM', 'customer1004@example.com', 'Phạm Thị D', 'Việt Nam', '0900001004', 'FEMALE'),
(1005, '1985-09-10', '105 Đường số 5, Quận Bình Thạnh, TP. HCM', 'customer1005@example.com', 'Hoàng Văn E', 'Việt Nam', '0900001005', 'MALE'),
(1006, '1998-05-05', '106 Đường số 6, Quận Gò Vấp, TP. HCM', 'customer1006@example.com', 'Võ Thị F', 'Việt Nam', '0900001006', 'FEMALE'),
(1007, '1990-12-30', '107 Đường số 7, Quận Tân Bình, TP. HCM', 'customer1007@example.com', 'Đặng Văn G', 'Việt Nam', '0900001007', 'MALE'),
(1008, '1993-08-18', '108 Đường số 8, Quận Phú Nhuận, TP. HCM', 'customer1008@example.com', 'Bùi Thị H', 'Việt Nam', '0900001008', 'FEMALE'),
(1009, '1982-04-02', '109 Đường số 9, Quận 10, TP. HCM', 'customer1009@example.com', 'Ngô Văn I', 'Việt Nam', '0900001009', 'MALE'),
(1010, '1997-10-12', '110 Đường số 10, Quận 11, TP. HCM', 'customer1010@example.com', 'Dương Thị K', 'Việt Nam', '0900001010', 'FEMALE'),
(1011, '1996-02-14', '111 Đường số 11, Quận 1, TP. HCM', 'customer1011@example.com', 'Nguyễn Hoàng L', 'Việt Nam', '0900001011', 'MALE'),
(1012, '1989-10-21', '112 Đường số 12, Quận 3, TP. HCM', 'customer1012@example.com', 'Trần Bảo M', 'Việt Nam', '0900001012', 'FEMALE'),
(1013, '1991-06-05', '113 Đường số 13, Quận 5, TP. HCM', 'customer1013@example.com', 'Lê Quốc N', 'Việt Nam', '0900001013', 'MALE'),
(1014, '1999-03-28', '114 Đường số 14, Quận 7, TP. HCM', 'customer1014@example.com', 'Phạm Ngọc O', 'Việt Nam', '0900001014', 'FEMALE'),
(1015, '1986-08-11', '115 Đường số 15, Quận Bình Thạnh, TP. HCM', 'customer1015@example.com', 'Hoàng Minh P', 'Việt Nam', '0900001015', 'MALE'),
(1016, '1997-04-08', '116 Đường số 16, Quận Gò Vấp, TP. HCM', 'customer1016@example.com', 'Võ Thúy Q', 'Việt Nam', '0900001016', 'FEMALE'),
(1017, '1991-11-29', '117 Đường số 17, Quận Tân Bình, TP. HCM', 'customer1017@example.com', 'Đặng Thanh R', 'Việt Nam', '0900001017', 'MALE'),
(1018, '1994-07-17', '118 Đường số 18, Quận Phú Nhuận, TP. HCM', 'customer1018@example.com', 'Bùi Diễm S', 'Việt Nam', '0900001018', 'FEMALE'),
(1019, '1983-03-01', '119 Đường số 19, Quận 10, TP. HCM', 'customer1019@example.com', 'Ngô Đức T', 'Việt Nam', '0900001019', 'MALE'),
(1020, '1998-09-15', '120 Đường số 20, Quận 11, TP. HCM', 'customer1020@example.com', 'Dương Kim U', 'Việt Nam', '0900001020', 'FEMALE'),
(1021, '1995-04-16', '121 Đường số 21, Quận 1, TP. HCM', 'customer1021@example.com', 'Nguyễn Tiến V', 'Việt Nam', '0900001021', 'MALE'),
(1022, '1987-12-22', '122 Đường số 22, Quận 3, TP. HCM', 'customer1022@example.com', 'Trần Mai X', 'Việt Nam', '0900001022', 'FEMALE'),
(1023, '1993-08-02', '123 Đường số 23, Quận 5, TP. HCM', 'customer1023@example.com', 'Lê Duy Y', 'Việt Nam', '0900001023', 'MALE'),
(1024, '2001-02-26', '124 Đường số 24, Quận 7, TP. HCM', 'customer1024@example.com', 'Phạm Hà Z', 'Việt Nam', '0900001024', 'FEMALE'),
(1025, '1984-10-09', '125 Đường số 25, Quận Bình Thạnh, TP. HCM', 'customer1025@example.com', 'Hoàng Bảo A', 'Việt Nam', '0900001025', 'MALE'),
(1026, '1999-06-06', '126 Đường số 26, Quận Gò Vấp, TP. HCM', 'customer1026@example.com', 'Võ Lan B', 'Việt Nam', '0900001026', 'FEMALE'),
(1027, '1990-01-28', '127 Đường số 27, Quận Tân Bình, TP. HCM', 'customer1027@example.com', 'Đặng Tuấn C', 'Việt Nam', '0900001027', 'MALE'),
(1028, '1992-09-19', '128 Đường số 28, Quận Phú Nhuận, TP. HCM', 'customer1028@example.com', 'Bùi Trâm D', 'Việt Nam', '0900001028', 'FEMALE'),
(1029, '1981-05-03', '129 Đường số 29, Quận 10, TP. HCM', 'customer1029@example.com', 'Ngô Phong E', 'Việt Nam', '0900001029', 'MALE'),
(1030, '1996-11-11', '130 Đường số 30, Quận 11, TP. HCM', 'customer1030@example.com', 'Dương Vy F', 'Việt Nam', '0900001030', 'FEMALE'),
(1031, '1994-03-14', '131 Đường số 31, Quận 1, TP. HCM', 'customer1031@example.com', 'Nguyễn Anh G', 'Việt Nam', '0900001031', 'MALE'),
(1032, '1988-11-19', '132 Đường số 32, Quận 3, TP. HCM', 'customer1032@example.com', 'Trần Vân H', 'Việt Nam', '0900001032', 'FEMALE'),
(1033, '1991-07-04', '133 Đường số 33, Quận 5, TP. HCM', 'customer1033@example.com', 'Lê Hữu I', 'Việt Nam', '0900001033', 'MALE'),
(1034, '2000-01-24', '134 Đường số 34, Quận 7, TP. HCM', 'customer1034@example.com', 'Phạm Thảo K', 'Việt Nam', '0900001034', 'FEMALE'),
(1035, '1985-09-09', '135 Đường số 35, Quận Bình Thạnh, TP. HCM', 'customer1035@example.com', 'Hoàng Khang L', 'Việt Nam', '0900001035', 'MALE'),
(1036, '1998-05-04', '136 Đường số 36, Quận Gò Vấp, TP. HCM', 'customer1036@example.com', 'Võ Quỳnh M', 'Việt Nam', '0900001036', 'FEMALE'),
(1037, '1990-12-29', '137 Đường số 37, Quận Tân Bình, TP. HCM', 'customer1037@example.com', 'Đặng Khoa N', 'Việt Nam', '0900001037', 'MALE'),
(1038, '1993-08-17', '138 Đường số 38, Quận Phú Nhuận, TP. HCM', 'customer1038@example.com', 'Bùi Ngọc O', 'Việt Nam', '0900001038', 'FEMALE'),
(1039, '1982-04-01', '139 Đường số 39, Quận 10, TP. HCM', 'customer1039@example.com', 'Ngô Huy P', 'Việt Nam', '0900001039', 'MALE'),
(1040, '1997-10-11', '140 Đường số 40, Quận 11, TP. HCM', 'customer1040@example.com', 'Dương Trang Q', 'Việt Nam', '0900001040', 'FEMALE'),
(1041, '1995-02-13', '141 Đường số 41, Quận 1, TP. HCM', 'customer1041@example.com', 'Nguyễn Quân R', 'Việt Nam', '0900001041', 'MALE'),
(1042, '1989-10-20', '142 Đường số 42, Quận 3, TP. HCM', 'customer1042@example.com', 'Trần Tâm S', 'Việt Nam', '0900001042', 'FEMALE'),
(1043, '1991-06-04', '143 Đường số 43, Quận 5, TP. HCM', 'customer1043@example.com', 'Lê Phát T', 'Việt Nam', '0900001043', 'MALE'),
(1044, '1999-03-27', '144 Đường số 44, Quận 7, TP. HCM', 'customer1044@example.com', 'Phạm Uyên U', 'Việt Nam', '0900001044', 'FEMALE'),
(1045, '1986-08-10', '145 Đường số 45, Quận Bình Thạnh, TP. HCM', 'customer1045@example.com', 'Hoàng Long V', 'Việt Nam', '0900001045', 'MALE'),
(1046, '1997-04-07', '146 Đường số 46, Quận Gò Vấp, TP. HCM', 'customer1046@example.com', 'Võ Xuân X', 'Việt Nam', '0900001046', 'FEMALE'),
(1047, '1991-11-28', '147 Đường số 47, Quận Tân Bình, TP. HCM', 'customer1047@example.com', 'Đặng Minh Y', 'Việt Nam', '0900001047', 'MALE'),
(1048, '1994-07-16', '148 Đường số 48, Quận Phú Nhuận, TP. HCM', 'customer1048@example.com', 'Bùi Yến Z', 'Việt Nam', '0900001048', 'FEMALE'),
(1049, '1983-02-28', '149 Đường số 49, Quận 10, TP. HCM', 'customer1049@example.com', 'Ngô Quang A', 'Việt Nam', '0900001049', 'MALE'),
(1050, '1998-09-14', '150 Đường số 50, Quận 11, TP. HCM', 'customer1050@example.com', 'Dương Thùy B', 'Việt Nam', '0900001050', 'FEMALE'),
(1051, '1996-03-15', '151 Đường số 51, Quận 1, TP. HCM', 'customer1051@example.com', 'Nguyễn Đình C', 'Việt Nam', '0900001051', 'MALE'),
(1052, '1987-11-20', '152 Đường số 52, Quận 3, TP. HCM', 'customer1052@example.com', 'Trần Hằng D', 'Việt Nam', '0900001052', 'FEMALE'),
(1053, '1993-07-01', '153 Đường số 53, Quận 5, TP. HCM', 'customer1053@example.com', 'Lê Trung E', 'Việt Nam', '0900001053', 'MALE'),
(1054, '2001-01-25', '154 Đường số 54, Quận 7, TP. HCM', 'customer1054@example.com', 'Phạm Kiều F', 'Việt Nam', '0900001054', 'FEMALE'),
(1055, '1984-09-10', '155 Đường số 55, Quận Bình Thạnh, TP. HCM', 'customer1055@example.com', 'Hoàng Sơn G', 'Việt Nam', '0900001055', 'MALE'),
(1056, '1999-05-05', '156 Đường số 56, Quận Gò Vấp, TP. HCM', 'customer1056@example.com', 'Võ Trúc H', 'Việt Nam', '0900001056', 'FEMALE'),
(1057, '1990-12-30', '157 Đường số 57, Quận Tân Bình, TP. HCM', 'customer1057@example.com', 'Đặng Nam I', 'Việt Nam', '0900001057', 'MALE'),
(1058, '1992-08-18', '158 Đường số 58, Quận Phú Nhuận, TP. HCM', 'customer1058@example.com', 'Bùi Linh K', 'Việt Nam', '0900001058', 'FEMALE'),
(1059, '1981-04-02', '159 Đường số 59, Quận 10, TP. HCM', 'customer1059@example.com', 'Ngô Hải L', 'Việt Nam', '0900001059', 'MALE'),
(1060, '1996-10-12', '160 Đường số 60, Quận 11, TP. HCM', 'customer1060@example.com', 'Dương Mỹ M', 'Việt Nam', '0900001060', 'FEMALE'),
(1061, '1995-04-14', '161 Đường số 61, Quận 1, TP. HCM', 'customer1061@example.com', 'Nguyễn Công N', 'Việt Nam', '0900001061', 'MALE'),
(1062, '1988-12-21', '162 Đường số 62, Quận 3, TP. HCM', 'customer1062@example.com', 'Trần Nhung O', 'Việt Nam', '0900001062', 'FEMALE'),
(1063, '1991-06-05', '163 Đường số 63, Quận 5, TP. HCM', 'customer1063@example.com', 'Lê Tấn P', 'Việt Nam', '0900001063', 'MALE'),
(1064, '1999-03-28', '164 Đường số 64, Quận 7, TP. HCM', 'customer1064@example.com', 'Phạm Phượng Q', 'Việt Nam', '0900001064', 'FEMALE'),
(1065, '1986-08-11', '165 Đường số 65, Quận Bình Thạnh, TP. HCM', 'customer1065@example.com', 'Hoàng Việt R', 'Việt Nam', '0900001065', 'MALE'),
(1066, '1997-04-08', '166 Đường số 66, Quận Gò Vấp, TP. HCM', 'customer1066@example.com', 'Võ Diễm S', 'Việt Nam', '0900001066', 'FEMALE'),
(1067, '1990-11-29', '167 Đường số 67, Quận Tân Bình, TP. HCM', 'customer1067@example.com', 'Đặng Hiếu T', 'Việt Nam', '0900001067', 'MALE'),
(1068, '1994-07-17', '168 Đường số 68, Quận Phú Nhuận, TP. HCM', 'customer1068@example.com', 'Bùi Tuyết U', 'Việt Nam', '0900001068', 'FEMALE'),
(1069, '1983-03-01', '169 Đường số 69, Quận 10, TP. HCM', 'customer1069@example.com', 'Ngô Khiêm V', 'Việt Nam', '0900001069', 'MALE'),
(1070, '1998-09-15', '170 Đường số 70, Quận 11, TP. HCM', 'customer1070@example.com', 'Dương Hoa X', 'Việt Nam', '0900001070', 'FEMALE'),
(1071, '1994-04-16', '171 Đường số 71, Quận 1, TP. HCM', 'customer1071@example.com', 'Nguyễn Sang Y', 'Việt Nam', '0900001071', 'MALE'),
(1072, '1987-12-22', '172 Đường số 72, Quận 3, TP. HCM', 'customer1072@example.com', 'Trần Nga Z', 'Việt Nam', '0900001072', 'FEMALE'),
(1073, '1993-08-02', '173 Đường số 73, Quận 5, TP. HCM', 'customer1073@example.com', 'Lê Bình A', 'Việt Nam', '0900001073', 'MALE'),
(1074, '2000-02-26', '174 Đường số 74, Quận 7, TP. HCM', 'customer1074@example.com', 'Phạm Huệ B', 'Việt Nam', '0900001074', 'FEMALE'),
(1075, '1985-10-09', '175 Đường số 75, Quận Bình Thạnh, TP. HCM', 'customer1075@example.com', 'Hoàng Dũng C', 'Việt Nam', '0900001075', 'MALE'),
(1076, '1999-06-06', '176 Đường số 76, Quận Gò Vấp, TP. HCM', 'customer1076@example.com', 'Võ Lệ D', 'Việt Nam', '0900001076', 'FEMALE'),
(1077, '1991-01-28', '177 Đường số 77, Quận Tân Bình, TP. HCM', 'customer1077@example.com', 'Đặng Phú E', 'Việt Nam', '0900001077', 'MALE'),
(1078, '1992-09-19', '178 Đường số 78, Quận Phú Nhuận, TP. HCM', 'customer1078@example.com', 'Bùi Thư F', 'Việt Nam', '0900001078', 'FEMALE'),
(1079, '1982-05-03', '179 Đường số 79, Quận 10, TP. HCM', 'customer1079@example.com', 'Ngô Vinh G', 'Việt Nam', '0900001079', 'MALE'),
(1080, '1997-11-11', '180 Đường số 80, Quận 11, TP. HCM', 'customer1080@example.com', 'Dương Ngân H', 'Việt Nam', '0900001080', 'FEMALE'),
(1081, '1994-03-14', '181 Đường số 81, Quận 1, TP. HCM', 'customer1081@example.com', 'Nguyễn Thái I', 'Việt Nam', '0900001081', 'MALE'),
(1082, '1988-11-19', '182 Đường số 82, Quận 3, TP. HCM', 'customer1082@example.com', 'Trần Sương K', 'Việt Nam', '0900001082', 'FEMALE'),
(1083, '1990-07-04', '183 Đường số 83, Quận 5, TP. HCM', 'customer1083@example.com', 'Lê Đông L', 'Việt Nam', '0900001083', 'MALE'),
(1084, '1999-01-24', '184 Đường số 84, Quận 7, TP. HCM', 'customer1084@example.com', 'Phạm Hồng M', 'Việt Nam', '0900001084', 'FEMALE'),
(1085, '1986-09-09', '185 Đường số 85, Quận Bình Thạnh, TP. HCM', 'customer1085@example.com', 'Hoàng Tùng N', 'Việt Nam', '0900001085', 'MALE'),
(1086, '1998-05-04', '186 Đường số 86, Quận Gò Vấp, TP. HCM', 'customer1086@example.com', 'Võ Cẩm O', 'Việt Nam', '0900001086', 'FEMALE'),
(1087, '1991-12-29', '187 Đường số 87, Quận Tân Bình, TP. HCM', 'customer1087@example.com', 'Đặng Quốc P', 'Việt Nam', '0900001087', 'MALE'),
(1088, '1993-08-17', '188 Đường số 88, Quận Phú Nhuận, TP. HCM', 'customer1088@example.com', 'Bùi Hoài Q', 'Việt Nam', '0900001088', 'FEMALE'),
(1089, '1982-04-01', '189 Đường số 89, Quận 10, TP. HCM', 'customer1089@example.com', 'Ngô Danh R', 'Việt Nam', '0900001089', 'MALE'),
(1090, '1997-10-11', '190 Đường số 90, Quận 11, TP. HCM', 'customer1090@example.com', 'Dương Thu S', 'Việt Nam', '0900001090', 'FEMALE'),
(1091, '1995-02-13', '191 Đường số 91, Quận 1, TP. HCM', 'customer1091@example.com', 'Nguyễn Mạnh T', 'Việt Nam', '0900001091', 'MALE'),
(1092, '1989-10-20', '192 Đường số 92, Quận 3, TP. HCM', 'customer1092@example.com', 'Trần Sen U', 'Việt Nam', '0900001092', 'FEMALE'),
(1093, '1991-06-04', '193 Đường số 93, Quận 5, TP. HCM', 'customer1093@example.com', 'Lê Thành V', 'Việt Nam', '0900001093', 'MALE'),
(1094, '1999-03-27', '194 Đường số 94, Quận 7, TP. HCM', 'customer1094@example.com', 'Phạm Châu X', 'Việt Nam', '0900001094', 'FEMALE'),
(1095, '1986-08-10', '195 Đường số 95, Quận Bình Thạnh, TP. HCM', 'customer1095@example.com', 'Hoàng Nguyên Y', 'Việt Nam', '0900001095', 'MALE'),
(1096, '1997-04-07', '196 Đường số 96, Quận Gò Vấp, TP. HCM', 'customer1096@example.com', 'Võ Kim Z', 'Việt Nam', '0900001096', 'FEMALE'),
(1097, '1990-11-28', '197 Đường số 97, Quận Tân Bình, TP. HCM', 'customer1097@example.com', 'Đặng Nhật A', 'Việt Nam', '0900001097', 'MALE'),
(1098, '1994-07-16', '198 Đường số 98, Quận Phú Nhuận, TP. HCM', 'customer1098@example.com', 'Bùi Thanh B', 'Việt Nam', '0900001098', 'FEMALE'),
(1099, '1983-02-28', '199 Đường số 99, Quận 10, TP. HCM', 'customer1099@example.com', 'Ngô Trí C', 'Việt Nam', '0900001099', 'MALE'),
(1100, '1998-09-14', '200 Đường số 100, Quận 11, TP. HCM', 'customer1100@example.com', 'Dương Tuyết D', 'Việt Nam', '0900001100', 'FEMALE'),
(1101, '1996-02-15', '201 Đường số 101, Quận 1, TP. HCM', 'customer1101@example.com', 'Nguyễn Gia E', 'Việt Nam', '0900001101', 'MALE'),
(1102, '1987-10-21', '202 Đường số 102, Quận 3, TP. HCM', 'customer1102@example.com', 'Trần Hà F', 'Việt Nam', '0900001102', 'FEMALE'),
(1103, '1992-06-05', '203 Đường số 103, Quận 5, TP. HCM', 'customer1103@example.com', 'Lê Hoàng G', 'Việt Nam', '0900001103', 'MALE'),
(1104, '2000-03-28', '204 Đường số 104, Quận 7, TP. HCM', 'customer1104@example.com', 'Phạm Nguyên H', 'Việt Nam', '0900001104', 'FEMALE'),
(1105, '1985-08-11', '205 Đường số 105, Quận Bình Thạnh, TP. HCM', 'customer1105@example.com', 'Hoàng Chương I', 'Việt Nam', '0900001105', 'MALE'),
(1106, '1997-04-08', '206 Đường số 106, Quận Gò Vấp, TP. HCM', 'customer1106@example.com', 'Võ Như K', 'Việt Nam', '0900001106', 'FEMALE'),
(1107, '1991-11-29', '207 Đường số 107, Quận Tân Bình, TP. HCM', 'customer1107@example.com', 'Đặng Phi L', 'Việt Nam', '0900001107', 'MALE'),
(1108, '1993-07-17', '208 Đường số 108, Quận Phú Nhuận, TP. HCM', 'customer1108@example.com', 'Bùi Hồng M', 'Việt Nam', '0900001108', 'FEMALE'),
(1109, '1982-03-01', '209 Đường số 109, Quận 10, TP. HCM', 'customer1109@example.com', 'Ngô Nhân N', 'Việt Nam', '0900001109', 'MALE'),
(1110, '1996-09-15', '210 Đường số 110, Quận 11, TP. HCM', 'customer1110@example.com', 'Dương Minh O', 'Việt Nam', '0900001110', 'FEMALE'),
(1111, '1995-04-16', '211 Đường số 111, Quận 1, TP. HCM', 'customer1111@example.com', 'Nguyễn Trọng P', 'Việt Nam', '0900001111', 'MALE'),
(1112, '1988-12-22', '212 Đường số 112, Quận 3, TP. HCM', 'customer1112@example.com', 'Trần Hiền Q', 'Việt Nam', '0900001112', 'FEMALE'),
(1113, '1990-08-02', '213 Đường số 113, Quận 5, TP. HCM', 'customer1113@example.com', 'Lê Triết R', 'Việt Nam', '0900001113', 'MALE'),
(1114, '1999-02-26', '214 Đường số 114, Quận 7, TP. HCM', 'customer1114@example.com', 'Phạm Phương S', 'Việt Nam', '0900001114', 'FEMALE'),
(1115, '1984-10-09', '215 Đường số 115, Quận Bình Thạnh, TP. HCM', 'customer1115@example.com', 'Hoàng Ân T', 'Việt Nam', '0900001115', 'MALE'),
(1116, '1998-06-06', '216 Đường số 116, Quận Gò Vấp, TP. HCM', 'customer1116@example.com', 'Võ Thảo U', 'Việt Nam', '0900001116', 'FEMALE'),
(1117, '1990-01-28', '217 Đường số 117, Quận Tân Bình, TP. HCM', 'customer1117@example.com', 'Đặng Trường V', 'Việt Nam', '0900001117', 'MALE'),
(1118, '1992-09-19', '218 Đường số 118, Quận Phú Nhuận, TP. HCM', 'customer1118@example.com', 'Bùi Loan X', 'Việt Nam', '0900001118', 'FEMALE'),
(1119, '1981-05-03', '219 Đường số 119, Quận 10, TP. HCM', 'customer1119@example.com', 'Ngô Dương Y', 'Việt Nam', '0900001119', 'MALE'),
(1120, '1997-11-11', '220 Đường số 120, Quận 11, TP. HCM', 'customer1120@example.com', 'Dương Duyên Z', 'Việt Nam', '0900001120', 'FEMALE'),
(1121, '1994-03-14', '221 Đường số 121, Quận 1, TP. HCM', 'customer1121@example.com', 'Nguyễn Kỳ A', 'Việt Nam', '0900001121', 'MALE'),
(1122, '1988-11-19', '222 Đường số 122, Quận 3, TP. HCM', 'customer1122@example.com', 'Trần An B', 'Việt Nam', '0900001122', 'FEMALE'),
(1123, '1991-07-04', '223 Đường số 123, Quận 5, TP. HCM', 'customer1123@example.com', 'Lê Toàn C', 'Việt Nam', '0900001123', 'MALE'),
(1124, '2000-01-24', '224 Đường số 124, Quận 7, TP. HCM', 'customer1124@example.com', 'Phạm Anh D', 'Việt Nam', '0900001124', 'FEMALE'),
(1125, '1985-09-09', '225 Đường số 125, Quận Bình Thạnh, TP. HCM', 'customer1125@example.com', 'Hoàng Bách E', 'Việt Nam', '0900001125', 'MALE'),
(1126, '1998-05-04', '226 Đường số 126, Quận Gò Vấp, TP. HCM', 'customer1126@example.com', 'Võ Chi F', 'Việt Nam', '0900001126', 'FEMALE'),
(1127, '1990-12-29', '227 Đường số 127, Quận Tân Bình, TP. HCM', 'customer1127@example.com', 'Đặng Tâm G', 'Việt Nam', '0900001127', 'MALE'),
(1128, '1993-08-17', '228 Đường số 128, Quận Phú Nhuận, TP. HCM', 'customer1128@example.com', 'Bùi Ánh H', 'Việt Nam', '0900001128', 'FEMALE'),
(1129, '1982-04-01', '229 Đường số 129, Quận 10, TP. HCM', 'customer1129@example.com', 'Ngô Vũ I', 'Việt Nam', '0900001129', 'MALE'),
(1130, '1997-10-11', '230 Đường số 130, Quận 11, TP. HCM', 'customer1130@example.com', 'Dương Châu K', 'Việt Nam', '0900001130', 'FEMALE'),
(1131, '1995-02-13', '231 Đường số 131, Quận 1, TP. HCM', 'customer1131@example.com', 'Nguyễn Kiên L', 'Việt Nam', '0900001131', 'MALE'),
(1132, '1989-10-20', '232 Đường số 132, Quận 3, TP. HCM', 'customer1132@example.com', 'Trần Cúc M', 'Việt Nam', '0900001132', 'FEMALE'),
(1133, '1991-06-04', '233 Đường số 133, Quận 5, TP. HCM', 'customer1133@example.com', 'Lê Khôi N', 'Việt Nam', '0900001133', 'MALE'),
(1134, '1999-03-27', '234 Đường số 134, Quận 7, TP. HCM', 'customer1134@example.com', 'Phạm Dung O', 'Việt Nam', '0900001134', 'FEMALE'),
(1135, '1986-08-10', '235 Đường số 135, Quận Bình Thạnh, TP. HCM', 'customer1135@example.com', 'Hoàng Lâm P', 'Việt Nam', '0900001135', 'MALE'),
(1136, '1997-04-07', '236 Đường số 136, Quận Gò Vấp, TP. HCM', 'customer1136@example.com', 'Võ Giao Q', 'Việt Nam', '0900001136', 'FEMALE'),
(1137, '1990-11-28', '237 Đường số 137, Quận Tân Bình, TP. HCM', 'customer1137@example.com', 'Đặng Thắng R', 'Việt Nam', '0900001137', 'MALE'),
(1138, '1994-07-16', '238 Đường số 138, Quận Phú Nhuận, TP. HCM', 'customer1138@example.com', 'Bùi Giang S', 'Việt Nam', '0900001138', 'FEMALE'),
(1139, '1983-02-28', '239 Đường số 139, Quận 10, TP. HCM', 'customer1139@example.com', 'Ngô Tú T', 'Việt Nam', '0900001139', 'MALE'),
(1140, '1998-09-14', '240 Đường số 140, Quận 11, TP. HCM', 'customer1140@example.com', 'Dương Hân U', 'Việt Nam', '0900001140', 'FEMALE'),
(1141, '1996-03-15', '241 Đường số 141, Quận 1, TP. HCM', 'customer1141@example.com', 'Nguyễn Đạt V', 'Việt Nam', '0900001141', 'MALE'),
(1142, '1987-11-20', '242 Đường số 142, Quận 3, TP. HCM', 'customer1142@example.com', 'Trần Hương X', 'Việt Nam', '0900001142', 'FEMALE'),
(1143, '1993-07-01', '243 Đường số 143, Quận 5, TP. HCM', 'customer1143@example.com', 'Lê Huy Y', 'Việt Nam', '0900001143', 'MALE'),
(1144, '2001-01-25', '244 Đường số 144, Quận 7, TP. HCM', 'customer1144@example.com', 'Phạm Khanh Z', 'Việt Nam', '0900001144', 'FEMALE'),
(1145, '1984-09-10', '245 Đường số 145, Quận Bình Thạnh, TP. HCM', 'customer1145@example.com', 'Hoàng Thiên A', 'Việt Nam', '0900001145', 'MALE'),
(1146, '1999-05-05', '246 Đường số 146, Quận Gò Vấp, TP. HCM', 'customer1146@example.com', 'Võ Khuê B', 'Việt Nam', '0900001146', 'FEMALE'),
(1147, '1990-12-30', '247 Đường số 147, Quận Tân Bình, TP. HCM', 'customer1147@example.com', 'Đặng Luân C', 'Việt Nam', '0900001147', 'MALE'),
(1148, '1992-08-18', '248 Đường số 148, Quận Phú Nhuận, TP. HCM', 'customer1148@example.com', 'Bùi Kim D', 'Việt Nam', '0900001148', 'FEMALE'),
(1149, '1981-04-02', '249 Đường số 149, Quận 10, TP. HCM', 'customer1149@example.com', 'Ngô Thiện E', 'Việt Nam', '0900001149', 'MALE'),
(1150, '1996-10-12', '250 Đường số 150, Quận 11, TP. HCM', 'customer1150@example.com', 'Dương Kiều F', 'Việt Nam', '0900001150', 'FEMALE');

INSERT INTO tbl_customer (id, membership) VALUES
(1001, 'NORMAL'), (1002, 'SILVER'), (1003, 'NORMAL'), (1004, 'BRONZE'), (1005, 'GOLD'),
(1006, 'NORMAL'), (1007, 'NORMAL'), (1008, 'SILVER'), (1009, 'NORMAL'), (1010, 'BRONZE'),
(1011, 'NORMAL'), (1012, 'NORMAL'), (1013, 'SILVER'), (1014, 'NORMAL'), (1015, 'BRONZE'),
(1016, 'NORMAL'), (1017, 'GOLD'), (1018, 'NORMAL'), (1019, 'SILVER'), (1020, 'NORMAL'),
(1021, 'BRONZE'), (1022, 'NORMAL'), (1023, 'NORMAL'), (1024, 'SILVER'), (1025, 'NORMAL'),
(1026, 'GOLD'), (1027, 'NORMAL'), (1028, 'BRONZE'), (1029, 'NORMAL'), (1030, 'SILVER'),
(1031, 'NORMAL'), (1032, 'NORMAL'), (1033, 'BRONZE'), (1034, 'NORMAL'), (1035, 'SILVER'),
(1036, 'GOLD'), (1037, 'NORMAL'), (1038, 'NORMAL'), (1039, 'BRONZE'), (1040, 'NORMAL'),
(1041, 'SILVER'), (1042, 'NORMAL'), (1043, 'GOLD'), (1044, 'NORMAL'), (1045, 'BRONZE'),
(1046, 'NORMAL'), (1047, 'SILVER'), (1048, 'NORMAL'), (1049, 'NORMAL'), (1050, 'GOLD'),
(1051, 'BRONZE'), (1052, 'NORMAL'), (1053, 'SILVER'), (1054, 'NORMAL'), (1055, 'NORMAL'),
(1056, 'BRONZE'), (1057, 'NORMAL'), (1058, 'GOLD'), (1059, 'NORMAL'), (1060, 'SILVER'),
(1061, 'NORMAL'), (1062, 'BRONZE'), (1063, 'NORMAL'), (1064, 'SILVER'), (1065, 'NORMAL'),
(1066, 'GOLD'), (1067, 'NORMAL'), (1068, 'NORMAL'), (1069, 'BRONZE'), (1070, 'SILVER'),
(1071, 'NORMAL'), (1072, 'GOLD'), (1073, 'NORMAL'), (1074, 'BRONZE'), (1075, 'NORMAL'),
(1076, 'SILVER'), (1077, 'NORMAL'), (1078, 'NORMAL'), (1079, 'GOLD'), (1080, 'BRONZE'),
(1081, 'NORMAL'), (1082, 'SILVER'), (1083, 'NORMAL'), (1084, 'NORMAL'), (1085, 'BRONZE'),
(1086, 'GOLD'), (1087, 'NORMAL'), (1088, 'SILVER'), (1089, 'NORMAL'), (1090, 'BRONZE'),
(1091, 'NORMAL'), (1092, 'GOLD'), (1093, 'NORMAL'), (1094, 'SILVER'), (1095, 'NORMAL'),
(1096, 'BRONZE'), (1097, 'NORMAL'), (1098, 'GOLD'), (1099, 'NORMAL'), (1100, 'SILVER'),
(1101, 'BRONZE'), (1102, 'NORMAL'), (1103, 'NORMAL'), (1104, 'SILVER'), (1105, 'GOLD'),
(1106, 'NORMAL'), (1107, 'BRONZE'), (1108, 'NORMAL'), (1109, 'SILVER'), (1110, 'NORMAL'),
(1111, 'NORMAL'), (1112, 'GOLD'), (1113, 'NORMAL'), (1114, 'BRONZE'), (1115, 'SILVER'),
(1116, 'NORMAL'), (1117, 'NORMAL'), (1118, 'GOLD'), (1119, 'NORMAL'), (1120, 'BRONZE'),
(1121, 'SILVER'), (1122, 'NORMAL'), (1123, 'GOLD'), (1124, 'NORMAL'), (1125, 'NORMAL'),
(1126, 'BRONZE'), (1127, 'SILVER'), (1128, 'NORMAL'), (1129, 'GOLD'), (1130, 'NORMAL'),
(1131, 'BRONZE'), (1132, 'NORMAL'), (1133, 'SILVER'), (1134, 'NORMAL'), (1135, 'GOLD'),
(1136, 'NORMAL'), (1137, 'BRONZE'), (1138, 'NORMAL'), (1139, 'SILVER'), (1140, 'NORMAL'),
(1141, 'GOLD'), (1142, 'NORMAL'), (1143, 'BRONZE'), (1144, 'NORMAL'), (1145, 'SILVER'),
(1146, 'NORMAL'), (1147, 'GOLD'), (1148, 'NORMAL'), (1149, 'BRONZE'), (1150, 'NORMAL');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(1001, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1002, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1003, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1004, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1005, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1006, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1007, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1008, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1009, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1010, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1011, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1012, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1013, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1014, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1015, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1016, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1017, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1018, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1019, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1020, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1021, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1022, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1023, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1024, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1025, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1026, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1027, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1028, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1029, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1030, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1031, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1032, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1033, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1034, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1035, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1036, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1037, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1038, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1039, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1040, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1041, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1042, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1043, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1044, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1045, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1046, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1047, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1048, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1049, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1050, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1051, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1052, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1053, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1054, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1055, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1056, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1057, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1058, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1059, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1060, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1061, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1062, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1063, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1064, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1065, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1066, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1067, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1068, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1069, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1070, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1071, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1072, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1073, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1074, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1075, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1076, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1077, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1078, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1079, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1080, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1081, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1082, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1083, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1084, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1085, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1086, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1087, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1088, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1089, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1090, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1091, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1092, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1093, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1094, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1095, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1096, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1097, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1098, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1099, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1100, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1101, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1102, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1103, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1104, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1105, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1106, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1107, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1108, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1109, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1110, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1111, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1112, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1113, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1114, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1115, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1116, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1117, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1118, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1119, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1120, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1121, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1122, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1123, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1124, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1125, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1126, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1127, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1128, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1129, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1130, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1131, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1132, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1133, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1134, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1135, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1136, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1137, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1138, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1139, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1140, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1141, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1142, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1143, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1144, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1145, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'),
(1146, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1147, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1148, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1149, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE'), (1150, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'CUSTOMER', 'ACTIVE');

-- === Specialty: Khám tổng quát (ID: 1) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2001, '1980-05-10', '1 P. Khoa Tổng Quát, Q.1', 'doctor2001@example.com', 'Bác sĩ Tổng Quát 1', 'Việt Nam', '0911002001', 'MALE'),
(2002, '1975-11-22', '2 P. Khoa Tổng Quát, Q.1', 'doctor2002@example.com', 'Bác sĩ Tổng Quát 2', 'Việt Nam', '0911002002', 'FEMALE'),
(2003, '1982-03-15', '3 P. Khoa Tổng Quát, Q.1', 'doctor2003@example.com', 'Bác sĩ Tổng Quát 3', 'Việt Nam', '0911002003', 'MALE'),
(2004, '1978-09-01', '4 P. Khoa Tổng Quát, Q.1', 'doctor2004@example.com', 'Bác sĩ Tổng Quát 4', 'Việt Nam', '0911002004', 'FEMALE'),
(2005, '1985-01-20', '5 P. Khoa Tổng Quát, Q.1', 'doctor2005@example.com', 'Bác sĩ Tổng Quát 5', 'Việt Nam', '0911002005', 'MALE'),
(2006, '1972-07-07', '6 P. Khoa Tổng Quát, Q.1', 'doctor2006@example.com', 'Bác sĩ Tổng Quát 6', 'Việt Nam', '0911002006', 'FEMALE'),
(2007, '1988-12-11', '7 P. Khoa Tổng Quát, Q.1', 'doctor2007@example.com', 'Bác sĩ Tổng Quát 7', 'Việt Nam', '0911002007', 'MALE'),
(2008, '1970-06-18', '8 P. Khoa Tổng Quát, Q.1', 'doctor2008@example.com', 'Bác sĩ Tổng Quát 8', 'Việt Nam', '0911002008', 'FEMALE'),
(2009, '1981-04-25', '9 P. Khoa Tổng Quát, Q.1', 'doctor2009@example.com', 'Bác sĩ Tổng Quát 9', 'Việt Nam', '0911002009', 'MALE'),
(2010, '1976-10-30', '10 P. Khoa Tổng Quát, Q.1', 'doctor2010@example.com', 'Bác sĩ Tổng Quát 10', 'Việt Nam', '0911002010', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2001, 1, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2002, 1, 'Trưởng khoa', 'Thạc sĩ Y học'),
(2003, 1, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2004, 1, 'Bác sĩ Điều trị', 'Thạc sĩ Y học'),
(2005, 1, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2006, 1, 'Bác sĩ Cao cấp', 'Tiến sĩ Y học'),
(2007, 1, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2008, 1, 'Bác sĩ Điều trị', 'Thạc sĩ Y học'),
(2009, 1, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2010, 1, 'Bác sĩ Điều trị', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2001, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2002, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2003, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2004, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2005, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2006, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2007, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2008, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2009, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2010, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Nội khoa (ID: 2) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2011, '1979-06-12', '1 P. Khoa Nội, Q.3', 'doctor2011@example.com', 'Bác sĩ Nội khoa 1', 'Việt Nam', '0911002011', 'MALE'),
(2012, '1983-01-05', '2 P. Khoa Nội, Q.3', 'doctor2012@example.com', 'Bác sĩ Nội khoa 2', 'Việt Nam', '0911002012', 'FEMALE'),
(2013, '1977-08-19', '3 P. Khoa Nội, Q.3', 'doctor2013@example.com', 'Bác sĩ Nội khoa 3', 'Việt Nam', '0911002013', 'MALE'),
(2014, '1986-04-14', '4 P. Khoa Nội, Q.3', 'doctor2014@example.com', 'Bác sĩ Nội khoa 4', 'Việt Nam', '0911002014', 'FEMALE'),
(2015, '1974-10-26', '5 P. Khoa Nội, Q.3', 'doctor2015@example.com', 'Bác sĩ Nội khoa 5', 'Việt Nam', '0911002015', 'MALE'),
(2016, '1989-02-08', '6 P. Khoa Nội, Q.3', 'doctor2016@example.com', 'Bác sĩ Nội khoa 6', 'Việt Nam', '0911002016', 'FEMALE'),
(2017, '1971-07-31', '7 P. Khoa Nội, Q.3', 'doctor2017@example.com', 'Bác sĩ Nội khoa 7', 'Việt Nam', '0911002017', 'MALE'),
(2018, '1984-05-17', '8 P. Khoa Nội, Q.3', 'doctor2018@example.com', 'Bác sĩ Nội khoa 8', 'Việt Nam', '0911002018', 'FEMALE'),
(2019, '1973-11-03', '9 P. Khoa Nội, Q.3', 'doctor2019@example.com', 'Bác sĩ Nội khoa 9', 'Việt Nam', '0911002019', 'MALE'),
(2020, '1987-09-28', '10 P. Khoa Nội, Q.3', 'doctor2020@example.com', 'Bác sĩ Nội khoa 10', 'Việt Nam', '0911002020', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2011, 2, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2012, 2, 'Bác sĩ Điều trị', 'Thạc sĩ Y học'),
(2013, 2, 'Trưởng khoa', 'Tiến sĩ Y học'), (2014, 2, 'Bác sĩ Điều trị', 'Chuyên khoa I'),
(2015, 2, 'Bác sĩ Điều trị', 'Thạc sĩ Y học'), (2016, 2, 'Bác sĩ Điều trị', 'Chuyên khoa I'),
(2017, 2, 'Bác sĩ Cao cấp', 'Phó Giáo sư'), (2018, 2, 'Bác sĩ Điều trị', 'Thạc sĩ Y học'),
(2019, 2, 'Bác sĩ Điều trị', 'Chuyên khoa I'), (2020, 2, 'Bác sĩ Điều trị', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2011, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2012, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2013, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2014, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2015, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2016, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2017, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2018, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2019, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2020, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Ngoại tổng quát (ID: 3) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2021, '1978-07-11', '1 P. Khoa Ngoại, Q.5', 'doctor2021@example.com', 'Bác sĩ Ngoại khoa 1', 'Việt Nam', '0911002021', 'MALE'),
(2022, '1984-02-04', '2 P. Khoa Ngoại, Q.5', 'doctor2022@example.com', 'Bác sĩ Ngoại khoa 2', 'Việt Nam', '0911002022', 'FEMALE'),
(2023, '1976-09-18', '3 P. Khoa Ngoại, Q.5', 'doctor2023@example.com', 'Bác sĩ Ngoại khoa 3', 'Việt Nam', '0911002023', 'MALE'),
(2024, '1987-05-13', '4 P. Khoa Ngoại, Q.5', 'doctor2024@example.com', 'Bác sĩ Ngoại khoa 4', 'Việt Nam', '0911002024', 'FEMALE'),
(2025, '1973-11-25', '5 P. Khoa Ngoại, Q.5', 'doctor2025@example.com', 'Bác sĩ Ngoại khoa 5', 'Việt Nam', '0911002025', 'MALE'),
(2026, '1990-03-07', '6 P. Khoa Ngoại, Q.5', 'doctor2026@example.com', 'Bác sĩ Ngoại khoa 6', 'Việt Nam', '0911002026', 'FEMALE'),
(2027, '1970-08-30', '7 P. Khoa Ngoại, Q.5', 'doctor2027@example.com', 'Bác sĩ Ngoại khoa 7', 'Việt Nam', '0911002027', 'MALE'),
(2028, '1985-06-16', '8 P. Khoa Ngoại, Q.5', 'doctor2028@example.com', 'Bác sĩ Ngoại khoa 8', 'Việt Nam', '0911002028', 'FEMALE'),
(2029, '1972-12-02', '9 P. Khoa Ngoại, Q.5', 'doctor2029@example.com', 'Bác sĩ Ngoại khoa 9', 'Việt Nam', '0911002029', 'MALE'),
(2030, '1988-10-27', '10 P. Khoa Ngoại, Q.5', 'doctor2030@example.com', 'Bác sĩ Ngoại khoa 10', 'Việt Nam', '0911002030', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2021, 3, 'Bác sĩ Phẫu thuật', 'Chuyên khoa I'), (2022, 3, 'Bác sĩ Phẫu thuật', 'Thạc sĩ Y học'),
(2023, 3, 'Trưởng khoa Ngoại', 'Tiến sĩ Y học'), (2024, 3, 'Bác sĩ Phẫu thuật', 'Chuyên khoa II'),
(2025, 3, 'Bác sĩ Phẫu thuật', 'Thạc sĩ Y học'), (2026, 3, 'Bác sĩ Phẫu thuật', 'Chuyên khoa I'),
(2027, 3, 'Bác sĩ Phẫu thuật Cao cấp', 'Phó Giáo sư'), (2028, 3, 'Bác sĩ Phẫu thuật', 'Thạc sĩ Y học'),
(2029, 3, 'Bác sĩ Phẫu thuật', 'Chuyên khoa II'), (2030, 3, 'Bác sĩ Phẫu thuật', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2021, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2022, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2023, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2024, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2025, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2026, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2027, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2028, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2029, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2030, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Sản phụ khoa (ID: 4) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2031, '1981-08-10', '1 P. Khoa Sản, Q.7', 'doctor2031@example.com', 'Bác sĩ Sản khoa 1', 'Việt Nam', '0911002031', 'FEMALE'),
(2032, '1977-03-03', '2 P. Khoa Sản, Q.7', 'doctor2032@example.com', 'Bác sĩ Sản khoa 2', 'Việt Nam', '0911002032', 'FEMALE'),
(2033, '1983-10-17', '3 P. Khoa Sản, Q.7', 'doctor2033@example.com', 'Bác sĩ Sản khoa 3', 'Việt Nam', '0911002033', 'MALE'), -- Male Obstetrician
(2034, '1979-06-12', '4 P. Khoa Sản, Q.7', 'doctor2034@example.com', 'Bác sĩ Sản khoa 4', 'Việt Nam', '0911002034', 'FEMALE'),
(2035, '1986-12-24', '5 P. Khoa Sản, Q.7', 'doctor2035@example.com', 'Bác sĩ Sản khoa 5', 'Việt Nam', '0911002035', 'FEMALE'),
(2036, '1975-04-06', '6 P. Khoa Sản, Q.7', 'doctor2036@example.com', 'Bác sĩ Sản khoa 6', 'Việt Nam', '0911002036', 'FEMALE'),
(2037, '1988-09-29', '7 P. Khoa Sản, Q.7', 'doctor2037@example.com', 'Bác sĩ Sản khoa 7', 'Việt Nam', '0911002037', 'FEMALE'),
(2038, '1972-07-15', '8 P. Khoa Sản, Q.7', 'doctor2038@example.com', 'Bác sĩ Sản khoa 8', 'Việt Nam', '0911002038', 'FEMALE'),
(2039, '1980-01-01', '9 P. Khoa Sản, Q.7', 'doctor2039@example.com', 'Bác sĩ Sản khoa 9', 'Việt Nam', '0911002039', 'MALE'), -- Male Obstetrician
(2040, '1985-11-26', '10 P. Khoa Sản, Q.7', 'doctor2040@example.com', 'Bác sĩ Sản khoa 10', 'Việt Nam', '0911002040', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2031, 4, 'Bác sĩ Sản Phụ khoa', 'Chuyên khoa I'), (2032, 4, 'Bác sĩ Sản Phụ khoa', 'Thạc sĩ Y học'),
(2033, 4, 'Trưởng khoa Sản', 'Tiến sĩ Y học'), (2034, 4, 'Bác sĩ Sản Phụ khoa', 'Chuyên khoa II'),
(2035, 4, 'Bác sĩ Sản Phụ khoa', 'Thạc sĩ Y học'), (2036, 4, 'Bác sĩ Sản Phụ khoa', 'Chuyên khoa I'),
(2037, 4, 'Bác sĩ Sản Phụ khoa', 'Phó Giáo sư'), (2038, 4, 'Bác sĩ Sản Phụ khoa', 'Thạc sĩ Y học'),
(2039, 4, 'Bác sĩ Sản Phụ khoa', 'Chuyên khoa II'), (2040, 4, 'Bác sĩ Sản Phụ khoa', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2031, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2032, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2033, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2034, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2035, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2036, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2037, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2038, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2039, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2040, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Nhi khoa (ID: 5) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2041, '1982-09-09', '1 P. Khoa Nhi, Q.BT', 'doctor2041@example.com', 'Bác sĩ Nhi khoa 1', 'Việt Nam', '0911002041', 'FEMALE'),
(2042, '1978-04-02', '2 P. Khoa Nhi, Q.BT', 'doctor2042@example.com', 'Bác sĩ Nhi khoa 2', 'Việt Nam', '0911002042', 'MALE'),
(2043, '1984-11-16', '3 P. Khoa Nhi, Q.BT', 'doctor2043@example.com', 'Bác sĩ Nhi khoa 3', 'Việt Nam', '0911002043', 'FEMALE'),
(2044, '1980-07-11', '4 P. Khoa Nhi, Q.BT', 'doctor2044@example.com', 'Bác sĩ Nhi khoa 4', 'Việt Nam', '0911002044', 'MALE'),
(2045, '1987-01-23', '5 P. Khoa Nhi, Q.BT', 'doctor2045@example.com', 'Bác sĩ Nhi khoa 5', 'Việt Nam', '0911002045', 'FEMALE'),
(2046, '1976-05-05', '6 P. Khoa Nhi, Q.BT', 'doctor2046@example.com', 'Bác sĩ Nhi khoa 6', 'Việt Nam', '0911002046', 'MALE'),
(2047, '1989-10-28', '7 P. Khoa Nhi, Q.BT', 'doctor2047@example.com', 'Bác sĩ Nhi khoa 7', 'Việt Nam', '0911002047', 'FEMALE'),
(2048, '1973-08-14', '8 P. Khoa Nhi, Q.BT', 'doctor2048@example.com', 'Bác sĩ Nhi khoa 8', 'Việt Nam', '0911002048', 'MALE'),
(2049, '1981-02-02', '9 P. Khoa Nhi, Q.BT', 'doctor2049@example.com', 'Bác sĩ Nhi khoa 9', 'Việt Nam', '0911002049', 'FEMALE'),
(2050, '1986-12-25', '10 P. Khoa Nhi, Q.BT', 'doctor2050@example.com', 'Bác sĩ Nhi khoa 10', 'Việt Nam', '0911002050', 'MALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2041, 5, 'Bác sĩ Nhi khoa', 'Chuyên khoa I'), (2042, 5, 'Bác sĩ Nhi khoa', 'Thạc sĩ Y học'),
(2043, 5, 'Trưởng khoa Nhi', 'Tiến sĩ Y học'), (2044, 5, 'Bác sĩ Nhi khoa', 'Chuyên khoa II'),
(2045, 5, 'Bác sĩ Nhi khoa', 'Thạc sĩ Y học'), (2046, 5, 'Bác sĩ Nhi khoa', 'Chuyên khoa I'),
(2047, 5, 'Bác sĩ Nhi khoa', 'Phó Giáo sư'), (2048, 5, 'Bác sĩ Nhi khoa', 'Thạc sĩ Y học'),
(2049, 5, 'Bác sĩ Nhi khoa', 'Chuyên khoa II'), (2050, 5, 'Bác sĩ Nhi khoa', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2041, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2042, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2043, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2044, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2045, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2046, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2047, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2048, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2049, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2050, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Răng hàm mặt (ID: 6) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2051, '1983-10-08', '1 P. Khoa RHM, Q.GV', 'doctor2051@example.com', 'Nha sĩ 1', 'Việt Nam', '0911002051', 'MALE'),
(2052, '1979-05-01', '2 P. Khoa RHM, Q.GV', 'doctor2052@example.com', 'Nha sĩ 2', 'Việt Nam', '0911002052', 'FEMALE'),
(2053, '1985-12-15', '3 P. Khoa RHM, Q.GV', 'doctor2053@example.com', 'Nha sĩ 3', 'Việt Nam', '0911002053', 'MALE'),
(2054, '1981-08-10', '4 P. Khoa RHM, Q.GV', 'doctor2054@example.com', 'Nha sĩ 4', 'Việt Nam', '0911002054', 'FEMALE'),
(2055, '1988-02-22', '5 P. Khoa RHM, Q.GV', 'doctor2055@example.com', 'Nha sĩ 5', 'Việt Nam', '0911002055', 'MALE'),
(2056, '1977-06-04', '6 P. Khoa RHM, Q.GV', 'doctor2056@example.com', 'Nha sĩ 6', 'Việt Nam', '0911002056', 'FEMALE'),
(2057, '1990-11-27', '7 P. Khoa RHM, Q.GV', 'doctor2057@example.com', 'Nha sĩ 7', 'Việt Nam', '0911002057', 'MALE'),
(2058, '1974-09-13', '8 P. Khoa RHM, Q.GV', 'doctor2058@example.com', 'Nha sĩ 8', 'Việt Nam', '0911002058', 'FEMALE'),
(2059, '1982-03-03', '9 P. Khoa RHM, Q.GV', 'doctor2059@example.com', 'Nha sĩ 9', 'Việt Nam', '0911002059', 'MALE'),
(2060, '1987-01-24', '10 P. Khoa RHM, Q.GV', 'doctor2060@example.com', 'Nha sĩ 10', 'Việt Nam', '0911002060', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2051, 6, 'Nha sĩ', 'Bằng Răng Hàm Mặt'), (2052, 6, 'Nha sĩ', 'Thạc sĩ RHM'),
(2053, 6, 'Trưởng khoa RHM', 'Tiến sĩ RHM'), (2054, 6, 'Nha sĩ', 'Chuyên khoa I RHM'),
(2055, 6, 'Nha sĩ', 'Thạc sĩ RHM'), (2056, 6, 'Nha sĩ', 'Bằng Răng Hàm Mặt'),
(2057, 6, 'Nha sĩ', 'Chuyên khoa I RHM'), (2058, 6, 'Nha sĩ', 'Thạc sĩ RHM'),
(2059, 6, 'Nha sĩ', 'Chuyên khoa II RHM'), (2060, 6, 'Nha sĩ', 'Thạc sĩ RHM');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2051, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2052, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2053, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2054, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2055, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2056, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2057, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2058, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2059, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2060, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- ... (Tiếp tục mô hình tương tự cho các chuyên khoa còn lại) ...

-- === Specialty: Chấn thương chỉnh hình (ID: 7) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2061, '1977-11-07', '1 P. Khoa CTCH, Q.TB', 'doctor2061@example.com', 'Bác sĩ CTCH 1', 'Việt Nam', '0911002061', 'MALE'),
(2062, '1983-06-01', '2 P. Khoa CTCH, Q.TB', 'doctor2062@example.com', 'Bác sĩ CTCH 2', 'Việt Nam', '0911002062', 'FEMALE'),
(2063, '1975-01-14', '3 P. Khoa CTCH, Q.TB', 'doctor2063@example.com', 'Bác sĩ CTCH 3', 'Việt Nam', '0911002063', 'MALE'),
(2064, '1986-08-09', '4 P. Khoa CTCH, Q.TB', 'doctor2064@example.com', 'Bác sĩ CTCH 4', 'Việt Nam', '0911002064', 'FEMALE'),
(2065, '1972-03-21', '5 P. Khoa CTCH, Q.TB', 'doctor2065@example.com', 'Bác sĩ CTCH 5', 'Việt Nam', '0911002065', 'MALE'),
(2066, '1989-07-03', '6 P. Khoa CTCH, Q.TB', 'doctor2066@example.com', 'Bác sĩ CTCH 6', 'Việt Nam', '0911002066', 'FEMALE'),
(2067, '1970-12-26', '7 P. Khoa CTCH, Q.TB', 'doctor2067@example.com', 'Bác sĩ CTCH 7', 'Việt Nam', '0911002067', 'MALE'),
(2068, '1984-10-12', '8 P. Khoa CTCH, Q.TB', 'doctor2068@example.com', 'Bác sĩ CTCH 8', 'Việt Nam', '0911002068', 'FEMALE'),
(2069, '1978-04-04', '9 P. Khoa CTCH, Q.TB', 'doctor2069@example.com', 'Bác sĩ CTCH 9', 'Việt Nam', '0911002069', 'MALE'),
(2070, '1981-02-23', '10 P. Khoa CTCH, Q.TB', 'doctor2070@example.com', 'Bác sĩ CTCH 10', 'Việt Nam', '0911002070', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2061, 7, 'Bác sĩ CTCH', 'Chuyên khoa I'), (2062, 7, 'Bác sĩ CTCH', 'Thạc sĩ Y học'),
(2063, 7, 'Trưởng khoa CTCH', 'Tiến sĩ Y học'), (2064, 7, 'Bác sĩ CTCH', 'Chuyên khoa II'),
(2065, 7, 'Bác sĩ CTCH', 'Thạc sĩ Y học'), (2066, 7, 'Bác sĩ CTCH', 'Chuyên khoa I'),
(2067, 7, 'Bác sĩ CTCH Cao cấp', 'Phó Giáo sư'), (2068, 7, 'Bác sĩ CTCH', 'Thạc sĩ Y học'),
(2069, 7, 'Bác sĩ CTCH', 'Chuyên khoa II'), (2070, 7, 'Bác sĩ CTCH', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2061, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2062, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2063, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2064, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2065, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2066, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2067, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2068, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2069, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2070, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Tâm thần (ID: 8) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2071, '1976-12-06', '1 P. Khoa Tâm Thần, Q.PN', 'doctor2071@example.com', 'Bác sĩ Tâm Thần 1', 'Việt Nam', '0911002071', 'MALE'),
(2072, '1982-07-02', '2 P. Khoa Tâm Thần, Q.PN', 'doctor2072@example.com', 'Bác sĩ Tâm Thần 2', 'Việt Nam', '0911002072', 'FEMALE'),
(2073, '1974-02-13', '3 P. Khoa Tâm Thần, Q.PN', 'doctor2073@example.com', 'Bác sĩ Tâm Thần 3', 'Việt Nam', '0911002073', 'MALE'),
(2074, '1987-09-08', '4 P. Khoa Tâm Thần, Q.PN', 'doctor2074@example.com', 'Bác sĩ Tâm Thần 4', 'Việt Nam', '0911002074', 'FEMALE'),
(2075, '1971-04-20', '5 P. Khoa Tâm Thần, Q.PN', 'doctor2075@example.com', 'Bác sĩ Tâm Thần 5', 'Việt Nam', '0911002075', 'MALE'),
(2076, '1988-08-04', '6 P. Khoa Tâm Thần, Q.PN', 'doctor2076@example.com', 'Bác sĩ Tâm Thần 6', 'Việt Nam', '0911002076', 'FEMALE'),
(2077, '1969-01-25', '7 P. Khoa Tâm Thần, Q.PN', 'doctor2077@example.com', 'Bác sĩ Tâm Thần 7', 'Việt Nam', '0911002077', 'MALE'),
(2078, '1983-11-11', '8 P. Khoa Tâm Thần, Q.PN', 'doctor2078@example.com', 'Bác sĩ Tâm Thần 8', 'Việt Nam', '0911002078', 'FEMALE'),
(2079, '1977-05-05', '9 P. Khoa Tâm Thần, Q.PN', 'doctor2079@example.com', 'Bác sĩ Tâm Thần 9', 'Việt Nam', '0911002079', 'MALE'),
(2080, '1980-03-22', '10 P. Khoa Tâm Thần, Q.PN', 'doctor2080@example.com', 'Bác sĩ Tâm Thần 10', 'Việt Nam', '0911002080', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2071, 8, 'Bác sĩ Tâm Thần', 'Chuyên khoa I'), (2072, 8, 'Bác sĩ Tâm Thần', 'Thạc sĩ Tâm lý'),
(2073, 8, 'Trưởng khoa Tâm Thần', 'Tiến sĩ Y học'), (2074, 8, 'Bác sĩ Tâm Thần', 'Chuyên khoa II'),
(2075, 8, 'Bác sĩ Tâm Thần', 'Thạc sĩ Y học'), (2076, 8, 'Bác sĩ Tâm Thần', 'Chuyên khoa I'),
(2077, 8, 'Chuyên gia Tâm Thần', 'Phó Giáo sư'), (2078, 8, 'Bác sĩ Tâm Thần', 'Thạc sĩ Tâm lý'),
(2079, 8, 'Bác sĩ Tâm Thần', 'Chuyên khoa II'), (2080, 8, 'Bác sĩ Tâm Thần', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2071, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2072, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2073, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2074, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2075, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2076, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2077, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2078, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2079, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2080, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Hô hấp (ID: 9) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2081, '1975-01-05', '1 P. Khoa Hô Hấp, Q.10', 'doctor2081@example.com', 'Bác sĩ Hô Hấp 1', 'Việt Nam', '0911002081', 'MALE'),
(2082, '1981-08-01', '2 P. Khoa Hô Hấp, Q.10', 'doctor2082@example.com', 'Bác sĩ Hô Hấp 2', 'Việt Nam', '0911002082', 'FEMALE'),
(2083, '1973-03-12', '3 P. Khoa Hô Hấp, Q.10', 'doctor2083@example.com', 'Bác sĩ Hô Hấp 3', 'Việt Nam', '0911002083', 'MALE'),
(2084, '1988-10-07', '4 P. Khoa Hô Hấp, Q.10', 'doctor2084@example.com', 'Bác sĩ Hô Hấp 4', 'Việt Nam', '0911002084', 'FEMALE'),
(2085, '1970-05-19', '5 P. Khoa Hô Hấp, Q.10', 'doctor2085@example.com', 'Bác sĩ Hô Hấp 5', 'Việt Nam', '0911002085', 'MALE'),
(2086, '1987-09-03', '6 P. Khoa Hô Hấp, Q.10', 'doctor2086@example.com', 'Bác sĩ Hô Hấp 6', 'Việt Nam', '0911002086', 'FEMALE'),
(2087, '1968-02-24', '7 P. Khoa Hô Hấp, Q.10', 'doctor2087@example.com', 'Bác sĩ Hô Hấp 7', 'Việt Nam', '0911002087', 'MALE'),
(2088, '1982-12-10', '8 P. Khoa Hô Hấp, Q.10', 'doctor2088@example.com', 'Bác sĩ Hô Hấp 8', 'Việt Nam', '0911002088', 'FEMALE'),
(2089, '1976-06-06', '9 P. Khoa Hô Hấp, Q.10', 'doctor2089@example.com', 'Bác sĩ Hô Hấp 9', 'Việt Nam', '0911002089', 'MALE'),
(2090, '1979-04-21', '10 P. Khoa Hô Hấp, Q.10', 'doctor2090@example.com', 'Bác sĩ Hô Hấp 10', 'Việt Nam', '0911002090', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2081, 9, 'Bác sĩ Hô Hấp', 'Chuyên khoa I'), (2082, 9, 'Bác sĩ Hô Hấp', 'Thạc sĩ Y học'),
(2083, 9, 'Trưởng khoa Hô Hấp', 'Tiến sĩ Y học'), (2084, 9, 'Bác sĩ Hô Hấp', 'Chuyên khoa II'),
(2085, 9, 'Bác sĩ Hô Hấp', 'Thạc sĩ Y học'), (2086, 9, 'Bác sĩ Hô Hấp', 'Chuyên khoa I'),
(2087, 9, 'Chuyên gia Hô Hấp', 'Phó Giáo sư'), (2088, 9, 'Bác sĩ Hô Hấp', 'Thạc sĩ Y học'),
(2089, 9, 'Bác sĩ Hô Hấp', 'Chuyên khoa II'), (2090, 9, 'Bác sĩ Hô Hấp', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2081, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2082, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2083, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2084, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2085, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2086, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2087, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2088, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2089, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2090, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Tiết niệu (ID: 10) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2091, '1974-02-04', '1 P. Khoa Tiết Niệu, Q.11', 'doctor2091@example.com', 'Bác sĩ Tiết Niệu 1', 'Việt Nam', '0911002091', 'MALE'),
(2092, '1980-09-02', '2 P. Khoa Tiết Niệu, Q.11', 'doctor2092@example.com', 'Bác sĩ Tiết Niệu 2', 'Việt Nam', '0911002092', 'FEMALE'),
(2093, '1972-04-11', '3 P. Khoa Tiết Niệu, Q.11', 'doctor2093@example.com', 'Bác sĩ Tiết Niệu 3', 'Việt Nam', '0911002093', 'MALE'),
(2094, '1989-11-06', '4 P. Khoa Tiết Niệu, Q.11', 'doctor2094@example.com', 'Bác sĩ Tiết Niệu 4', 'Việt Nam', '0911002094', 'FEMALE'),
(2095, '1969-06-18', '5 P. Khoa Tiết Niệu, Q.11', 'doctor2095@example.com', 'Bác sĩ Tiết Niệu 5', 'Việt Nam', '0911002095', 'MALE'),
(2096, '1986-10-02', '6 P. Khoa Tiết Niệu, Q.11', 'doctor2096@example.com', 'Bác sĩ Tiết Niệu 6', 'Việt Nam', '0911002096', 'FEMALE'),
(2097, '1967-03-23', '7 P. Khoa Tiết Niệu, Q.11', 'doctor2097@example.com', 'Bác sĩ Tiết Niệu 7', 'Việt Nam', '0911002097', 'MALE'),
(2098, '1981-01-09', '8 P. Khoa Tiết Niệu, Q.11', 'doctor2098@example.com', 'Bác sĩ Tiết Niệu 8', 'Việt Nam', '0911002098', 'FEMALE'),
(2099, '1975-07-05', '9 P. Khoa Tiết Niệu, Q.11', 'doctor2099@example.com', 'Bác sĩ Tiết Niệu 9', 'Việt Nam', '0911002099', 'MALE'),
(2100, '1978-05-20', '10 P. Khoa Tiết Niệu, Q.11', 'doctor2100@example.com', 'Bác sĩ Tiết Niệu 10', 'Việt Nam', '0911002100', 'MALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2091, 10, 'Bác sĩ Tiết Niệu', 'Chuyên khoa I'), (2092, 10, 'Bác sĩ Tiết Niệu', 'Thạc sĩ Y học'),
(2093, 10, 'Trưởng khoa Tiết Niệu', 'Tiến sĩ Y học'), (2094, 10, 'Bác sĩ Tiết Niệu', 'Chuyên khoa II'),
(2095, 10, 'Bác sĩ Tiết Niệu', 'Thạc sĩ Y học'), (2096, 10, 'Bác sĩ Tiết Niệu', 'Chuyên khoa I'),
(2097, 10, 'Chuyên gia Tiết Niệu', 'Phó Giáo sư'), (2098, 10, 'Bác sĩ Tiết Niệu', 'Thạc sĩ Y học'),
(2099, 10, 'Bác sĩ Tiết Niệu', 'Chuyên khoa II'), (2100, 10, 'Bác sĩ Tiết Niệu', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2091, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2092, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2093, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2094, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2095, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2096, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2097, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2098, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2099, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2100, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Thần kinh (ID: 11) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2101, '1973-03-03', '1 P. Khoa Thần Kinh, Q.1', 'doctor2101@example.com', 'Bác sĩ Thần Kinh 1', 'Việt Nam', '0911002101', 'MALE'),
(2102, '1979-10-01', '2 P. Khoa Thần Kinh, Q.1', 'doctor2102@example.com', 'Bác sĩ Thần Kinh 2', 'Việt Nam', '0911002102', 'FEMALE'),
(2103, '1971-05-10', '3 P. Khoa Thần Kinh, Q.1', 'doctor2103@example.com', 'Bác sĩ Thần Kinh 3', 'Việt Nam', '0911002103', 'MALE'),
(2104, '1988-12-05', '4 P. Khoa Thần Kinh, Q.1', 'doctor2104@example.com', 'Bác sĩ Thần Kinh 4', 'Việt Nam', '0911002104', 'FEMALE'),
(2105, '1968-07-17', '5 P. Khoa Thần Kinh, Q.1', 'doctor2105@example.com', 'Bác sĩ Thần Kinh 5', 'Việt Nam', '0911002105', 'MALE'),
(2106, '1985-11-01', '6 P. Khoa Thần Kinh, Q.1', 'doctor2106@example.com', 'Bác sĩ Thần Kinh 6', 'Việt Nam', '0911002106', 'FEMALE'),
(2107, '1966-04-22', '7 P. Khoa Thần Kinh, Q.1', 'doctor2107@example.com', 'Bác sĩ Thần Kinh 7', 'Việt Nam', '0911002107', 'MALE'),
(2108, '1980-02-08', '8 P. Khoa Thần Kinh, Q.1', 'doctor2108@example.com', 'Bác sĩ Thần Kinh 8', 'Việt Nam', '0911002108', 'FEMALE'),
(2109, '1974-08-04', '9 P. Khoa Thần Kinh, Q.1', 'doctor2109@example.com', 'Bác sĩ Thần Kinh 9', 'Việt Nam', '0911002109', 'MALE'),
(2110, '1977-06-19', '10 P. Khoa Thần Kinh, Q.1', 'doctor2110@example.com', 'Bác sĩ Thần Kinh 10', 'Việt Nam', '0911002110', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2101, 11, 'Bác sĩ Thần Kinh', 'Chuyên khoa I'), (2102, 11, 'Bác sĩ Thần Kinh', 'Thạc sĩ Y học'),
(2103, 11, 'Trưởng khoa Thần Kinh', 'Tiến sĩ Y học'), (2104, 11, 'Bác sĩ Thần Kinh', 'Chuyên khoa II'),
(2105, 11, 'Bác sĩ Thần Kinh', 'Thạc sĩ Y học'), (2106, 11, 'Bác sĩ Thần Kinh', 'Chuyên khoa I'),
(2107, 11, 'Chuyên gia Thần Kinh', 'Giáo sư'), (2108, 11, 'Bác sĩ Thần Kinh', 'Thạc sĩ Y học'),
(2109, 11, 'Bác sĩ Thần Kinh', 'Chuyên khoa II'), (2110, 11, 'Bác sĩ Thần Kinh', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2101, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2102, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2103, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2104, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2105, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2106, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2107, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2108, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2109, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2110, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Da liễu (ID: 12) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2111, '1980-04-02', '1 P. Khoa Da Liễu, Q.3', 'doctor2111@example.com', 'Bác sĩ Da Liễu 1', 'Việt Nam', '0911002111', 'FEMALE'),
(2112, '1976-11-03', '2 P. Khoa Da Liễu, Q.3', 'doctor2112@example.com', 'Bác sĩ Da Liễu 2', 'Việt Nam', '0911002112', 'MALE'),
(2113, '1983-06-09', '3 P. Khoa Da Liễu, Q.3', 'doctor2113@example.com', 'Bác sĩ Da Liễu 3', 'Việt Nam', '0911002113', 'FEMALE'),
(2114, '1979-01-04', '4 P. Khoa Da Liễu, Q.3', 'doctor2114@example.com', 'Bác sĩ Da Liễu 4', 'Việt Nam', '0911002114', 'MALE'),
(2115, '1986-08-16', '5 P. Khoa Da Liễu, Q.3', 'doctor2115@example.com', 'Bác sĩ Da Liễu 5', 'Việt Nam', '0911002115', 'FEMALE'),
(2116, '1972-05-06', '6 P. Khoa Da Liễu, Q.3', 'doctor2116@example.com', 'Bác sĩ Da Liễu 6', 'Việt Nam', '0911002116', 'MALE'),
(2117, '1989-10-27', '7 P. Khoa Da Liễu, Q.3', 'doctor2117@example.com', 'Bác sĩ Da Liễu 7', 'Việt Nam', '0911002117', 'FEMALE'),
(2118, '1975-08-13', '8 P. Khoa Da Liễu, Q.3', 'doctor2118@example.com', 'Bác sĩ Da Liễu 8', 'Việt Nam', '0911002118', 'MALE'),
(2119, '1982-02-01', '9 P. Khoa Da Liễu, Q.3', 'doctor2119@example.com', 'Bác sĩ Da Liễu 9', 'Việt Nam', '0911002119', 'FEMALE'),
(2120, '1985-12-24', '10 P. Khoa Da Liễu, Q.3', 'doctor2120@example.com', 'Bác sĩ Da Liễu 10', 'Việt Nam', '0911002120', 'MALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2111, 12, 'Bác sĩ Da liễu', 'Chuyên khoa I'), (2112, 12, 'Bác sĩ Da liễu', 'Thạc sĩ Y học'),
(2113, 12, 'Trưởng khoa Da liễu', 'Tiến sĩ Y học'), (2114, 12, 'Bác sĩ Da liễu', 'Chuyên khoa II'),
(2115, 12, 'Bác sĩ Da liễu', 'Thạc sĩ Y học'), (2116, 12, 'Bác sĩ Da liễu', 'Chuyên khoa I'),
(2117, 12, 'Chuyên gia Da liễu', 'Phó Giáo sư'), (2118, 12, 'Bác sĩ Da liễu', 'Thạc sĩ Y học'),
(2119, 12, 'Bác sĩ Da liễu', 'Chuyên khoa II'), (2120, 12, 'Bác sĩ Da liễu', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2111, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2112, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2113, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2114, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2115, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2116, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2117, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2118, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2119, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2120, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Tai mũi họng (ID: 13) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2121, '1978-05-08', '1 P. Khoa TMH, Q.5', 'doctor2121@example.com', 'Bác sĩ TMH 1', 'Việt Nam', '0911002121', 'MALE'),
(2122, '1984-12-02', '2 P. Khoa TMH, Q.5', 'doctor2122@example.com', 'Bác sĩ TMH 2', 'Việt Nam', '0911002122', 'FEMALE'),
(2123, '1976-07-15', '3 P. Khoa TMH, Q.5', 'doctor2123@example.com', 'Bác sĩ TMH 3', 'Việt Nam', '0911002123', 'MALE'),
(2124, '1987-02-11', '4 P. Khoa TMH, Q.5', 'doctor2124@example.com', 'Bác sĩ TMH 4', 'Việt Nam', '0911002124', 'FEMALE'),
(2125, '1973-09-24', '5 P. Khoa TMH, Q.5', 'doctor2125@example.com', 'Bác sĩ TMH 5', 'Việt Nam', '0911002125', 'MALE'),
(2126, '1990-04-05', '6 P. Khoa TMH, Q.5', 'doctor2126@example.com', 'Bác sĩ TMH 6', 'Việt Nam', '0911002126', 'FEMALE'),
(2127, '1970-10-29', '7 P. Khoa TMH, Q.5', 'doctor2127@example.com', 'Bác sĩ TMH 7', 'Việt Nam', '0911002127', 'MALE'),
(2128, '1985-07-14', '8 P. Khoa TMH, Q.5', 'doctor2128@example.com', 'Bác sĩ TMH 8', 'Việt Nam', '0911002128', 'FEMALE'),
(2129, '1972-12-01', '9 P. Khoa TMH, Q.5', 'doctor2129@example.com', 'Bác sĩ TMH 9', 'Việt Nam', '0911002129', 'MALE'),
(2130, '1988-11-25', '10 P. Khoa TMH, Q.5', 'doctor2130@example.com', 'Bác sĩ TMH 10', 'Việt Nam', '0911002130', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2121, 13, 'Bác sĩ TMH', 'Chuyên khoa I'), (2122, 13, 'Bác sĩ TMH', 'Thạc sĩ Y học'),
(2123, 13, 'Trưởng khoa TMH', 'Tiến sĩ Y học'), (2124, 13, 'Bác sĩ TMH', 'Chuyên khoa II'),
(2125, 13, 'Bác sĩ TMH', 'Thạc sĩ Y học'), (2126, 13, 'Bác sĩ TMH', 'Chuyên khoa I'),
(2127, 13, 'Chuyên gia TMH', 'Phó Giáo sư'), (2128, 13, 'Bác sĩ TMH', 'Thạc sĩ Y học'),
(2129, 13, 'Bác sĩ TMH', 'Chuyên khoa II'), (2130, 13, 'Bác sĩ TMH', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2121, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2122, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2123, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2124, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2125, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2126, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2127, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2128, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2129, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2130, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Tim mạch (ID: 14) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2131, '1977-06-07', '1 P. Khoa Tim Mạch, Q.7', 'doctor2131@example.com', 'Bác sĩ Tim Mạch 1', 'Việt Nam', '0911002131', 'MALE'),
(2132, '1983-01-03', '2 P. Khoa Tim Mạch, Q.7', 'doctor2132@example.com', 'Bác sĩ Tim Mạch 2', 'Việt Nam', '0911002132', 'FEMALE'),
(2133, '1975-08-14', '3 P. Khoa Tim Mạch, Q.7', 'doctor2133@example.com', 'Bác sĩ Tim Mạch 3', 'Việt Nam', '0911002133', 'MALE'),
(2134, '1986-03-10', '4 P. Khoa Tim Mạch, Q.7', 'doctor2134@example.com', 'Bác sĩ Tim Mạch 4', 'Việt Nam', '0911002134', 'FEMALE'),
(2135, '1972-10-23', '5 P. Khoa Tim Mạch, Q.7', 'doctor2135@example.com', 'Bác sĩ Tim Mạch 5', 'Việt Nam', '0911002135', 'MALE'),
(2136, '1989-05-04', '6 P. Khoa Tim Mạch, Q.7', 'doctor2136@example.com', 'Bác sĩ Tim Mạch 6', 'Việt Nam', '0911002136', 'FEMALE'),
(2137, '1969-11-28', '7 P. Khoa Tim Mạch, Q.7', 'doctor2137@example.com', 'Bác sĩ Tim Mạch 7', 'Việt Nam', '0911002137', 'MALE'),
(2138, '1984-08-15', '8 P. Khoa Tim Mạch, Q.7', 'doctor2138@example.com', 'Bác sĩ Tim Mạch 8', 'Việt Nam', '0911002138', 'FEMALE'),
(2139, '1971-01-01', '9 P. Khoa Tim Mạch, Q.7', 'doctor2139@example.com', 'Bác sĩ Tim Mạch 9', 'Việt Nam', '0911002139', 'MALE'),
(2140, '1987-12-23', '10 P. Khoa Tim Mạch, Q.7', 'doctor2140@example.com', 'Bác sĩ Tim Mạch 10', 'Việt Nam', '0911002140', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2131, 14, 'Bác sĩ Tim Mạch', 'Chuyên khoa I'), (2132, 14, 'Bác sĩ Tim Mạch', 'Thạc sĩ Y học'),
(2133, 14, 'Trưởng khoa Tim Mạch', 'Tiến sĩ Y học'), (2134, 14, 'Bác sĩ Tim Mạch Can Thiệp', 'Chuyên khoa II'),
(2135, 14, 'Bác sĩ Tim Mạch', 'Thạc sĩ Y học'), (2136, 14, 'Bác sĩ Tim Mạch', 'Chuyên khoa I'),
(2137, 14, 'Chuyên gia Tim Mạch', 'Giáo sư'), (2138, 14, 'Bác sĩ Tim Mạch', 'Thạc sĩ Y học'),
(2139, 14, 'Bác sĩ Tim Mạch Can Thiệp', 'Chuyên khoa II'), (2140, 14, 'Bác sĩ Tim Mạch', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2131, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2132, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2133, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2134, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2135, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2136, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2137, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2138, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2139, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2140, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Tiêu hóa (ID: 15) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2141, '1976-07-06', '1 P. Khoa Tiêu Hóa, Q.BT', 'doctor2141@example.com', 'Bác sĩ Tiêu Hóa 1', 'Việt Nam', '0911002141', 'MALE'),
(2142, '1982-02-02', '2 P. Khoa Tiêu Hóa, Q.BT', 'doctor2142@example.com', 'Bác sĩ Tiêu Hóa 2', 'Việt Nam', '0911002142', 'FEMALE'),
(2143, '1974-09-13', '3 P. Khoa Tiêu Hóa, Q.BT', 'doctor2143@example.com', 'Bác sĩ Tiêu Hóa 3', 'Việt Nam', '0911002143', 'MALE'),
(2144, '1985-04-09', '4 P. Khoa Tiêu Hóa, Q.BT', 'doctor2144@example.com', 'Bác sĩ Tiêu Hóa 4', 'Việt Nam', '0911002144', 'FEMALE'),
(2145, '1971-11-22', '5 P. Khoa Tiêu Hóa, Q.BT', 'doctor2145@example.com', 'Bác sĩ Tiêu Hóa 5', 'Việt Nam', '0911002145', 'MALE'),
(2146, '1988-06-03', '6 P. Khoa Tiêu Hóa, Q.BT', 'doctor2146@example.com', 'Bác sĩ Tiêu Hóa 6', 'Việt Nam', '0911002146', 'FEMALE'),
(2147, '1968-12-27', '7 P. Khoa Tiêu Hóa, Q.BT', 'doctor2147@example.com', 'Bác sĩ Tiêu Hóa 7', 'Việt Nam', '0911002147', 'MALE'),
(2148, '1983-09-14', '8 P. Khoa Tiêu Hóa, Q.BT', 'doctor2148@example.com', 'Bác sĩ Tiêu Hóa 8', 'Việt Nam', '0911002148', 'FEMALE'),
(2149, '1970-02-02', '9 P. Khoa Tiêu Hóa, Q.BT', 'doctor2149@example.com', 'Bác sĩ Tiêu Hóa 9', 'Việt Nam', '0911002149', 'MALE'),
(2150, '1986-01-22', '10 P. Khoa Tiêu Hóa, Q.BT', 'doctor2150@example.com', 'Bác sĩ Tiêu Hóa 10', 'Việt Nam', '0911002150', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2141, 15, 'Bác sĩ Tiêu Hóa', 'Chuyên khoa I'), (2142, 15, 'Bác sĩ Tiêu Hóa', 'Thạc sĩ Y học'),
(2143, 15, 'Trưởng khoa Tiêu Hóa', 'Tiến sĩ Y học'), (2144, 15, 'Bác sĩ Nội soi Tiêu Hóa', 'Chuyên khoa II'),
(2145, 15, 'Bác sĩ Tiêu Hóa', 'Thạc sĩ Y học'), (2146, 15, 'Bác sĩ Tiêu Hóa', 'Chuyên khoa I'),
(2147, 15, 'Chuyên gia Tiêu Hóa', 'Phó Giáo sư'), (2148, 15, 'Bác sĩ Tiêu Hóa', 'Thạc sĩ Y học'),
(2149, 15, 'Bác sĩ Nội soi Tiêu Hóa', 'Chuyên khoa II'), (2150, 15, 'Bác sĩ Tiêu Hóa', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2141, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2142, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2143, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2144, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2145, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2146, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2147, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2148, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2149, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2150, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Nội tiết (ID: 16) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2151, '1975-08-05', '1 P. Khoa Nội Tiết, Q.GV', 'doctor2151@example.com', 'Bác sĩ Nội Tiết 1', 'Việt Nam', '0911002151', 'MALE'),
(2152, '1981-03-01', '2 P. Khoa Nội Tiết, Q.GV', 'doctor2152@example.com', 'Bác sĩ Nội Tiết 2', 'Việt Nam', '0911002152', 'FEMALE'),
(2153, '1973-10-12', '3 P. Khoa Nội Tiết, Q.GV', 'doctor2153@example.com', 'Bác sĩ Nội Tiết 3', 'Việt Nam', '0911002153', 'MALE'),
(2154, '1984-05-08', '4 P. Khoa Nội Tiết, Q.GV', 'doctor2154@example.com', 'Bác sĩ Nội Tiết 4', 'Việt Nam', '0911002154', 'FEMALE'),
(2155, '1970-12-21', '5 P. Khoa Nội Tiết, Q.GV', 'doctor2155@example.com', 'Bác sĩ Nội Tiết 5', 'Việt Nam', '0911002155', 'MALE'),
(2156, '1987-07-02', '6 P. Khoa Nội Tiết, Q.GV', 'doctor2156@example.com', 'Bác sĩ Nội Tiết 6', 'Việt Nam', '0911002156', 'FEMALE'),
(2157, '1967-01-26', '7 P. Khoa Nội Tiết, Q.GV', 'doctor2157@example.com', 'Bác sĩ Nội Tiết 7', 'Việt Nam', '0911002157', 'MALE'),
(2158, '1982-10-13', '8 P. Khoa Nội Tiết, Q.GV', 'doctor2158@example.com', 'Bác sĩ Nội Tiết 8', 'Việt Nam', '0911002158', 'FEMALE'),
(2159, '1976-03-03', '9 P. Khoa Nội Tiết, Q.GV', 'doctor2159@example.com', 'Bác sĩ Nội Tiết 9', 'Việt Nam', '0911002159', 'MALE'),
(2160, '1979-02-20', '10 P. Khoa Nội Tiết, Q.GV', 'doctor2160@example.com', 'Bác sĩ Nội Tiết 10', 'Việt Nam', '0911002160', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2151, 16, 'Bác sĩ Nội Tiết', 'Chuyên khoa I'), (2152, 16, 'Bác sĩ Nội Tiết', 'Thạc sĩ Y học'),
(2153, 16, 'Trưởng khoa Nội Tiết', 'Tiến sĩ Y học'), (2154, 16, 'Bác sĩ Nội Tiết', 'Chuyên khoa II'),
(2155, 16, 'Bác sĩ Nội Tiết', 'Thạc sĩ Y học'), (2156, 16, 'Bác sĩ Nội Tiết', 'Chuyên khoa I'),
(2157, 16, 'Chuyên gia Nội Tiết', 'Phó Giáo sư'), (2158, 16, 'Bác sĩ Nội Tiết', 'Thạc sĩ Y học'),
(2159, 16, 'Bác sĩ Nội Tiết', 'Chuyên khoa II'), (2160, 16, 'Bác sĩ Nội Tiết', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2151, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2152, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2153, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2154, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2155, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2156, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2157, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2158, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2159, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2160, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Thận học (ID: 17) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2161, '1974-09-04', '1 P. Khoa Thận Học, Q.TB', 'doctor2161@example.com', 'Bác sĩ Thận Học 1', 'Việt Nam', '0911002161', 'MALE'),
(2162, '1980-04-01', '2 P. Khoa Thận Học, Q.TB', 'doctor2162@example.com', 'Bác sĩ Thận Học 2', 'Việt Nam', '0911002162', 'FEMALE'),
(2163, '1972-11-11', '3 P. Khoa Thận Học, Q.TB', 'doctor2163@example.com', 'Bác sĩ Thận Học 3', 'Việt Nam', '0911002163', 'MALE'),
(2164, '1983-06-07', '4 P. Khoa Thận Học, Q.TB', 'doctor2164@example.com', 'Bác sĩ Thận Học 4', 'Việt Nam', '0911002164', 'FEMALE'),
(2165, '1969-01-20', '5 P. Khoa Thận Học, Q.TB', 'doctor2165@example.com', 'Bác sĩ Thận Học 5', 'Việt Nam', '0911002165', 'MALE'),
(2166, '1986-08-02', '6 P. Khoa Thận Học, Q.TB', 'doctor2166@example.com', 'Bác sĩ Thận Học 6', 'Việt Nam', '0911002166', 'FEMALE'),
(2167, '1966-02-25', '7 P. Khoa Thận Học, Q.TB', 'doctor2167@example.com', 'Bác sĩ Thận Học 7', 'Việt Nam', '0911002167', 'MALE'),
(2168, '1981-11-12', '8 P. Khoa Thận Học, Q.TB', 'doctor2168@example.com', 'Bác sĩ Thận Học 8', 'Việt Nam', '0911002168', 'FEMALE'),
(2169, '1975-04-04', '9 P. Khoa Thận Học, Q.TB', 'doctor2169@example.com', 'Bác sĩ Thận Học 9', 'Việt Nam', '0911002169', 'MALE'),
(2170, '1978-03-19', '10 P. Khoa Thận Học, Q.TB', 'doctor2170@example.com', 'Bác sĩ Thận Học 10', 'Việt Nam', '0911002170', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2161, 17, 'Bác sĩ Thận Học', 'Chuyên khoa I'), (2162, 17, 'Bác sĩ Thận Học', 'Thạc sĩ Y học'),
(2163, 17, 'Trưởng khoa Thận Học', 'Tiến sĩ Y học'), (2164, 17, 'Bác sĩ Thận Nhân Tạo', 'Chuyên khoa II'),
(2165, 17, 'Bác sĩ Thận Học', 'Thạc sĩ Y học'), (2166, 17, 'Bác sĩ Thận Học', 'Chuyên khoa I'),
(2167, 17, 'Chuyên gia Thận Học', 'Phó Giáo sư'), (2168, 17, 'Bác sĩ Thận Học', 'Thạc sĩ Y học'),
(2169, 17, 'Bác sĩ Thận Nhân Tạo', 'Chuyên khoa II'), (2170, 17, 'Bác sĩ Thận Học', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2161, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2162, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2163, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2164, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2165, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2166, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2167, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2168, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2169, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2170, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Ung bướu (ID: 18) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2171, '1973-10-03', '1 P. Khoa Ung Bướu, Q.PN', 'doctor2171@example.com', 'Bác sĩ Ung Bướu 1', 'Việt Nam', '0911002171', 'MALE'),
(2172, '1979-05-01', '2 P. Khoa Ung Bướu, Q.PN', 'doctor2172@example.com', 'Bác sĩ Ung Bướu 2', 'Việt Nam', '0911002172', 'FEMALE'),
(2173, '1971-12-10', '3 P. Khoa Ung Bướu, Q.PN', 'doctor2173@example.com', 'Bác sĩ Ung Bướu 3', 'Việt Nam', '0911002173', 'MALE'),
(2174, '1982-07-06', '4 P. Khoa Ung Bướu, Q.PN', 'doctor2174@example.com', 'Bác sĩ Ung Bướu 4', 'Việt Nam', '0911002174', 'FEMALE'),
(2175, '1968-02-19', '5 P. Khoa Ung Bướu, Q.PN', 'doctor2175@example.com', 'Bác sĩ Ung Bướu 5', 'Việt Nam', '0911002175', 'MALE'),
(2176, '1985-09-01', '6 P. Khoa Ung Bướu, Q.PN', 'doctor2176@example.com', 'Bác sĩ Ung Bướu 6', 'Việt Nam', '0911002176', 'FEMALE'),
(2177, '1965-03-24', '7 P. Khoa Ung Bướu, Q.PN', 'doctor2177@example.com', 'Bác sĩ Ung Bướu 7', 'Việt Nam', '0911002177', 'MALE'),
(2178, '1980-12-11', '8 P. Khoa Ung Bướu, Q.PN', 'doctor2178@example.com', 'Bác sĩ Ung Bướu 8', 'Việt Nam', '0911002178', 'FEMALE'),
(2179, '1974-05-03', '9 P. Khoa Ung Bướu, Q.PN', 'doctor2179@example.com', 'Bác sĩ Ung Bướu 9', 'Việt Nam', '0911002179', 'MALE'),
(2180, '1977-04-18', '10 P. Khoa Ung Bướu, Q.PN', 'doctor2180@example.com', 'Bác sĩ Ung Bướu 10', 'Việt Nam', '0911002180', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2171, 18, 'Bác sĩ Ung Bướu', 'Chuyên khoa I'), (2172, 18, 'Bác sĩ Ung Bướu', 'Thạc sĩ Y học'),
(2173, 18, 'Trưởng khoa Ung Bướu', 'Tiến sĩ Y học'), (2174, 18, 'Bác sĩ Xạ Trị', 'Chuyên khoa II'),
(2175, 18, 'Bác sĩ Ung Bướu', 'Thạc sĩ Y học'), (2176, 18, 'Bác sĩ Ung Bướu', 'Chuyên khoa I'),
(2177, 18, 'Chuyên gia Ung Bướu', 'Giáo sư'), (2178, 18, 'Bác sĩ Ung Bướu', 'Thạc sĩ Y học'),
(2179, 18, 'Bác sĩ Xạ Trị', 'Chuyên khoa II'), (2180, 18, 'Bác sĩ Ung Bướu', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2171, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2172, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2173, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2174, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2175, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2176, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2177, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2178, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2179, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2180, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Nhãn khoa (ID: 19) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2181, '1972-11-02', '1 P. Khoa Mắt, Q.10', 'doctor2181@example.com', 'Bác sĩ Nhãn Khoa 1', 'Việt Nam', '0911002181', 'MALE'),
(2182, '1978-06-02', '2 P. Khoa Mắt, Q.10', 'doctor2182@example.com', 'Bác sĩ Nhãn Khoa 2', 'Việt Nam', '0911002182', 'FEMALE'),
(2183, '1970-01-09', '3 P. Khoa Mắt, Q.10', 'doctor2183@example.com', 'Bác sĩ Nhãn Khoa 3', 'Việt Nam', '0911002183', 'MALE'),
(2184, '1981-08-05', '4 P. Khoa Mắt, Q.10', 'doctor2184@example.com', 'Bác sĩ Nhãn Khoa 4', 'Việt Nam', '0911002184', 'FEMALE'),
(2185, '1967-03-18', '5 P. Khoa Mắt, Q.10', 'doctor2185@example.com', 'Bác sĩ Nhãn Khoa 5', 'Việt Nam', '0911002185', 'MALE'),
(2186, '1984-10-01', '6 P. Khoa Mắt, Q.10', 'doctor2186@example.com', 'Bác sĩ Nhãn Khoa 6', 'Việt Nam', '0911002186', 'FEMALE'),
(2187, '1964-04-23', '7 P. Khoa Mắt, Q.10', 'doctor2187@example.com', 'Bác sĩ Nhãn Khoa 7', 'Việt Nam', '0911002187', 'MALE'),
(2188, '1979-12-10', '8 P. Khoa Mắt, Q.10', 'doctor2188@example.com', 'Bác sĩ Nhãn Khoa 8', 'Việt Nam', '0911002188', 'FEMALE'),
(2189, '1973-06-04', '9 P. Khoa Mắt, Q.10', 'doctor2189@example.com', 'Bác sĩ Nhãn Khoa 9', 'Việt Nam', '0911002189', 'MALE'),
(2190, '1976-05-17', '10 P. Khoa Mắt, Q.10', 'doctor2190@example.com', 'Bác sĩ Nhãn Khoa 10', 'Việt Nam', '0911002190', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2181, 19, 'Bác sĩ Nhãn Khoa', 'Chuyên khoa I'), (2182, 19, 'Bác sĩ Nhãn Khoa', 'Thạc sĩ Y học'),
(2183, 19, 'Trưởng khoa Mắt', 'Tiến sĩ Y học'), (2184, 19, 'Bác sĩ Phẫu thuật Mắt', 'Chuyên khoa II'),
(2185, 19, 'Bác sĩ Nhãn Khoa', 'Thạc sĩ Y học'), (2186, 19, 'Bác sĩ Nhãn Khoa', 'Chuyên khoa I'),
(2187, 19, 'Chuyên gia Nhãn Khoa', 'Phó Giáo sư'), (2188, 19, 'Bác sĩ Nhãn Khoa', 'Thạc sĩ Y học'),
(2189, 19, 'Bác sĩ Phẫu thuật Mắt', 'Chuyên khoa II'), (2190, 19, 'Bác sĩ Nhãn Khoa', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2181, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2182, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2183, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2184, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2185, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2186, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2187, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2188, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2189, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2190, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');

-- === Specialty: Bệnh truyền nhiễm (ID: 20) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2191, '1971-10-02', '1 P. Khoa Truyền Nhiễm, Q.11', 'doctor2191@example.com', 'Bác sĩ Truyền Nhiễm 1', 'Việt Nam', '0911002191', 'MALE'),
(2192, '1977-07-01', '2 P. Khoa Truyền Nhiễm, Q.11', 'doctor2192@example.com', 'Bác sĩ Truyền Nhiễm 2', 'Việt Nam', '0911002192', 'FEMALE'),
(2193, '1969-02-08', '3 P. Khoa Truyền Nhiễm, Q.11', 'doctor2193@example.com', 'Bác sĩ Truyền Nhiễm 3', 'Việt Nam', '0911002193', 'MALE'),
(2194, '1980-09-04', '4 P. Khoa Truyền Nhiễm, Q.11', 'doctor2194@example.com', 'Bác sĩ Truyền Nhiễm 4', 'Việt Nam', '0911002194', 'FEMALE'),
(2195, '1966-04-17', '5 P. Khoa Truyền Nhiễm, Q.11', 'doctor2195@example.com', 'Bác sĩ Truyền Nhiễm 5', 'Việt Nam', '0911002195', 'MALE'),
(2196, '1983-11-01', '6 P. Khoa Truyền Nhiễm, Q.11', 'doctor2196@example.com', 'Bác sĩ Truyền Nhiễm 6', 'Việt Nam', '0911002196', 'FEMALE'),
(2197, '1963-05-22', '7 P. Khoa Truyền Nhiễm, Q.11', 'doctor2197@example.com', 'Bác sĩ Truyền Nhiễm 7', 'Việt Nam', '0911002197', 'MALE'),
(2198, '1978-01-09', '8 P. Khoa Truyền Nhiễm, Q.11', 'doctor2198@example.com', 'Bác sĩ Truyền Nhiễm 8', 'Việt Nam', '0911002198', 'FEMALE'),
(2199, '1972-07-03', '9 P. Khoa Truyền Nhiễm, Q.11', 'doctor2199@example.com', 'Bác sĩ Truyền Nhiễm 9', 'Việt Nam', '0911002199', 'MALE'),
(2200, '1975-06-16', '10 P. Khoa Truyền Nhiễm, Q.11', 'doctor2200@example.com', 'Bác sĩ Truyền Nhiễm 10', 'Việt Nam', '0911002200', 'FEMALE');

INSERT INTO tbl_doctor (id, medical_specialty_id, position, qualification) VALUES
(2191, 20, 'Bác sĩ Truyền Nhiễm', 'Chuyên khoa I'), (2192, 20, 'Bác sĩ Truyền Nhiễm', 'Thạc sĩ Y học'),
(2193, 20, 'Trưởng khoa Truyền Nhiễm', 'Tiến sĩ Y học'), (2194, 20, 'Bác sĩ Truyền Nhiễm', 'Chuyên khoa II'),
(2195, 20, 'Bác sĩ Truyền Nhiễm', 'Thạc sĩ Y học'), (2196, 20, 'Bác sĩ Truyền Nhiễm', 'Chuyên khoa I'),
(2197, 20, 'Chuyên gia Truyền Nhiễm', 'Phó Giáo sư'), (2198, 20, 'Bác sĩ Truyền Nhiễm', 'Thạc sĩ Y học'),
(2199, 20, 'Bác sĩ Truyền Nhiễm', 'Chuyên khoa II'), (2200, 20, 'Bác sĩ Truyền Nhiễm', 'Thạc sĩ Y học');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2191, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2192, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2193, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2194, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2195, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2196, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2197, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2198, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'),
(2199, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE'), (2200, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'DOCTOR', 'ACTIVE');


-- ==================================================
-- START: INSERT NURSES AND ACCOUNTS (50 total)
-- ==================================================

-- === Insert Users for Nurses (tbl_user) ===
INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2201, '1993-02-10', '101 Khu Điều Dưỡng A, Q.1', 'nurse2201@example.com', 'Nguyễn Thị An', 'Việt Nam', '0922002201', 'FEMALE'),
(2202, '1995-07-21', '102 Khu Điều Dưỡng A, Q.1', 'nurse2202@example.com', 'Trần Văn Bình', 'Việt Nam', '0922002202', 'MALE'),
(2203, '1991-11-05', '103 Khu Điều Dưỡng A, Q.1', 'nurse2203@example.com', 'Lê Thị Cúc', 'Việt Nam', '0922002203', 'FEMALE'),
(2204, '1996-04-18', '104 Khu Điều Dưỡng B, Q.3', 'nurse2204@example.com', 'Phạm Văn Dũng', 'Việt Nam', '0922002204', 'MALE'),
(2205, '1990-09-30', '105 Khu Điều Dưỡng B, Q.3', 'nurse2205@example.com', 'Hoàng Thị Em', 'Việt Nam', '0922002205', 'FEMALE'),
(2206, '1994-01-12', '106 Khu Điều Dưỡng B, Q.3', 'nurse2206@example.com', 'Võ Thị F', 'Việt Nam', '0922002206', 'FEMALE'),
(2207, '1992-06-25', '107 Khu Điều Dưỡng C, Q.5', 'nurse2207@example.com', 'Đặng Văn Giang', 'Việt Nam', '0922002207', 'MALE'),
(2208, '1997-10-08', '108 Khu Điều Dưỡng C, Q.5', 'nurse2208@example.com', 'Bùi Thị Hạnh', 'Việt Nam', '0922002208', 'FEMALE'),
(2209, '1989-03-14', '109 Khu Điều Dưỡng C, Q.5', 'nurse2209@example.com', 'Ngô Thị Kim', 'Việt Nam', '0922002209', 'FEMALE'),
(2210, '1993-08-27', '110 Khu Điều Dưỡng D, Q.7', 'nurse2210@example.com', 'Dương Văn Long', 'Việt Nam', '0922002210', 'MALE'),
(2211, '1995-12-01', '111 Khu Điều Dưỡng D, Q.7', 'nurse2211@example.com', 'Nguyễn Thị Mai', 'Việt Nam', '0922002211', 'FEMALE'),
(2212, '1990-05-16', '112 Khu Điều Dưỡng D, Q.7', 'nurse2212@example.com', 'Trần Thị Nga', 'Việt Nam', '0922002212', 'FEMALE'),
(2213, '1996-09-09', '113 Khu Điều Dưỡng E, Q.TB', 'nurse2213@example.com', 'Lê Văn Oanh', 'Việt Nam', '0922002213', 'MALE'),
(2214, '1991-02-22', '114 Khu Điều Dưỡng E, Q.TB', 'nurse2214@example.com', 'Phạm Thị Phượng', 'Việt Nam', '0922002214', 'FEMALE'),
(2215, '1994-07-07', '115 Khu Điều Dưỡng E, Q.TB', 'nurse2215@example.com', 'Hoàng Thị Quyên', 'Việt Nam', '0922002215', 'FEMALE'),
(2216, '1992-11-19', '116 Khu Điều Dưỡng F, Q.GV', 'nurse2216@example.com', 'Võ Văn Sang', 'Việt Nam', '0922002216', 'MALE'),
(2217, '1997-03-03', '117 Khu Điều Dưỡng F, Q.GV', 'nurse2217@example.com', 'Đặng Thị Tâm', 'Việt Nam', '0922002217', 'FEMALE'),
(2218, '1988-08-15', '118 Khu Điều Dưỡng F, Q.GV', 'nurse2218@example.com', 'Bùi Thị Uyên', 'Việt Nam', '0922002218', 'FEMALE'),
(2219, '1993-12-28', '119 Khu Điều Dưỡng G, Q.PN', 'nurse2219@example.com', 'Ngô Văn Việt', 'Việt Nam', '0922002219', 'MALE'),
(2220, '1990-04-11', '120 Khu Điều Dưỡng G, Q.PN', 'nurse2220@example.com', 'Dương Thị Xuân', 'Việt Nam', '0922002220', 'FEMALE'),
(2221, '1995-08-01', '121 Khu Điều Dưỡng G, Q.PN', 'nurse2221@example.com', 'Nguyễn Thị Yến', 'Việt Nam', '0922002221', 'FEMALE'),
(2222, '1991-01-14', '122 Khu Điều Dưỡng H, Q.10', 'nurse2222@example.com', 'Trần Văn An', 'Việt Nam', '0922002222', 'MALE'),
(2223, '1996-06-27', '123 Khu Điều Dưỡng H, Q.10', 'nurse2223@example.com', 'Lê Thị Bảo', 'Việt Nam', '0922002223', 'FEMALE'),
(2224, '1990-10-10', '124 Khu Điều Dưỡng H, Q.10', 'nurse2224@example.com', 'Phạm Thị Châu', 'Việt Nam', '0922002224', 'FEMALE'),
(2225, '1994-03-24', '125 Khu Điều Dưỡng I, Q.11', 'nurse2225@example.com', 'Hoàng Văn Danh', 'Việt Nam', '0922002225', 'MALE'),
(2226, '1992-08-06', '126 Khu Điều Dưỡng I, Q.11', 'nurse2226@example.com', 'Võ Thị Diễm', 'Việt Nam', '0922002226', 'FEMALE'),
(2227, '1997-12-18', '127 Khu Điều Dưỡng I, Q.11', 'nurse2227@example.com', 'Đặng Thị Giang', 'Việt Nam', '0922002227', 'FEMALE'),
(2228, '1989-05-02', '128 Khu Điều Dưỡng K, Q.BT', 'nurse2228@example.com', 'Bùi Văn Hào', 'Việt Nam', '0922002228', 'MALE'),
(2229, '1993-10-15', '129 Khu Điều Dưỡng K, Q.BT', 'nurse2229@example.com', 'Ngô Thị Hiền', 'Việt Nam', '0922002229', 'FEMALE'),
(2230, '1990-01-27', '130 Khu Điều Dưỡng K, Q.BT', 'nurse2230@example.com', 'Dương Thị Hoa', 'Việt Nam', '0922002230', 'FEMALE'),
(2231, '1995-06-11', '131 Khu Điều Dưỡng A, Q.1', 'nurse2231@example.com', 'Nguyễn Văn Khải', 'Việt Nam', '0922002231', 'MALE'),
(2232, '1991-11-23', '132 Khu Điều Dưỡng A, Q.1', 'nurse2232@example.com', 'Trần Thị Lan', 'Việt Nam', '0922002232', 'FEMALE'),
(2233, '1996-03-07', '133 Khu Điều Dưỡng B, Q.3', 'nurse2233@example.com', 'Lê Thị Lệ', 'Việt Nam', '0922002233', 'FEMALE'),
(2234, '1990-07-20', '134 Khu Điều Dưỡng B, Q.3', 'nurse2234@example.com', 'Phạm Văn Minh', 'Việt Nam', '0922002234', 'MALE'),
(2235, '1994-12-02', '135 Khu Điều Dưỡng C, Q.5', 'nurse2235@example.com', 'Hoàng Thị Mỹ', 'Việt Nam', '0922002235', 'FEMALE'),
(2236, '1992-05-15', '136 Khu Điều Dưỡng C, Q.5', 'nurse2236@example.com', 'Võ Thị Ngà', 'Việt Nam', '0922002236', 'FEMALE'),
(2237, '1997-09-27', '137 Khu Điều Dưỡng D, Q.7', 'nurse2237@example.com', 'Đặng Văn Nhân', 'Việt Nam', '0922002237', 'MALE'),
(2238, '1989-01-10', '138 Khu Điều Dưỡng D, Q.7', 'nurse2238@example.com', 'Bùi Thị Oanh', 'Việt Nam', '0922002238', 'FEMALE'),
(2239, '1993-06-23', '139 Khu Điều Dưỡng E, Q.TB', 'nurse2239@example.com', 'Ngô Thị Phương', 'Việt Nam', '0922002239', 'FEMALE'),
(2240, '1990-11-05', '140 Khu Điều Dưỡng E, Q.TB', 'nurse2240@example.com', 'Dương Văn Quang', 'Việt Nam', '0922002240', 'MALE'),
(2241, '1995-04-18', '141 Khu Điều Dưỡng F, Q.GV', 'nurse2241@example.com', 'Nguyễn Thị Quỳnh', 'Việt Nam', '0922002241', 'FEMALE'),
(2242, '1991-09-01', '142 Khu Điều Dưỡng F, Q.GV', 'nurse2242@example.com', 'Trần Thị Sen', 'Việt Nam', '0922002242', 'FEMALE'),
(2243, '1996-01-13', '143 Khu Điều Dưỡng G, Q.PN', 'nurse2243@example.com', 'Lê Văn Tài', 'Việt Nam', '0922002243', 'MALE'),
(2244, '1990-06-26', '144 Khu Điều Dưỡng G, Q.PN', 'nurse2244@example.com', 'Phạm Thị Thủy', 'Việt Nam', '0922002244', 'FEMALE'),
(2245, '1994-11-08', '145 Khu Điều Dưỡng H, Q.10', 'nurse2245@example.com', 'Hoàng Thị Trang', 'Việt Nam', '0922002245', 'FEMALE'),
(2246, '1992-04-21', '146 Khu Điều Dưỡng H, Q.10', 'nurse2246@example.com', 'Võ Văn Trung', 'Việt Nam', '0922002246', 'MALE'),
(2247, '1997-08-04', '147 Khu Điều Dưỡng I, Q.11', 'nurse2247@example.com', 'Đặng Thị Vân', 'Việt Nam', '0922002247', 'FEMALE'),
(2248, '1988-12-17', '148 Khu Điều Dưỡng I, Q.11', 'nurse2248@example.com', 'Bùi Thị Vy', 'Việt Nam', '0922002248', 'FEMALE'),
(2249, '1993-05-30', '149 Khu Điều Dưỡng K, Q.BT', 'nurse2249@example.com', 'Ngô Văn Yên', 'Việt Nam', '0922002249', 'MALE'),
(2250, '1990-10-12', '150 Khu Điều Dưỡng K, Q.BT', 'nurse2250@example.com', 'Dương Thị Ánh', 'Việt Nam', '0922002250', 'FEMALE');

-- === Insert Nurse specific info (tbl_nurse) ===

INSERT INTO tbl_nurse (id, position, qualification, medical_specialty_id) VALUES
(2201, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2202, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2203, 'Điều dưỡng trưởng', 'Cử nhân Điều dưỡng', 2),
(2204, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 2),
(2205, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 3),
(2206, 'Điều dưỡng chuyên khoa', 'Chứng chỉ Gây mê hồi sức', 3),
(2207, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 4),
(2208, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 4),
(2209, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 5),
(2210, 'Điều dưỡng trưởng Ca', 'Cử nhân Điều dưỡng', 5),
(2211, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 6),
(2212, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 6),
(2213, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 7),
(2214, 'Điều dưỡng chuyên khoa', 'Chứng chỉ Hồi sức cấp cứu', 7),
(2215, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 8),
(2216, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 8),
(2217, 'Điều dưỡng trưởng Khoa', 'Thạc sĩ Điều dưỡng', 9),
(2218, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 9),
(2219, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 10),
(2220, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 10),
(2221, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2222, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2223, 'Điều dưỡng chuyên khoa', 'Chứng chỉ Chăm sóc Nhi', 2),
(2224, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2225, 'Điều dưỡng trưởng Ca', 'Cử nhân Điều dưỡng', 3),
(2226, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2227, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 4),
(2228, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2229, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 5),
(2230, 'Điều dưỡng trưởng', 'Cử nhân Điều dưỡng', 1),
(2231, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 6),
(2232, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2233, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 7),
(2234, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2235, 'Điều dưỡng chuyên khoa', 'Chứng chỉ Tim mạch', 8),
(2236, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2237, 'Điều dưỡng trưởng Khoa', 'Thạc sĩ Điều dưỡng', 9),
(2238, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2239, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 10),
(2240, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 1),
(2241, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 1),
(2242, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 19),
(2243, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 2),
(2244, 'Điều dưỡng chuyên khoa', 'Chứng chỉ Ung bướu', 11),
(2245, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 12),
(2246, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 16),
(2247, 'Điều dưỡng trưởng Ca', 'Cử nhân Điều dưỡng', 13),
(2248, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 17),
(2249, 'Điều dưỡng viên', 'Cử nhân Điều dưỡng', 14),
(2250, 'Điều dưỡng viên', 'Cao đẳng Điều dưỡng', 18);

-- === Insert Accounts for Nurses (tbl_account) ===
-- WARNING: Plain text password '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW' - INSECURE!
INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2201, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2202, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2203, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2204, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2205, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2206, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2207, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2208, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2209, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2210, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2211, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2212, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2213, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2214, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2215, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2216, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2217, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2218, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2219, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2220, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2221, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2222, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2223, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2224, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2225, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2226, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2227, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2228, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2229, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2230, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2231, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2232, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2233, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2234, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2235, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2236, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2237, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2238, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2239, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2240, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2241, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2242, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2243, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2244, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2245, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2246, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2247, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2248, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'),
(2249, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE'), (2250, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'NURSE', 'ACTIVE');


INSERT INTO tbl_room_detail (id, floor, building, name) VALUES
-- Khu A (Tổng quát, Da liễu, TMH, Mắt, Tâm thần) - Tầng G & 1
(201, 0, 'Khu A', 'Phòng AG01 - Đón tiếp & Hướng dẫn Khu A'),
(202, 0, 'Khu A', 'Phòng AG02 - Khám Tổng Quát Sơ bộ'),
(203, 0, 'Khu A', 'Phòng AG03 - Khám Da Liễu 1'),
(204, 0, 'Khu A', 'Phòng AG04 - Khám Da Liễu 2'),
(205, 0, 'Khu A', 'Phòng AG05 - Thủ thuật Da Liễu'),
(206, 1, 'Khu A', 'Phòng A101 - Khám Tai Mũi Họng 1'),
(207, 1, 'Khu A', 'Phòng A102 - Khám Tai Mũi Họng 2'),
(208, 1, 'Khu A', 'Phòng A103 - Nội soi Tai Mũi Họng'),
(209, 1, 'Khu A', 'Phòng A104 - Đo Thính Lực'),
(210, 1, 'Khu A', 'Phòng A105 - Khám Mắt (Nhãn Khoa) 1'),
(211, 1, 'Khu A', 'Phòng A106 - Khám Mắt (Nhãn Khoa) 2'),
(212, 1, 'Khu A', 'Phòng A107 - Đo Thị Lực & Khúc Xạ'),
(213, 1, 'Khu A', 'Phòng A108 - Tư vấn Tâm Thần 1'),
(214, 1, 'Khu A', 'Phòng A109 - Tư vấn Tâm Thần 2'),
(215, 1, 'Khu A', 'Phòng A110 - Phòng Chờ Khu A Tầng 1'),

-- Khu B (Nội khoa, Tim mạch, Hô hấp, Tiêu hóa, Nội tiết, Thận học) - Tầng 2
(216, 2, 'Khu B', 'Phòng B201 - Khám Nội Khoa 1'),
(217, 2, 'Khu B', 'Phòng B202 - Khám Nội Khoa 2'),
(218, 2, 'Khu B', 'Phòng B203 - Khám Nội Khoa 3'),
(219, 2, 'Khu B', 'Phòng B204 - Khám Tim Mạch 1'),
(220, 2, 'Khu B', 'Phòng B205 - Khám Tim Mạch 2'),
(221, 2, 'Khu B', 'Phòng B206 - Phòng Điện Tâm Đồ (ECG)'),
(222, 2, 'Khu B', 'Phòng B207 - Khám Hô Hấp 1'),
(223, 2, 'Khu B', 'Phòng B208 - Khám Hô Hấp 2'),
(224, 2, 'Khu B', 'Phòng B209 - Đo Chức Năng Hô Hấp'),
(225, 2, 'Khu B', 'Phòng B210 - Khám Tiêu Hóa 1'),
(226, 2, 'Khu B', 'Phòng B211 - Khám Tiêu Hóa 2'),
(227, 2, 'Khu B', 'Phòng B212 - Nội Soi Tiêu Hóa 1'),
(228, 2, 'Khu B', 'Phòng B213 - Nội Soi Tiêu Hóa 2 (Gây mê)'),
(229, 2, 'Khu B', 'Phòng B214 - Khám Nội Tiết 1'),
(230, 2, 'Khu B', 'Phòng B215 - Khám Nội Tiết 2'),
(231, 2, 'Khu B', 'Phòng B216 - Tư Vấn Dinh Dưỡng & Tiểu Đường'),
(232, 2, 'Khu B', 'Phòng B217 - Khám Thận Học 1'),
(233, 2, 'Khu B', 'Phòng B218 - Khám Thận Học 2'),
(234, 2, 'Khu B', 'Phòng B219 - Phòng Chờ Khu B Tầng 2'),
(235, 2, 'Khu B', 'Phòng B220 - Văn phòng Bác sĩ Nội trú'),

-- Khu C (Ngoại, CTCH, Tiết niệu, Thần kinh, Ung Bướu) - Tầng 3
(236, 3, 'Khu C', 'Phòng C301 - Khám Ngoại Tổng Quát 1'),
(237, 3, 'Khu C', 'Phòng C302 - Khám Ngoại Tổng Quát 2'),
(238, 3, 'Khu C', 'Phòng C303 - Phòng Tiểu Phẫu Ngoại Khoa'),
(239, 3, 'Khu C', 'Phòng C304 - Phòng Thay Băng & Cắt Chỉ'),
(240, 3, 'Khu C', 'Phòng C305 - Khám Chấn Thương Chỉnh Hình 1'),
(241, 3, 'Khu C', 'Phòng C306 - Khám Chấn Thương Chỉnh Hình 2'),
(242, 3, 'Khu C', 'Phòng C307 - Phòng Bó Bột'),
(243, 3, 'Khu C', 'Phòng C308 - Khám Tiết Niệu 1'),
(244, 3, 'Khu C', 'Phòng C309 - Khám Tiết Niệu 2'),
(245, 3, 'Khu C', 'Phòng C310 - Nội Soi Tiết Niệu'),
(246, 3, 'Khu C', 'Phòng C311 - Khám Thần Kinh 1'),
(247, 3, 'Khu C', 'Phòng C312 - Khám Thần Kinh 2'),
(248, 3, 'Khu C', 'Phòng C313 - Phòng Đo Điện Não Đồ (EEG)'),
(249, 3, 'Khu C', 'Phòng C314 - Khám Ung Bướu 1'),
(250, 3, 'Khu C', 'Phòng C315 - Khám Ung Bướu 2'),
(251, 3, 'Khu C', 'Phòng C316 - Tư Vấn Ung Bướu'),
(252, 3, 'Khu C', 'Phòng C317 - Phòng Chờ Khu C Tầng 3'),
(253, 3, 'Khu C', 'Phòng C318 - Văn phòng Bác sĩ Ngoại trú'),

-- Khu D (Sản Phụ Khoa, Nhi Khoa, Răng Hàm Mặt) - Tầng 4
(254, 4, 'Khu D', 'Phòng D401 - Khám Sản Phụ Khoa 1'),
(255, 4, 'Khu D', 'Phòng D402 - Khám Sản Phụ Khoa 2'),
(256, 4, 'Khu D', 'Phòng D403 - Siêu Âm Sản Phụ Khoa'),
(257, 4, 'Khu D', 'Phòng D404 - Phòng Khám Thai'),
(258, 4, 'Khu D', 'Phòng D405 - Tư Vấn Tiền Sản'),
(259, 4, 'Khu D', 'Phòng D406 - Khám Nhi Khoa 1'),
(260, 4, 'Khu D', 'Phòng D407 - Khám Nhi Khoa 2'),
(261, 4, 'Khu D', 'Phòng D408 - Khám Nhi Khoa 3'),
(262, 4, 'Khu D', 'Phòng D409 - Phòng Tiêm Chủng Nhi'),
(263, 4, 'Khu D', 'Phòng D410 - Phòng Chờ Nhi Khoa (Có khu vui chơi)'),
(264, 4, 'Khu D', 'Phòng D411 - Khám Răng Hàm Mặt 1 (Ghế 1)'),
(265, 4, 'Khu D', 'Phòng D412 - Khám Răng Hàm Mặt 2 (Ghế 2)'),
(266, 4, 'Khu D', 'Phòng D413 - Khám Răng Hàm Mặt 3 (Ghế 3)'),
(267, 4, 'Khu D', 'Phòng D414 - Chụp X-Quang Răng (Panorex/Cephalo)'),
(268, 4, 'Khu D', 'Phòng D415 - Phòng Chờ Khu D Tầng 4'),

-- Khu CLS (Cận Lâm Sàng: Xét nghiệm, CĐHA, Bệnh truyền nhiễm) - Tầng G
(269, 0, 'Khu CLS', 'Phòng CLSG01 - Lấy Mẫu Xét Nghiệm Máu'),
(270, 0, 'Khu CLS', 'Phòng CLSG02 - Lấy Mẫu Xét Nghiệm Nước Tiểu/Khác'),
(271, 0, 'Khu CLS', 'Phòng CLSG03 - Nhận/Trả Kết Quả Xét Nghiệm'),
(272, 0, 'Khu CLS', 'Phòng CLSG04 - Siêu Âm Tổng Quát 1'),
(273, 0, 'Khu CLS', 'Phòng CLSG05 - Siêu Âm Tổng Quát 2'),
(274, 0, 'Khu CLS', 'Phòng CLSG06 - Siêu Âm Doppler Mạch Máu'),
(275, 0, 'Khu CLS', 'Phòng CLSG07 - Chụp X-Quang Kỹ Thuật Số 1'),
(276, 0, 'Khu CLS', 'Phòng CLSG08 - Chụp X-Quang Kỹ Thuật Số 2'),
(277, 0, 'Khu CLS', 'Phòng CLSG09 - Khám Bệnh Truyền Nhiễm 1'),
(278, 0, 'Khu CLS', 'Phòng CLSG10 - Khám Bệnh Truyền Nhiễm 2 (Cách ly)'),
(279, 0, 'Khu CLS', 'Phòng CLSG11 - Phòng Chờ Khu CLS'),
(280, 0, 'Khu CLS', 'Phòng CLSG12 - Nhà thuốc Bệnh viện');

INSERT INTO tbl_patient (id, name, date_of_birth, gender, address, phone, cccd, email, nation, career) VALUES
(1, 'Nguyễn Thị Mai', '1995-08-12', 'FEMALE', '12 Phố Huế, Quận Hai Bà Trưng, Hà Nội', '0901112233', '001195001234', 'ntmai.95@example.com', 'Việt Nam', 'Kế toán'),
(2, 'Trần Văn Nam', '1988-04-25', 'MALE', '45 Đường Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh', '0912223344', '079088005678', 'namtran.dev@email.com', 'Việt Nam', 'Lập trình viên'),
(3, 'Lê Thị Hoa', '2005-11-01', 'FEMALE', '78 Đường Hùng Vương, Quận Ninh Kiều, Cần Thơ', '0923334455', '092205009876', 'hoa.lethi.2005@domain.vn', 'Việt Nam', 'Học sinh'),
(4, 'Phạm Minh Tuấn', '1976-02-18', 'MALE', '101 Đường Trần Phú, Quận 5, TP. Hồ Chí Minh', '0934445566', '079076001122', 'tuanpham.ceo@company.com', 'Việt Nam', 'Giám đốc kinh doanh'),
(5, 'Hoàng Thị Thu', '1999-07-07', 'FEMALE', '24 Ngõ Trung Yên, Quận Cầu Giấy, Hà Nội', '0945556677', '001199003344', 'thu.hoang.99@student.edu.vn', 'Việt Nam', 'Sinh viên'),
(6, 'Vũ Đức Bình', '1965-12-30', 'MALE', '56 Đường Lê Lợi, Quận Hải Châu, Đà Nẵng', '0956667788', '049065005566', 'binhvu.engineer@factory.com', 'Việt Nam', 'Kỹ sư cơ khí'),
(7, 'Đặng Lan Anh', '2012-09-15', 'FEMALE', '33 Đường 3/2, Quận 10, TP. Hồ Chí Minh', '0967778899', '079212007788', NULL, 'Việt Nam', 'Học sinh Tiểu học'),
(8, 'Bùi Thế Vinh', '1980-06-05', 'MALE', '88 Phố Bà Triệu, Quận Hoàn Kiếm, Hà Nội', '0978889900', '001080009900', 'vinh.bui.arch@design.vn', 'Việt Nam', 'Kiến trúc sư'),
(9, 'Đỗ Thùy Linh', '1992-01-20', 'FEMALE', '19 Đường Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh', '0989990011', '079192001212', 'linhdo.mkt@agency.com', 'Việt Nam', 'Nhân viên Marketing'),
(10, 'Ngô Gia Huy', '2003-03-10', 'MALE', '67 Đường CMT8, Quận 3, TP. Hồ Chí Minh', '0990001122', '079203003434', 'huyngo.2k3@university.edu.vn', 'Việt Nam', 'Sinh viên'),
(11, 'Phan Thị Kim Chi', '1958-10-08', 'FEMALE', '42 Đường Nguyễn Văn Linh, Quận 7, TP. Hồ Chí Minh', '0311112233', '079058005656', 'kimchi.phan58@email.net', 'Việt Nam', 'Nội trợ'),
(12, 'Lý Thành Long', '1991-11-19', 'MALE', '99 Đường Giải Phóng, Quận Hoàng Mai, Hà Nội', '0322223344', '001091007878', 'longly.music@studio.com', 'Việt Nam', 'Nhạc sĩ'),
(13, 'Châu Mỹ Duyên', '1985-07-29', 'FEMALE', '11 Đường Tôn Đức Thắng, Quận Đống Đa, Hà Nội', '0333334455', '001185009090', 'duyen.chau.gv@school.edu.vn', 'Việt Nam', 'Giáo viên'),
(14, 'Dương Minh Khang', '2019-05-02', 'MALE', '222 Đường Quang Trung, Quận Gò Vấp, TP. Hồ Chí Minh', '0344445566', NULL, NULL, 'Việt Nam', 'Trẻ em'),
(15, 'Huỳnh Bảo Trân', '1998-08-22', 'FEMALE', '31 Đường Bạch Đằng, Quận Tân Bình, TP. Hồ Chí Minh', '0355556677', '079198002468', 'tranhuynh.designer@art.com', 'Việt Nam', 'Nhân viên thiết kế'),
(16, 'Mai Tiến Dũng', '1972-09-14', 'MALE', '75 Phố Xã Đàn, Quận Đống Đa, Hà Nội', '0366667788', '001072001357', 'dungmai.driver@transport.vn', 'Việt Nam', 'Tài xế'),
(17, 'Tô Ngọc Hà', '1990-12-01', 'FEMALE', '14 Ngách 50, Ngõ Lương Sử C, Hà Nội', '0377778899', '001190008642', 'ha.to.hr@corporate.com', 'Việt Nam', 'Chuyên viên nhân sự'),
(18, 'Lâm Gia Bảo', '2008-06-26', 'MALE', '50 Đường Võ Thị Sáu, Quận 3, TP. Hồ Chí Minh', '0388889900', '079208009753', 'baolam.2k8@gmail.com', 'Việt Nam', 'Học sinh Trung học'),
(19, 'Trịnh Thị Thúy', '1960-03-03', 'FEMALE', '18 Đường Hoàng Diệu, Quận Ba Đình, Hà Nội', '0399990011', '001060001010', 'thuytrinh.retired@email.com', 'Việt Nam', 'Nghỉ hưu'),
(20, 'Đoàn Quốc Trung', '1983-11-11', 'MALE', '91 Đường Nguyễn Chí Thanh, Quận 5, TP. Hồ Chí Minh', '0701112233', '079083002020', 'trungdoan.law@firm.vn', 'Việt Nam', 'Luật sư');

INSERT INTO tbl_medical_record (customer_id, patient_id, barcode) VALUES
(1001, 1, 'MR000001XYZ'),  -- Bệnh nhân 1 được đăng ký bởi Khách hàng 1001
(1002, 2, 'MR000002ABC'),  -- Bệnh nhân 2 được đăng ký bởi Khách hàng 1002
(1003, 3, 'MR000003DEF'),  -- Bệnh nhân 3 được đăng ký bởi Khách hàng 1003
(1004, 4, 'MR000004GHI'),  -- Bệnh nhân 4 được đăng ký bởi Khách hàng 1004
(1005, 5, 'MR000005JKL'),  -- Bệnh nhân 5 được đăng ký bởi Khách hàng 1005
(1006, 6, 'MR000006MNO'),  -- Bệnh nhân 6 được đăng ký bởi Khách hàng 1006
(1007, 7, 'MR000007PQR'),  -- Bệnh nhân 7 được đăng ký bởi Khách hàng 1007
(1008, 8, 'MR000008STU'),  -- Bệnh nhân 8 được đăng ký bởi Khách hàng 1008
(1009, 9, 'MR000009VWX'),  -- Bệnh nhân 9 được đăng ký bởi Khách hàng 1009
(1010, 10, 'MR000010YZA'), -- Bệnh nhân 10 được đăng ký bởi Khách hàng 1010
(1011, 11, 'MR000011BCD'), -- Bệnh nhân 11 được đăng ký bởi Khách hàng 1011
(1012, 12, 'MR000012EFG'), -- Bệnh nhân 12 được đăng ký bởi Khách hàng 1012
(1013, 13, 'MR000013HIJ'), -- Bệnh nhân 13 được đăng ký bởi Khách hàng 1013
(1014, 14, 'MR000014KLM'), -- Bệnh nhân 14 được đăng ký bởi Khách hàng 1014
(1015, 15, 'MR000015NOP'), -- Bệnh nhân 15 được đăng ký bởi Khách hàng 1015
(1016, 16, 'MR000016QRS'), -- Bệnh nhân 16 được đăng ký bởi Khách hàng 1016
(1017, 17, 'MR000017TUV'), -- Bệnh nhân 17 được đăng ký bởi Khách hàng 1017
(1018, 18, 'MR000018WXY'), -- Bệnh nhân 18 được đăng ký bởi Khách hàng 1018
(1019, 19, 'MR000019ZAB'), -- Bệnh nhân 19 được đăng ký bởi Khách hàng 1019
(1020, 20, 'MR000020CDE'); -- Bệnh nhân 20 được đăng ký bởi Khách hàng 1020

INSERT INTO tbl_user (id, date_of_birth, address, email, full_name, nation, phone, gender) VALUES
(2251, '2004-01-01', 'Đăng Văn Bi', 'bldq@gmail.com', 'Bùi Lê Đông Quân', 'Việt Nam', '0997788665', 'MALE'),
(2252, '2004-01-01', 'Vũng Tàu', 'ntt@gmail.com', 'Nguyễn Thành Đạt', 'Việt Nam', '0997782661', 'MALE'),
(2253, '2004-01-01', 'Hàn Quốc', 'cmv@gmail.com', 'Choi Minh Văn', 'Việt Nam', '0997742665', 'MALE'),
(2254, '2004-09-25', 'Biên Hòa', 'chien@gmail.com', 'Phạm Công Chiến', 'Việt Nam', '0979859559', 'MALE');

INSERT INTO tbl_account (user_id, password, role, status) VALUES
(2251, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'STAFF', 'ACTIVE'),
(2252, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'STAFF', 'ACTIVE'),
(2253, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'STAFF', 'ACTIVE'),
(2254, '$2a$10$5zVt.fbYLgqdw9Rn3.coX.xETazDmzblSKgPJtG71yUCHYWpnDoqW', 'STAFF', 'ACTIVE');

INSERT INTO tbl_staff (staff_role, id) VALUES (2, 2251),
                                              (2, 2252),
                                              (2, 2253),
                                              (2, 2254);

INSERT INTO tbl_post (id, date_of_create, staff_id, content, header)
VALUES
(1, DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), 2252, 'Exploring the power of AI in daily work.', 'AI and Work'),
(2, DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), 2252, 'Team collaboration has improved significantly.', 'Better Collaboration'),
(3, DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), 2252, 'Successfully deployed the new microservice architecture.', 'Microservices Success'),
(4, DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), 2252, 'Training sessions on new tools are helpful.', 'Training Recap'),
(5, DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), 2252, 'Looking forward to the upcoming project launch.', 'Project Launch Insights');

INSERT INTO tbl_post_image (post_id, image_url) VALUES
(4, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/eq7mhtyswgnswnuxyc33.jpg'),
(4, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/gtvlwti1idpdbgilfobn.jpg'),
(1, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/eq7mhtyswgnswnuxyc33.jpg'),
(2, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/gtvlwti1idpdbgilfobn.jpg'),
(3, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/eq7mhtyswgnswnuxyc33.jpg'),
(4, 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400726/muscle/gtvlwti1idpdbgilfobn.jpg');


INSERT INTO tbl_encounter (visit_date, medical_record_id, diagnosis, notes, treatment)
VALUES
('2025-04-20', 1, 'Cảm cúm', 'Hắt hơi, sổ mũi', 'Uống thuốc cảm 5 ngày'),
('2025-04-21', 1, 'Viêm amidan', 'Sưng đỏ họng', 'Kháng sinh 7 ngày'),
('2025-04-22', 2, 'Đau bụng', 'Đau nhẹ quanh rốn', 'Ăn uống dễ tiêu'),
('2025-04-23', 2, 'Tiêu chảy', 'Đi ngoài phân lỏng', 'Bù nước và oresol'),
('2025-04-24', 3, 'Sốt virus', 'Sốt cao 39 độ', 'Hạ sốt và theo dõi tại nhà'),
('2025-04-25', 3, 'Đau lưng', 'Đau vùng thắt lưng', 'Nghỉ ngơi, vật lý trị liệu'),
('2025-04-26', 4, 'Viêm phế quản', 'Ho có đờm', 'Kháng sinh và thuốc long đờm'),
('2025-04-27', 4, 'Chấn thương mềm', 'Sưng nề nhẹ', 'Chườm lạnh, giảm đau'),
('2025-04-28', 5, 'Đau dạ dày', 'Đau thượng vị', 'Uống thuốc dạ dày, ăn uống hợp lý'),
('2025-04-29', 5, 'Mệt mỏi', 'Chán ăn, mất ngủ', 'Bổ sung vitamin và nghỉ ngơi');


INSERT INTO tbl_prescription (encounter_id, status, issue_date) VALUES
(1,'RECEIVED', '2025-04-20 10:00:00'),
(2,'RECEIVED', '2025-04-21 14:45:00'),
(3,'RECEIVED', '2025-04-22 09:30:00'),
(4,'RECEIVED', '2025-04-23 11:15:00'),
(5,'CANCELLED', '2025-04-24 16:30:00'),
(6,'RECEIVED', '2025-04-25 08:00:00'),
(7,'RECEIVED', '2025-04-26 13:00:00'),
(8,'CANCELLED', '2025-04-27 17:45:00'),
(9,'RECEIVED', '2025-04-28 10:30:00'),
(10,'PENDING', '2025-04-29 15:15:00'),
(1,'CANCELLED', '2025-04-30 09:00:00'),
(2,'PENDING', '2025-05-01 14:00:00');

INSERT INTO tbl_medicine (name, price, medicine_usage, strength) VALUES
('Paracetamol 500mg', 15000.00, 'Giảm đau, hạ sốt. Uống sau ăn 6 giờ nếu cần.', '500mg'),
('Amoxicillin 500mg', 28000.00, 'Kháng sinh điều trị nhiễm khuẩn đường hô hấp, uống 2 lần/ngày.', '500mg'),
('Ibuprofen 400mg', 22000.00, 'Giảm đau, kháng viêm, điều trị viêm khớp. Uống mỗi 8 giờ.', '400mg'),
('Vitamin C 1000mg', 18000.00, 'Tăng cường miễn dịch, chống oxy hóa. Uống 1 viên/ngày sau ăn.', '1000mg'),
('Loperamide 2mg', 12000.00, 'Điều trị tiêu chảy cấp. Uống 1 viên sau mỗi lần đi tiêu lỏng.', '2mg'),
('Cetirizine 10mg', 16000.00, 'Giảm dị ứng, nổi mề đay. Uống 1 viên/ngày vào buổi tối.', '10mg'),
('Azithromycin 250mg', 35000.00, 'Kháng sinh điều trị nhiễm khuẩn. Uống 1 viên/ngày trong 3 ngày.', '250mg'),
('Omeprazole 20mg', 19000.00, 'Giảm tiết axit dạ dày. Uống trước bữa sáng.', '20mg'),
('Metformin 500mg', 25000.00, 'Điều trị tiểu đường tuýp 2. Uống 2 lần/ngày sau ăn.', '500mg'),
('Diazepam 5mg', 21000.00, 'An thần, hỗ trợ giấc ngủ. Uống 1 viên trước khi ngủ.', '5mg'),
('Ciprofloxacin 500mg', 38000.00, 'Kháng sinh điều trị nhiễm khuẩn đường tiết niệu. Uống 2 lần/ngày.', '500mg'),
('Amlodipine 5mg', 25000.00, 'Điều trị tăng huyết áp. Uống 1 viên/ngày.', '5mg'),
('Atorvastatin 20mg', 30000.00, 'Giảm cholesterol trong máu. Uống 1 viên vào buổi tối.', '20mg'),
('Pantoprazole 40mg', 27000.00, 'Giảm tiết axit dạ dày, điều trị viêm loét dạ dày. Uống trước bữa ăn sáng.', '40mg'),
('Levothyroxine 50mcg', 22000.00, 'Điều trị suy giáp. Uống vào buổi sáng khi bụng đói.', '50mcg'),
('Salbutamol 100mcg', 15000.00, 'Giãn phế quản, giảm khó thở trong hen suyễn. Xịt khi cần.', '100mcg'),
('Fluticasone 50mcg', 20000.00, 'Kháng viêm tại chỗ, điều trị viêm mũi dị ứng. Xịt mỗi bên mũi.', '50mcg'),
('Montelukast 10mg', 28000.00, 'Dự phòng và điều trị hen suyễn. Uống 1 viên vào buổi tối.', '10mg'),
('Losartan 50mg', 26000.00, 'Điều trị tăng huyết áp. Uống 1 viên/ngày.', '50mg'),
('Bisoprolol 5mg', 24000.00, 'Điều trị tăng huyết áp, đau thắt ngực. Uống 1 viên/ngày.', '5mg'),
('Furosemide 40mg', 18000.00, 'Thuốc lợi tiểu, điều trị phù. Uống theo chỉ định của bác sĩ.', '40mg'),
('Warfarin 2mg', 35000.00, 'Thuốc chống đông máu. Uống theo chỉ định của bác sĩ.', '2mg');


INSERT INTO tbl_prescription_item (quantity, medicine_id, prescription_id, dosage, unit) VALUES
(10, 1, 3, '1 vien/lan x 3 lan/ngay', 'vien'),
(5, 2, 3, '1 vien/lan x 2 lan/ngay', 'vien'),
(15, 3, 4, '1 vien/lan x 3 lan/ngay', 'vien'),
(7, 4, 4, '1 vien/lan x 1 lan/ngay', 'vien'),
(8, 5, 5, '1 vien sau moi lan tieu chay', 'vien'),
(6, 6, 5, '1 vien truoc khi ngu', 'vien'),
(12, 7, 6, '1 vien/ngay trong 3 ngay', 'vien'),
(14, 8, 6, '1 vien truoc bua sang', 'vien'),
(10, 9, 7, '1 vien/lan x 2 lan/ngay', 'vien'),
(4, 10, 7, '1 vien truoc khi ngu', 'vien'),
(14, 11, 8, '1 vien/lan x 2 lan/ngay', 'vien'),
(30, 12, 9, '1 vien/lan x 1 lan/ngay', 'vien'),
(30, 13, 9, '1 vien/lan x 1 lan/ngay', 'vien'),
(28, 14, 10, '1 vien truoc bua an sang', 'vien'),
(60, 15, 11, '1 vien vao buoi sang khi bung doi', 'vien'),
(2, 16, 12, 'Xit khi can', 'lan xit'),
(1, 17, 3, 'Xit moi ben mui', 'lan xit'),
(30, 18, 4, '1 vien vao buoi toi', 'vien'),
(30, 19, 5, '1 vien/lan x 1 lan/ngay', 'vien'),
(30, 20, 6, '1 vien/lan x 1 lan/ngay', 'vien'),
(15, 21, 7, 'Uong theo chi dinh cua bac si',  'vien'),
(20, 22, 8, 'Uong theo chi dinh cua bac si', 'vien'),
(10, 1, 9, '1 vien/lan x 3 lan/ngay', 'vien'),
(7, 2, 10, '1 vien/lan x 2 lan/ngay', 'vien'),
(12, 9, 11, '1 vien/lan x 2 lan/ngay', 'vien'),
(5, 12, 12, '1 vien/lan x 1 lan/ngay', 'vien'),
(8, 14, 3, '1 vien truoc bua an sang',  'vien'),
(4, 18, 4, '1 vien vao buoi toi',  'vien');


INSERT INTO tbl_schedule (id, date, max_slots, room_detail_id, created_at, doctor_id, updated_at) VALUES
(1, '2025-05-15', 5, 202, '2025-01-30 10:00:00', 2001, '2025-01-30 10:00:00'), -- Bác sĩ Tổng Quát (Phòng AG02)
(2, '2025-05-15', 5, 203, '2025-01-30 10:00:00', 2011, '2025-01-30 10:00:00'), -- Bác sĩ Da Liễu (Phòng AG03)
(3, '2025-05-15', 5, 206, '2025-01-30 10:00:00', 2013, '2025-01-30 10:00:00'), -- Bác sĩ TMH (Phòng A101)
(4, '2025-05-15', 5, 219, '2025-01-30 10:00:00', 2021, '2025-01-30 10:00:00'), -- Bác sĩ Tim Mạch (Phòng B204)
(5, '2025-05-15', 5, 225, '2025-01-30 10:00:00', 2023, '2025-01-30 10:00:00'), -- Bác sĩ Tiêu Hóa (Phòng B210)
(6, '2025-05-15', 5, 236, '2025-01-30 10:00:00', 2031, '2025-01-30 10:00:00'), -- Bác sĩ Ngoại (Phòng C301)
(7, '2025-05-15', 5, 254, '2025-01-30 10:00:00', 2041, '2025-01-30 10:00:00'), -- Bác sĩ Sản Phụ Khoa (Phòng D401)
(8, '2025-05-15', 5, 259, '2025-01-30 10:00:00', 2051, '2025-01-30 10:00:00'), -- Bác sĩ Nhi Khoa (Phòng D406)
(9, '2025-05-15', 5, 264, '2025-01-30 10:00:00', 2061, '2025-01-30 10:00:00'), -- Bác sĩ Răng Hàm Mặt (Phòng D411)
(10, '2025-05-15', 5, 246, '2025-01-30 10:00:00', 2071, '2025-01-30 10:00:00'), -- Bác sĩ Thần Kinh (Phòng C311)

-- Ngày 16/05/2025
(11, '2025-05-16', 5, 222, '2025-01-30 10:00:00', 2081, '2025-01-30 10:00:00'), -- Bác sĩ Hô Hấp (Phòng B207 - Khám Hô Hấp 1)
(12, '2025-05-16', 5, 243, '2025-01-30 10:00:00', 2091, '2025-01-30 10:00:00'), -- Bác sĩ Tiết Niệu (Phòng C308 - Khám Tiết Niệu 1)
(13, '2025-05-16', 5, 213, '2025-01-30 10:00:00', 2072, '2025-01-30 10:00:00'), -- Bác sĩ Tâm Thần (Phòng A108 - Tư vấn Tâm Thần 1)
(14, '2025-05-16', 5, 210, '2025-01-30 10:00:00', 2014, '2025-01-30 10:00:00'), -- Bác sĩ Mắt (Phòng A105 - Khám Mắt 1)
(15, '2025-05-16', 5, 229, '2025-01-30 10:00:00', 2024, '2025-01-30 10:00:00'), -- Bác sĩ Nội Tiết (Phòng B214 - Khám Nội Tiết 1)
(16, '2025-05-16', 5, 240, '2025-01-30 10:00:00', 2062, '2025-01-30 10:00:00'), -- Bác sĩ CTCH (Phòng C305 - Khám Chấn Thương Chỉnh Hình 1)
(17, '2025-05-16', 5, 255, '2025-01-30 10:00:00', 2032, '2025-01-30 10:00:00'), -- Bác sĩ Sản Phụ Khoa (Phòng D402 - Khám Sản Phụ Khoa 2)
-- Ngày 17/05/2025
(18, '2025-05-17', 5, 223, '2025-01-30 10:00:00', 2082, '2025-01-30 10:00:00'), -- Bác sĩ Hô Hấp (Phòng B208 - Khám Hô Hấp 2)
(19, '2025-05-17', 5, 244, '2025-01-30 10:00:00', 2092, '2025-01-30 10:00:00'), -- Bác sĩ Tiết Niệu (Phòng C309 - Khám Tiết Niệu 2)
(20, '2025-05-17', 5, 214, '2025-01-30 10:00:00', 2073, '2025-01-30 10:00:00'), -- Bác sĩ Tâm Thần (Phòng A109 - Tư vấn Tâm Thần 2)
(21, '2025-05-17', 5, 211, '2025-01-30 10:00:00', 2015, '2025-01-30 10:00:00'), -- Bác sĩ Mắt (Phòng A106 - Khám Mắt 2)
(22, '2025-05-17', 5, 230, '2025-01-30 10:00:00', 2025, '2025-01-30 10:00:00'), -- Bác sĩ Nội Tiết (Phòng B215 - Khám Nội Tiết 2)
(23, '2025-05-17', 5, 241, '2025-01-30 10:00:00', 2063, '2025-01-30 10:00:00'), -- Bác sĩ CTCH (Phòng C306 - Khám Chấn Thương Chỉnh Hình 2)
(24, '2025-05-17', 5, 260, '2025-01-30 10:00:00', 2042, '2025-01-30 10:00:00'), -- Bác sĩ Nhi Khoa (Phòng D407 - Khám Nhi Khoa 2)
-- Ngày 18/05/2025
(25, '2025-05-18', 5, 224, '2025-01-30 10:00:00', 2083, '2025-01-30 10:00:00'), -- Bác sĩ Hô Hấp (Phòng B209 - Khám Hô Hấp 3)
(26, '2025-05-18', 5, 245, '2025-01-30 10:00:00', 2093, '2025-01-30 10:00:00'), -- Bác sĩ Tiết Niệu (Phòng C310 - Khám Tiết Niệu 3)
(27, '2025-05-18', 5, 249, '2025-01-30 10:00:00', 2074, '2025-01-30 10:00:00'), -- Bác sĩ Ung Bướu (Phòng C314 - Khám Ung Bướu 1)
(28, '2025-05-18', 5, 265, '2025-01-30 10:00:00', 2052, '2025-01-30 10:00:00'), -- Bác sĩ Răng Hàm Mặt (Phòng D412 - Khám Răng Hàm Mặt 2)
(29, '2025-05-18', 5, 237, '2025-01-30 10:00:00', 2022, '2025-01-30 10:00:00'), -- Bác sĩ Ngoại (Phòng C302 - Khám Ngoại Tổng Quát 2)
(30, '2025-05-18', 5, 261, '2025-01-30 10:00:00', 2043, '2025-01-30 10:00:00'), -- Bác sĩ Nhi Khoa (Phòng D408 - Khám Nhi Khoa 3)

(31, '2025-05-16', 5, 216, '2025-01-30 10:00:00', 2002, '2025-01-30 10:00:00'), -- Bác sĩ Tổng Quát (Phòng B201 - Khám Nội Khoa 1)
(32, '2025-05-16', 5, 204, '2025-01-30 10:00:00', 2012, '2025-01-30 10:00:00'), -- Bác sĩ Da Liễu (Phòng AG04 - Khám Da Liễu 2)
(33, '2025-05-16', 5, 207, '2025-01-30 10:00:00', 2016, '2025-01-30 10:00:00'), -- Bác sĩ TMH (Phòng A102 - Khám Tai Mũi Họng 2)
(34, '2025-05-16', 5, 220, '2025-01-30 10:00:00', 2022, '2025-01-30 10:00:00'), -- Bác sĩ Tim Mạch (Phòng B205 - Khám Tim Mạch 2)
(35, '2025-05-16', 5, 226, '2025-01-30 10:00:00', 2026, '2025-01-30 10:00:00'), -- Bác sĩ Tiêu Hóa (Phòng B211 - Khám Tiêu Hóa 2)
(36, '2025-05-16', 5, 237, '2025-01-30 10:00:00', 2032, '2025-01-30 10:00:00'), -- Bác sĩ Ngoại (Phòng C302 - Khám Ngoại Tổng Quát 2)
(37, '2025-05-16', 5, 255, '2025-01-30 10:00:00', 2036, '2025-01-30 10:00:00'), -- Bác sĩ Sản Phụ Khoa (Phòng D402 - Khám Sản Phụ Khoa 2)
-- Ngày 17/05/2025
(38, '2025-05-17', 5, 222, '2025-01-30 10:00:00', 2082, '2025-01-30 10:00:00'), -- Bác sĩ Hô Hấp (Phòng B207 - Khám Hô Hấp 1)
(39, '2025-05-17', 5, 243, '2025-01-30 10:00:00', 2092, '2025-01-30 10:00:00'), -- Bác sĩ Tiết Niệu (Phòng C308 - Khám Tiết Niệu 1)
(40, '2025-05-17', 5, 213, '2025-01-30 10:00:00', 2072, '2025-01-30 10:00:00'), -- Bác sĩ Tâm Thần (Phòng A108 - Tư vấn Tâm Thần 1)
(41, '2025-05-17', 5, 210, '2025-01-30 10:00:00', 2018, '2025-01-30 10:00:00'), -- Bác sĩ Mắt (Phòng A105 - Khám Mắt 1)
(42, '2025-05-17', 5, 229, '2025-01-30 10:00:00', 2028, '2025-01-30 10:00:00'), -- Bác sĩ Nội Tiết (Phòng B214 - Khám Nội Tiết 1)
(43, '2025-05-17', 5, 240, '2025-01-30 10:00:00', 2062, '2025-01-30 10:00:00'), -- Bác sĩ CTCH (Phòng C305 - Khám Chấn Thương Chỉnh Hình 1)
(44, '2025-05-17', 5, 260, '2025-01-30 10:00:00', 2046, '2025-01-30 10:00:00'), -- Bác sĩ Nhi Khoa (Phòng D407 - Khám Nhi Khoa 2)
-- Ngày 18/05/2025
(45, '2025-05-18', 5, 223, '2025-01-30 10:00:00', 2084, '2025-01-30 10:00:00'), -- Bác sĩ Hô Hấp (Phòng B208 - Khám Hô Hấp 2)
(46, '2025-05-18', 5, 244, '2025-01-30 10:00:00', 2094, '2025-01-30 10:00:00'), -- Bác sĩ Tiết Niệu (Phòng C309 - Khám Tiết Niệu 2)
(47, '2025-05-18', 5, 249, '2025-01-30 10:00:00', 2076, '2025-01-30 10:00:00'), -- Bác sĩ Ung Bướu (Phòng C314 - Khám Ung Bướu 1)
(48, '2025-05-18', 5, 265, '2025-01-30 10:00:00', 2052, '2025-01-30 10:00:00'), -- Bác sĩ Răng Hàm Mặt (Phòng D412 - Khám Răng Hàm Mặt 2)
(49, '2025-05-18', 5, 236, '2025-01-30 10:00:00', 2038, '2025-01-30 10:00:00'), -- Bác sĩ Ngoại (Phòng C301 - Khám Ngoại Tổng Quát 1)
(50, '2025-05-18', 5, 261, '2025-01-30 10:00:00', 2048, '2025-01-30 10:00:00'); -- Bác sĩ Nhi Khoa (Phòng D408 - Khám Nhi Khoa 3)


-- Thêm dữ liệu vào tbl_schedule_slot (8 khung giờ cho mỗi lịch)
INSERT INTO tbl_schedule_slot (id, booked_slots, time_slot_id, created_at, schedule_id, updated_at) VALUES
-- Lịch 1 (Bác sĩ Tổng Quát - ID 2001)
(1, 0, 1, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(2, 0, 2, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(3, 0, 3, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(4, 0, 4, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(5, 0, 5, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(6, 0, 6, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(7, 0, 7, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
(8, 0, 8, '2025-01-30 10:00:00', 1, '2025-01-30 10:00:00'),
-- Lịch 2 (Bác sĩ Da Liễu - ID 2011)
(9, 0, 1, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(10, 0, 2, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(11, 0, 3, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(12, 0, 4, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(13, 0, 5, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(14, 0, 6, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(15, 0, 7, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
(16, 0, 8, '2025-01-30 10:00:00', 2, '2025-01-30 10:00:00'),
-- Lịch 3 (Bác sĩ TMH - ID 2013)
(17, 0, 1, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(18, 0, 2, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(19, 0, 3, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(20, 0, 4, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(21, 0, 5, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(22, 0, 6, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(23, 0, 7, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
(24, 0, 8, '2025-01-30 10:00:00', 3, '2025-01-30 10:00:00'),
-- Lịch 4 (Bác sĩ Tim Mạch - ID 2021)
(25, 0, 1, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(26, 0, 2, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(27, 0, 3, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(28, 0, 4, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(29, 0, 5, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(30, 0, 6, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(31, 0, 7, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
(32, 0, 8, '2025-01-30 10:00:00', 4, '2025-01-30 10:00:00'),
-- Lịch 5 (Bác sĩ Tiêu Hóa - ID 2023)
(33, 0, 1, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(34, 0, 2, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(35, 0, 3, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(36, 0, 4, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(37, 0, 5, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(38, 0, 6, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(39, 0, 7, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
(40, 0, 8, '2025-01-30 10:00:00', 5, '2025-01-30 10:00:00'),
-- Lịch 6 (Bác sĩ Ngoại - ID 2031)
(41, 0, 1, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(42, 0, 2, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(43, 0, 3, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(44, 0, 4, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(45, 0, 5, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(46, 0, 6, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(47, 0, 7, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
(48, 0, 8, '2025-01-30 10:00:00', 6, '2025-01-30 10:00:00'),
-- Lịch 7 (Bác sĩ Sản Phụ Khoa - ID 2041)
(49, 0, 1, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(50, 0, 2, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(51, 0, 3, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(52, 0, 4, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(53, 0, 5, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(54, 0, 6, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(55, 0, 7, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
(56, 0, 8, '2025-01-30 10:00:00', 7, '2025-01-30 10:00:00'),
-- Lịch 8 (Bác sĩ Nhi Khoa - ID 2051)
(57, 0, 1, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(58, 0, 2, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(59, 0, 3, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(60, 0, 4, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(61, 0, 5, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(62, 0, 6, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(63, 0, 7, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
(64, 0, 8, '2025-01-30 10:00:00', 8, '2025-01-30 10:00:00'),
-- Lịch 9 (Bác sĩ Răng Hàm Mặt - ID 2061)
(65, 0, 1, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(66, 0, 2, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(67, 0, 3, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(68, 0, 4, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(69, 0, 5, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(70, 0, 6, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(71, 0, 7, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
(72, 0, 8, '2025-01-30 10:00:00', 9, '2025-01-30 10:00:00'),
-- Lịch 10 (Bác sĩ Thần Kinh - ID 2071)
(73, 0, 1, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(74, 0, 2, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(75, 0, 3, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(76, 0, 4, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(77, 0, 5, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(78, 0, 6, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(79, 0, 7, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
(80, 0, 8, '2025-01-30 10:00:00', 10, '2025-01-30 10:00:00'),
-- Lịch 11 (Bác sĩ Hô Hấp - ID 2081)
(81, 0, 1, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(82, 0, 2, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(83, 0, 3, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(84, 0, 4, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(85, 0, 5, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(86, 0, 6, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(87, 0, 7, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
(88, 0, 8, '2025-01-30 10:00:00', 11, '2025-01-30 10:00:00'),
-- Lịch 12 (Bác sĩ Tiết Niệu - ID 2091)
(89, 0, 1, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(90, 0, 2, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(91, 0, 3, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(92, 0, 4, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(93, 0, 5, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(94, 0, 6, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(95, 0, 7, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
(96, 0, 8, '2025-01-30 10:00:00', 12, '2025-01-30 10:00:00'),
-- Lịch 13 (Bác sĩ Tâm Thần - ID 2072)
(97, 0, 1, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(98, 0, 2, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(99, 0, 3, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(100, 0, 4, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(101, 0, 5, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(102, 0, 6, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(103, 0, 7, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
(104, 0, 8, '2025-01-30 10:00:00', 13, '2025-01-30 10:00:00'),
-- Lịch 14 (Bác sĩ Mắt - ID 2014)
(105, 0, 1, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(106, 0, 2, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(107, 0, 3, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(108, 0, 4, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(109, 0, 5, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(110, 0, 6, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(111, 0, 7, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
(112, 0, 8, '2025-01-30 10:00:00', 14, '2025-01-30 10:00:00'),
-- Lịch 15 (Bác sĩ Nội Tiết - ID 2024)
(113, 0, 1, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(114, 0, 2, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(115, 0, 3, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(116, 0, 4, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(117, 0, 5, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(118, 0, 6, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(119, 0, 7, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
(120, 0, 8, '2025-01-30 10:00:00', 15, '2025-01-30 10:00:00'),
-- Lịch 16 (Bác sĩ CTCH - ID 2062)
(121, 0, 1, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(122, 0, 2, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(123, 0, 3, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(124, 0, 4, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(125, 0, 5, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(126, 0, 6, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(127, 0, 7, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
(128, 0, 8, '2025-01-30 10:00:00', 16, '2025-01-30 10:00:00'),
-- Lịch 17 (Bác sĩ Sản Phụ Khoa - ID 2032)
(129, 0, 1, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(130, 0, 2, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(131, 0, 3, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(132, 0, 4, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(133, 0, 5, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(134, 0, 6, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(135, 0, 7, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
(136, 0, 8, '2025-01-30 10:00:00', 17, '2025-01-30 10:00:00'),
-- Lịch 18 (Bác sĩ Hô Hấp - ID 2082)
(137, 0, 1, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(138, 0, 2, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(139, 0, 3, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(140, 0, 4, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(141, 0, 5, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(142, 0, 6, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(143, 0, 7, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
(144, 0, 8, '2025-01-30 10:00:00', 18, '2025-01-30 10:00:00'),
-- Lịch 19 (Bác sĩ Tiết Niệu - ID 2092)
(145, 0, 1, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(146, 0, 2, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(147, 0, 3, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(148, 0, 4, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(149, 0, 5, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(150, 0, 6, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(151, 0, 7, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
(152, 0, 8, '2025-01-30 10:00:00', 19, '2025-01-30 10:00:00'),
-- Lịch 20 (Bác sĩ Tâm Thần - ID 2073)
(153, 0, 1, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(154, 0, 2, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(155, 0, 3, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(156, 0, 4, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(157, 0, 5, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(158, 0, 6, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(159, 0, 7, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
(160, 0, 8, '2025-01-30 10:00:00', 20, '2025-01-30 10:00:00'),
-- Lịch 21 (Bác sĩ Mắt - ID 2015)
(161, 0, 1, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(162, 0, 2, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(163, 0, 3, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(164, 0, 4, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(165, 0, 5, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(166, 0, 6, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(167, 0, 7, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
(168, 0, 8, '2025-01-30 10:00:00', 21, '2025-01-30 10:00:00'),
-- Lịch 22 (Bác sĩ Nội Tiết - ID 2025)
(169, 0, 1, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(170, 0, 2, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(171, 0, 3, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(172, 0, 4, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(173, 0, 5, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(174, 0, 6, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(175, 0, 7, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
(176, 0, 8, '2025-01-30 10:00:00', 22, '2025-01-30 10:00:00'),
-- Lịch 23 (Bác sĩ CTCH - ID 2063)
(177, 0, 1, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(178, 0, 2, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(179, 0, 3, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(180, 0, 4, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(181, 0, 5, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(182, 0, 6, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(183, 0, 7, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
(184, 0, 8, '2025-01-30 10:00:00', 23, '2025-01-30 10:00:00'),
-- Lịch 24 (Bác sĩ Nhi Khoa - ID 2042)
(185, 0, 1, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(186, 0, 2, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(187, 0, 3, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(188, 0, 4, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(189, 0, 5, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(190, 0, 6, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(191, 0, 7, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
(192, 0, 8, '2025-01-30 10:00:00', 24, '2025-01-30 10:00:00'),
-- Lịch 25 (Bác sĩ Hô Hấp - ID 2083)
(193, 0, 1, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(194, 0, 2, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(195, 0, 3, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(196, 0, 4, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(197, 0, 5, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(198, 0, 6, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(199, 0, 7, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
(200, 0, 8, '2025-01-30 10:00:00', 25, '2025-01-30 10:00:00'),
-- Lịch 26 (Bác sĩ Tiết Niệu - ID 2093)
(201, 0, 1, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(202, 0, 2, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(203, 0, 3, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(204, 0, 4, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(205, 0, 5, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(206, 0, 6, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(207, 0, 7, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
(208, 0, 8, '2025-01-30 10:00:00', 26, '2025-01-30 10:00:00'),
-- Lịch 27 (Bác sĩ Ung Bướu - ID 2074)
(209, 0, 1, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(210, 0, 2, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(211, 0, 3, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(212, 0, 4, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(213, 0, 5, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(214, 0, 6, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(215, 0, 7, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
(216, 0, 8, '2025-01-30 10:00:00', 27, '2025-01-30 10:00:00'),
-- Lịch 28 (Bác sĩ Răng Hàm Mặt - ID 2052)
(217, 0, 1, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(218, 0, 2, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(219, 0, 3, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(220, 0, 4, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(221, 0, 5, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(222, 0, 6, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(223, 0, 7, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
(224, 0, 8, '2025-01-30 10:00:00', 28, '2025-01-30 10:00:00'),
-- Lịch 29 (Bác sĩ Ngoại - ID 2022)
(225, 0, 1, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(226, 0, 2, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(227, 0, 3, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(228, 0, 4, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(229, 0, 5, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(230, 0, 6, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(231, 0, 7, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
(232, 0, 8, '2025-01-30 10:00:00', 29, '2025-01-30 10:00:00'),
-- Lịch 30 (Bác sĩ Nhi Khoa - ID 2043)
(233, 0, 1, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(234, 0, 2, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(235, 0, 3, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(236, 0, 4, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(237, 0, 5, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(238, 0, 6, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(239, 0, 7, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
(240, 0, 8, '2025-01-30 10:00:00', 30, '2025-01-30 10:00:00'),
-- Lịch 31 (Bác sĩ Tổng Quát - ID 2002)
(241, 0, 1, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(242, 0, 2, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(243, 0, 3, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(244, 0, 4, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(245, 0, 5, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(246, 0, 6, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(247, 0, 7, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
(248, 0, 8, '2025-01-30 10:00:00', 31, '2025-01-30 10:00:00'),
-- Lịch 32 (Bác sĩ Da Liễu - ID 2012)
(249, 0, 1, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(250, 0, 2, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(251, 0, 3, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(252, 0, 4, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(253, 0, 5, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(254, 0, 6, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(255, 0, 7, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
(256, 0, 8, '2025-01-30 10:00:00', 32, '2025-01-30 10:00:00'),
-- Lịch 33 (Bác sĩ TMH - ID 2016)
(257, 0, 1, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(258, 0, 2, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(259, 0, 3, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(260, 0, 4, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(261, 0, 5, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(262, 0, 6, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(263, 0, 7, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
(264, 0, 8, '2025-01-30 10:00:00', 33, '2025-01-30 10:00:00'),
-- Lịch 34 (Bác sĩ Tim Mạch - ID 2022)
(265, 0, 1, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(266, 0, 2, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(267, 0, 3, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(268, 0, 4, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(269, 0, 5, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(270, 0, 6, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(271, 0, 7, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
(272, 0, 8, '2025-01-30 10:00:00', 34, '2025-01-30 10:00:00'),
-- Lịch 35 (Bác sĩ Tiêu Hóa - ID 2026)
(273, 0, 1, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(274, 0, 2, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(275, 0, 3, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(276, 0, 4, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(277, 0, 5, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(278, 0, 6, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(279, 0, 7, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
(280, 0, 8, '2025-01-30 10:00:00', 35, '2025-01-30 10:00:00'),
-- Lịch 36 (Bác sĩ Ngoại - ID 2032)
(281, 0, 1, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(282, 0, 2, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(283, 0, 3, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(284, 0, 4, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(285, 0, 5, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(286, 0, 6, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(287, 0, 7, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
(288, 0, 8, '2025-01-30 10:00:00', 36, '2025-01-30 10:00:00'),
-- Lịch 37 (Bác sĩ Sản Phụ Khoa - ID 2036)
(289, 0, 1, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(290, 0, 2, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(291, 0, 3, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(292, 0, 4, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(293, 0, 5, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(294, 0, 6, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(295, 0, 7, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
(296, 0, 8, '2025-01-30 10:00:00', 37, '2025-01-30 10:00:00'),
-- Lịch 38 (Bác sĩ Hô Hấp - ID 2082)
(297, 0, 1, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(298, 0, 2, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(299, 0, 3, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(300, 0, 4, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(301, 0, 5, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(302, 0, 6, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(303, 0, 7, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
(304, 0, 8, '2025-01-30 10:00:00', 38, '2025-01-30 10:00:00'),
-- Lịch 39 (Bác sĩ Tiết Niệu - ID 2092)
(305, 0, 1, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(306, 0, 2, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(307, 0, 3, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(308, 0, 4, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(309, 0, 5, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(310, 0, 6, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(311, 0, 7, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
(312, 0, 8, '2025-01-30 10:00:00', 39, '2025-01-30 10:00:00'),
-- Lịch 40 (Bác sĩ Tâm Thần - ID 2072)
(313, 0, 1, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(314, 0, 2, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(315, 0, 3, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(316, 0, 4, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(317, 0, 5, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(318, 0, 6, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(319, 0, 7, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
(320, 0, 8, '2025-01-30 10:00:00', 40, '2025-01-30 10:00:00'),
-- Lịch 41 (Bác sĩ Mắt - ID 2018)
(321, 0, 1, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(322, 0, 2, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(323, 0, 3, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(324, 0, 4, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(325, 0, 5, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(326, 0, 6, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(327, 0, 7, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
(328, 0, 8, '2025-01-30 10:00:00', 41, '2025-01-30 10:00:00'),
-- Lịch 42 (Bác sĩ Nội Tiết - ID 2028)
(329, 0, 1, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(330, 0, 2, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(331, 0, 3, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(332, 0, 4, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(333, 0, 5, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(334, 0, 6, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(335, 0, 7, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
(336, 0, 8, '2025-01-30 10:00:00', 42, '2025-01-30 10:00:00'),
-- Lịch 43 (Bác sĩ CTCH - ID 2062)
(337, 0, 1, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(338, 0, 2, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(339, 0, 3, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(340, 0, 4, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(341, 0, 5, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(342, 0, 6, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(343, 0, 7, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
(344, 0, 8, '2025-01-30 10:00:00', 43, '2025-01-30 10:00:00'),
-- Lịch 44 (Bác sĩ Nhi Khoa - ID 2046)
(345, 0, 1, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(346, 0, 2, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(347, 0, 3, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(348, 0, 4, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(349, 0, 5, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(350, 0, 6, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(351, 0, 7, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
(352, 0, 8, '2025-01-30 10:00:00', 44, '2025-01-30 10:00:00'),
-- Lịch 45 (Bác sĩ Hô Hấp - ID 2084)
(353, 0, 1, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(354, 0, 2, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(355, 0, 3, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(356, 0, 4, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(357, 0, 5, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(358, 0, 6, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(359, 0, 7, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
(360, 0, 8, '2025-01-30 10:00:00', 45, '2025-01-30 10:00:00'),
-- Lịch 46 (Bác sĩ Tiết Niệu - ID 2094)
(361, 0, 1, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(362, 0, 2, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(363, 0, 3, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(364, 0, 4, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(365, 0, 5, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(366, 0, 6, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(367, 0, 7, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
(368, 0, 8, '2025-01-30 10:00:00', 46, '2025-01-30 10:00:00'),
-- Lịch 47 (Bác sĩ Ung Bướu - ID 2076)
(369, 0, 1, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(370, 0, 2, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(371, 0, 3, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(372, 0, 4, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(373, 0, 5, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(374, 0, 6, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(375, 0, 7, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
(376, 0, 8, '2025-01-30 10:00:00', 47, '2025-01-30 10:00:00'),
-- Lịch 48 (Bác sĩ Răng Hàm Mặt - ID 2052)
(377, 0, 1, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(378, 0, 2, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(379, 0, 3, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(380, 0, 4, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(381, 0, 5, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(382, 0, 6, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(383, 0, 7, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
(384, 0, 8, '2025-01-30 10:00:00', 48, '2025-01-30 10:00:00'),
-- Lịch 49 (Bác sĩ Ngoại - ID 2038)
(385, 0, 1, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(386, 0, 2, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(387, 0, 3, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(388, 0, 4, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(389, 0, 5, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(390, 0, 6, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(391, 0, 7, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
(392, 0, 8, '2025-01-30 10:00:00', 49, '2025-01-30 10:00:00'),
-- Lịch 50 (Bác sĩ Nhi Khoa - ID 2048)
(393, 0, 1, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(394, 0, 2, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(395, 0, 3, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(396, 0, 4, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(397, 0, 5, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(398, 0, 6, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(399, 0, 7, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00'),
(400, 0, 8, '2025-01-30 10:00:00', 50, '2025-01-30 10:00:00');



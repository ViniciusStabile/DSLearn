INSERT INTO tb_user (name, email, password) VALUES ('Alex', 'alex@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (name, email, password) VALUES ('Maria', 'maria@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (name, email, password) VALUES ('Bob', 'Bob@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_STUDENT');
INSERT INTO tb_role (authority) VALUES ('ROLE_INSTRUCTOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 3);

INSERT INTO tb_course(name, img_Uri, img_Gray_Uri) VALUES ('java', 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 'https://cdn.iconscout.com/icon/free/png-256/free-java-file-51-775447.png');

INSERT INTO tb_offer(edition,start_Moment,end_Moment, course_id) VALUES ('1.0', TIMESTAMP WITH TIME ZONE '2020-11-20T23:00:00Z', TIMESTAMP WITH TIME ZONE '2021-11-20T23:00:00Z', '1');
INSERT INTO tb_offer(edition,start_Moment,end_Moment, course_id) VALUES ('1.0', TIMESTAMP WITH TIME ZONE '2021-11-20T23:00:00Z', TIMESTAMP WITH TIME ZONE '2022-11-20T23:00:00Z', '1');

INSERT INTO tb_resource(title, description, position, img_Url, type, offer_id) VALUES ('Trilha html', 'Trilha do curso',1,'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 1, 1);
INSERT INTO tb_resource(title, description, position, img_Url, type, offer_id) VALUES ('Trilha html 2', 'Trilha do curso 2',2,'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 1, 2);

INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 1','Neste capitulo vamos começar', 1, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,null);
INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 2','Neste capitulo vamos continuar', 2, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,1);
INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 3','Neste capitulo vamos terminar', 3, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,2);

INSERT INTO tb_enrollment(user_id, offer_id,enroll_Moment,refund_Moment,available,only_Update) VALUES (1,1,TIMESTAMP WITH TIME ZONE '2020-11-20T23:00:00Z', null, true, false);
INSERT INTO tb_enrollment(user_id, offer_id,enroll_Moment,refund_Moment,available,only_Update) VALUES (2,1,TIMESTAMP WITH TIME ZONE '2020-12-20T23:00:00Z', null, true, false);



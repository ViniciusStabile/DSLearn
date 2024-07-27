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
INSERT INTO tb_course(name, img_Uri, img_Gray_Uri) VALUES ('HTML', 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 'https://cdn.iconscout.com/icon/free/png-256/free-java-file-51-775447.png');
INSERT INTO tb_course(name, img_Uri, img_Gray_Uri) VALUES ('Java Script', 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 'https://cdn.iconscout.com/icon/free/png-256/free-java-file-51-775447.png');

INSERT INTO tb_offer(edition,start_Moment,end_Moment, course_id) VALUES ('1.0', TIMESTAMP WITH TIME ZONE '2020-11-20T23:00:00Z', TIMESTAMP WITH TIME ZONE '2021-11-20T23:00:00Z', '1');
INSERT INTO tb_offer(edition,start_Moment,end_Moment, course_id) VALUES ('1.0', TIMESTAMP WITH TIME ZONE '2021-11-20T23:00:00Z', TIMESTAMP WITH TIME ZONE '2022-11-20T23:00:00Z', '1');

INSERT INTO tb_resource(title, description, position, img_Url, type, offer_id) VALUES ('Trilha html', 'Trilha do curso',1,'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 1, 1);
INSERT INTO tb_resource(title, description, position, img_Url, type, offer_id) VALUES ('Trilha html 2', 'Trilha do curso 2',2,'https://storage.needpix.com/rsynced_images/java-2327538_1280.png', 1, 2);

INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 1','Neste capitulo vamos começar', 1, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,null);
INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 2','Neste capitulo vamos continuar', 2, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,1);
INSERT INTO tb_section(title,description, position, img_Url, resource_id, prerequisite_id) VALUES ('Capitulo 3','Neste capitulo vamos terminar', 3, 'https://storage.needpix.com/rsynced_images/java-2327538_1280.png',1,2);

INSERT INTO tb_enrollment(user_id, offer_id,enroll_Moment,refund_Moment,available,only_Update) VALUES (1,1,TIMESTAMP WITH TIME ZONE '2020-11-20T23:00:00Z', null, true, false);
INSERT INTO tb_enrollment(user_id, offer_id,enroll_Moment,refund_Moment,available,only_Update) VALUES (2,1,TIMESTAMP WITH TIME ZONE '2020-12-20T23:00:00Z', null, true, false);

INSERT INTO tb_lesson(title, position, section_id) VALUES ('Aula 1 do capitulo 1', 1, 1);
INSERT INTO tb_content(id, text_Content, video_Uri) VALUES (1, 'Material de apoio', 'https://www.youtube');

INSERT INTO tb_lesson(title, position, section_id) VALUES ('Aula 2 do capitulo 1', 2, 1);
INSERT INTO tb_content(id, text_Content, video_Uri) VALUES (2, 'Material de apoio', 'https://www.youtube');

INSERT INTO tb_lesson(title, position, section_id) VALUES ('Aula 3 do capitulo 1', 3, 1);
INSERT INTO tb_content(id, text_Content, video_Uri) VALUES (3, 'Material de apoio', 'https://www.youtube');

INSERT INTO tb_lesson(title, position, section_id) VALUES ('Tarefa capitulo 1', 4, 1);
INSERT INTO tb_task(id,description,quest_Count,approval_count,weight,due_Date) VALUES (4,'Tarefa de casa aula', 5, 3, 1.0, TIMESTAMP WITH TIME ZONE '2020-12-25T23:00:00Z');

INSERT INTO tb_lessons_done(lesson_id,user_id,offer_id) VALUES (1,1,1);
INSERT INTO tb_lessons_done(lesson_id,user_id,offer_id) VALUES (2,1,1);

INSERT INTO tb_notification (text, moment, read, route, user_id) VALUES ('Primeiro feedback de tarefa: favor revisar', TIMESTAMP WITH TIME ZONE '2020-12-10T13:00:00Z', true, '/offers/1/resource/1/sections/1', 1);
INSERT INTO tb_notification (text, moment, read, route, user_id) VALUES ('Segundo feedback: favor revisar', TIMESTAMP WITH TIME ZONE '2020-12-12T13:00:00Z', true, '/offers/1/resource/1/sections/1', 1);
INSERT INTO tb_notification (text, moment, read, route, user_id) VALUES ('Terceiro feedback: favor revisar', TIMESTAMP WITH TIME ZONE '2020-12-14T13:00:00Z', true, '/offers/1/resource/1/sections/1', 1);

INSERT INTO tb_deliver (uri, moment, status, feedback, correct_Count, lesson_id, user_id, offer_id) VALUES ('https://github.com/devsuperior/bds-dslearn', TIMESTAMP WITH TIME ZONE '2020-12-10T10:00:00Z', 0, null, null, 4, 1, 1);

INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 1', 'Corpo do tópico 1', TIMESTAMP WITH TIME ZONE '2020-12-12T13:00:00Z', 1, 1, 1);
INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 2', 'Corpo do tópico 2', TIMESTAMP WITH TIME ZONE '2020-12-13T13:00:00Z', 2, 1, 1);
INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 3', 'Corpo do tópico 3', TIMESTAMP WITH TIME ZONE '2020-12-14T13:00:00Z', 2, 1, 1);
INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 4', 'Corpo do tópico 4', TIMESTAMP WITH TIME ZONE '2020-12-15T13:00:00Z', 1, 1, 2);
INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 5', 'Corpo do tópico 5', TIMESTAMP WITH TIME ZONE '2020-12-16T13:00:00Z', 1, 1, 2);
INSERT INTO tb_topic (title, body, moment, author_id, offer_id, lesson_id) VALUES ('Título do tópico 6', 'Corpo do tópico 6', TIMESTAMP WITH TIME ZONE '2020-12-17T13:00:00Z', 2, 1, 3);

INSERT INTO tb_topic_likes (topic_id, user_id) VALUES (1, 2);
INSERT INTO tb_topic_likes (topic_id, user_id) VALUES (2, 1);

INSERT INTO tb_reply (body, moment, topic_id, author_id) VALUES ('Tente reiniciar o computador', TIMESTAMP WITH TIME ZONE '2020-12-15T13:00:00Z', 1, 2);
INSERT INTO tb_reply (body, moment, topic_id, author_id) VALUES ('Deu certo, valeu!', TIMESTAMP WITH TIME ZONE '2020-12-20T13:00:00Z', 1, 1);

INSERT INTO tb_reply_likes (reply_id, user_id) VALUES (1, 1);


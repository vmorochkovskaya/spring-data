insert into event (id, title, date, ticket_price) VALUES (1, 'Event-1', '2023-02-02', 20.12);
insert into event (id, title, date, ticket_price) VALUES (2, 'Event-2', '2023-02-03', 12.1);
insert into event (id, title, date, ticket_price) VALUES (3, 'Event-3', '2023-02-04', 10.1);
insert into event (id, title, date, ticket_price) VALUES (4, 'Event-4', '2023-02-03', 30.2);
insert into event (id, title, date, ticket_price) VALUES (5, 'Event-2', '2023-02-01', 12.5);

insert into user_entity (id, email, name) VALUES (1, 'test@gmail.coom', 'Harry');
insert into user_entity (id, email, name) VALUES (2, 'harry@gmail.coom', 'Harry');

insert into ticket (id, user_id, event_id, place, category) VALUES (1, 1, 1, 1, 'DISCOUNTED');
insert into ticket (id, user_id, event_id, place, category) VALUES (2, 1, 2, 5, 'DISCOUNTED');
insert into ticket (id, user_id, event_id, place, category) VALUES (3, 1, 5, 10, 'DISCOUNTED');

insert into user_account (id, amount, user_id) VALUES (1, 60.1, 1);
insert into user_account (id, amount, user_id) VALUES (2, 70, 2);
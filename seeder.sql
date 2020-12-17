USE yum_rando_db;

INSERT INTO users (email, first_name, imgurl, last_name, password, phone_number, username, zipcode)
    VALUES
           ('test@test.com', 'test', 'https://f4.bcbits.com/img/0002994563_10.jpg', 'tester', 'test123', '123456789', 'yummy', '11111'),
        ('yes@test.com', 'yes', 'https://f4.bcbits.com/img/0002994563_10.jpg', 'yesser', 'test123', '987654321', 'rando', '22222'
           ),
           ('friend@test.com', 'friend', 'https://f4.bcbits.com/img/0002994563_10.jpg', 'friender', 'test123', '1111111111', 'friend', '33333'
           );


INSERT INTO restaurants (address, city, name, zipcode)
VALUES ('Chelsea Market, 75 9th Avenue', 'New York City', 'Ninth Street Espresso', '10011'),
       ('849 E. Commerce St. #393', 'San Antonio', 'Fogo de Chao', '78205'),
       ('19009 Preston Rd Suite #200', 'Dallas', 'Lavendou', '75252');


INSERT INTO tags (name) VALUES ('coffee'), ('Brazilian steakhouse'), ('french');


INSERT INTO lists (name, user_id)
VALUES
       ('Cafes', 1),
       ('Expensive', 1),
       ('WishList', 1),
       ('WishList', 2),
       ('WishList', 3);


INSERT INTO reviews (comment, rating, restaurant_id, user_id)
VALUES
       ('Yayz', 5, 1, 1),
       ('Maybe', 4, 2, 2),
       ('Sure', 3, 2, 3);


INSERT INTO photos (description, url, review_id)
VALUES
       ('nice', 'https://66.media.tumblr.com/b7c1fe8553793ab0bb58963b97bed508/tumblr_inline_n740ka2plb1qk43hf.jpg', 1),
       ('sure', 'https://66.media.tumblr.com/b7c1fe8553793ab0bb58963b97bed508/tumblr_inline_n740ka2plb1qk43hf.jpg', 2),
       ('maybe', 'https://66.media.tumblr.com/b7c1fe8553793ab0bb58963b97bed508/tumblr_inline_n740ka2plb1qk43hf.jpg', 3);


INSERT INTO list_restaurants (list_id, restaurant_id) VALUES (1,1),(2,2),(2,3),(3,2),(3,2),(3,3);


INSERT INTO restaurant_tags (restaurant_id, tag_id) VALUES (1,1),(2,2),(2,3),(3,3);


INSERT INTO user_favorite_tags (user_id, tag_id) VALUES (1,1);


INSERT INTO user_friends (friend_id, user_id) VALUES (1,1);

-- Users
INSERT INTO user (id, username, password, email, created_at, updated_at)
VALUES
(1, 'alice', '$2a$10$nViIYzhrQPZ6feTALrb39uQ10EJzIhcY.yZsF5f5HhMjU4GfasDJe', 'alice@mail.com', NOW(), NOW()),
(2, 'bob', '$2a$10$nViIYzhrQPZ6feTALrb39uQ10EJzIhcY.yZsF5f5HhMjU4GfasDJe', 'bob@mail.com', NOW(), NOW())
ON DUPLICATE KEY UPDATE id=id;
-- passwords are 'test!1234'

-- Topics
INSERT INTO topic (id, title, description)
VALUES
(1, 'Tech', 'Dernières actualités tech'),
(2, 'Art', 'Discussions autour de l’art')
ON DUPLICATE KEY UPDATE id=id;

-- Relations user_topic
INSERT INTO user_topic (user_id, topic_id) VALUES (1, 1)
ON DUPLICATE KEY UPDATE user_id=user_id;
INSERT INTO user_topic (user_id, topic_id) VALUES (2, 2)
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Posts
INSERT INTO post (id, title, content, user_id, topic_id, created_at)
VALUES
(1, 'Nouveau framework Java', 'Spring Boot 4 incoming...', 1, 1, NOW()),
(2, 'Exposition à Paris', 'Musée d’Orsay nouvelle expo', 2, 2, NOW())
ON DUPLICATE KEY UPDATE id=id;

-- Comments
INSERT INTO comment (id, content, post_id, user_id, created_at)
VALUES
(1, 'Intéressant merci !', 1, 2, NOW()),
(2, 'Faut que j’y aille', 2, 1, NOW())
ON DUPLICATE KEY UPDATE id=id;

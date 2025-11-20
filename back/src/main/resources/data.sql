-- Users
INSERT INTO user (id, username, password, email, created_at, updated_at)
VALUES
(1, 'alice', '$2a$10$fWnMA3M0.Hae2iebv5TN7OdWShr1rk/F0QFM3ShzbK4z0wTkdHAJS', 'alice@mail.com', NOW(), NOW()),
(2, 'bob', '$2a$10$fWnMA3M0.Hae2iebv5TN7OdWShr1rk/F0QFM3ShzbK4z0wTkdHAJS', 'bob@mail.com', NOW(), NOW())
ON DUPLICATE KEY UPDATE id=id;
-- passwords are 'Test!1234'

-- Topics
INSERT INTO topic (id, title, description)
VALUES
(1, 'Tech', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(2, 'Angular', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(3, 'Java', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(4, 'ReactJS', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(5, 'VueJS', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(6,  'Spring Boot', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(7, 'Maven', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(8, 'Graddle', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(9, 'Kotlin', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...'),
(10, 'Linux', 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry s standard...')
ON DUPLICATE KEY UPDATE id=id;

-- Relations user_topic
INSERT INTO user_topic (user_id, topic_id)
SELECT 1, 1
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 1 AND topic_id = 1
);

INSERT INTO user_topic (user_id, topic_id)
SELECT 2, 2
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 2 AND topic_id = 2
);

INSERT INTO user_topic (user_id, topic_id)
SELECT 1, 3
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 1 AND topic_id = 3
);

INSERT INTO user_topic (user_id, topic_id)
SELECT 2, 4
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 2 AND topic_id = 4
);

INSERT INTO user_topic (user_id, topic_id)
SELECT 1, 5
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 1 AND topic_id = 5
);

INSERT INTO user_topic (user_id, topic_id)
SELECT 2, 6
WHERE NOT EXISTS (
    SELECT 1 FROM user_topic WHERE user_id = 2 AND topic_id = 6
);

-- Posts
INSERT INTO post (id, title, content, user_id, topic_id, created_at)
VALUES
(1, 'Getting Started with Spring Boot 3', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 6, DATE_SUB(NOW(), INTERVAL 30 DAY)),
(2, 'Angular 17 New Features', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 2, 2, DATE_SUB(NOW(), INTERVAL 25 DAY)),
(3, 'Java 21 Virtual Threads', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 3, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(4, 'React Hooks Best Practices', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 2, 4, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(5, 'Maven vs Gradle in 2024', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 7, DATE_SUB(NOW(), INTERVAL 12 DAY)),
(6, 'Vue 3 Composition API Guide', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 5, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(7, 'Linux Command Line Tips', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 2, 10, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(8, 'Kotlin Coroutines Explained', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 9, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(9, 'Tech Trends 2025', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 2, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(10, 'Spring Security with JWT', 'Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', 1, 6, NOW())
ON DUPLICATE KEY UPDATE id=id;

-- Comments
INSERT INTO comment (id, content, post_id, user_id, created_at)
VALUES
(1, 'Très bon article, merci pour le partage !', 1, 2, NOW()),
(2, 'Je suis tout à fait d''accord avec cette approche.', 1, 1, NOW()),
(3, 'Super intéressant, j''attendais justement un article sur ce sujet.', 2, 1, NOW()),
(4, 'Merci pour ces explications claires !', 3, 2, NOW()),
(5, 'Ça va beaucoup m''aider dans mon projet en cours.', 3, 1, NOW()),
(6, 'Excellent tutoriel, très bien détaillé.', 4, 1, NOW()),
(7, 'Je recommande vivement cette lecture.', 5, 2, NOW()),
(8, 'Parfait, c''est exactement ce que je cherchais.', 6, 2, NOW()),
(9, 'Hâte de tester ça sur mon prochain projet !', 7, 1, NOW()),
(10, 'Article très complet, bravo !', 8, 2, NOW()),
(11, 'Merci d''avoir pris le temps d''écrire cet article.', 9, 1, NOW()),
(12, 'Super utile, je mets en favoris.', 10, 2, NOW())
ON DUPLICATE KEY UPDATE id=id;

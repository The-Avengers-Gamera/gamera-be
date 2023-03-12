INSERT INTO "user" (email, name, password, is_verified)
VALUES ('gamera.newseditor1@gmail.com', 'newseditor1', '$2a$10$GjOYMDR2ipNETST0HTU5.O5PttFR/kPnJktBH3nJtQSSbHeSlSJ2C', true),
       ('gamera.newseditor2@gmail.com', 'newseditor2', '$2a$10$UvzzQ3Tn4GRbnKLEXrsvhuqqjYsfRkWpc/JjSfSsj9dygmjSw28n2', true),
       ('gamera.revieweditor1@gmail.com', 'revieweditor1', '$2a$10$poRDbeqQ5X2dfV0CrtkC3ettMUuP5wfD8suAWMcYhaVDVlFcYKaNC', true),
       ('gamera.revieweditor2@gmail.com', 'revieweditor2', '$2a$10$RJiA2Rp296oROPkhWfPrKeqerM5me.Au9J8iShFNLcAvzD4GbslVe', true),
       ('gamera.admin1@gmail.com', 'admin1', '$2a$10$QTqDPgtYm4UwdAytMkMs7./5KiP7ZK.n/jXLWmGbjKJtXLB240okq', true),
       ('gamera.admin2@gmail.com', 'admin2', '$2a$10$iDXa2CajtxS.vgrB1GGJL.djEkCMrQ5dsm1cbPRMiMhQVRGnrl9py', true),
       ('gamera.testuser1@gmail.com', 'testuser1', '$2a$10$8NCLRG0R1p0lrfxONvyll.zY7aWcEKfsPFrao10CfjtRqxlMFMMo2', true),
       ('gamera.testuser2@gmail.com', 'testuser2', '$2a$10$ICSJMK2kaURYfbnzjRrctOJLSofDlDsJE2ov4OI/2pLFyKdpQobFe', true);

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 4
FROM "user" as u
WHERE u.email = 'gamera.newseditor1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 4
FROM "user" as u
WHERE u.email = 'gamera.newseditor2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 3
FROM "user" as u
WHERE u.email = 'gamera.revieweditor1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 3
FROM "user" as u
WHERE u.email = 'gamera.revieweditor2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 2
FROM "user" as u
WHERE u.email = 'gamera.admin1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 2
FROM "user" as u
WHERE u.email = 'gamera.admin2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.newseditor1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.newseditor2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.revieweditor1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.revieweditor2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.admin1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.admin2@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.testuser1@gmail.com';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'gamera.testuser2@gmail.com';

-- password format: Pwd{name}
-- For example: Pwdnewseditor1

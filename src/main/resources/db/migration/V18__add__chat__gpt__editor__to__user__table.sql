INSERT INTO "user" (email, name, password, is_verified)
VALUES ('chatgpt@gamera.com.au',
        'Chat GPT Editor',
        '$2a$10$pAUyae/jFioOlRuF.oo3V.u/U8/jMsU5N6tcDYhZkP0u.xOTuC09i',
        true);

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 3
FROM "user" as u
WHERE u.email = 'chatgpt@gamera.com.au';

INSERT INTO user_authority (user_id, authority_id)
SELECT u.id, 1
FROM "user" as u
WHERE u.email = 'chatgpt@gamera.com.au';

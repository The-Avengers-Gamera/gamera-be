SELECT a.*, g.name AS "game_name", u.name AS "author_name"
FROM gamera.article a
         LEFT JOIN gamera.game g
                   ON a.game_id = g.id
         LEFT JOIN gamera.user u
                   ON a.author_id = u.id
WHERE a.updated_time > :sql_last_value
ORDER BY a.updated_time ASC;

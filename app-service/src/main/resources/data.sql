-- Insert Users
INSERT INTO users (name, email, mobile_number, created_at, created_by)
VALUES
    ('John Doe', 'john.doe@example.com', '+1234567890', CURRENT_TIMESTAMP, 'SYSTEM'),
    ('Jane Smith', 'jane.smith@example.com', '+1987654321', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO posts (users_id, title, body, created_at, created_by)
VALUES
    (1, 'qui est esse',
     'est rerum tempore vitae
sequi sint nihil reprehenderit dolor beatae ea dolores neque
fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis
qui aperiam non debitis possimus qui neque nisi nulla',
     CURRENT_TIMESTAMP, 'SYSTEM'),

    (2, 'ea molestias quasi exercitationem repellat qui ipsa sit aut',
     'et iusto sed quo iure
voluptatem occaecati omnis eligendi aut ad
voluptatem doloribus vel accusantium quis pariatur
molestiae porro eius odio et labore et velit aut',
     CURRENT_TIMESTAMP, 'SYSTEM');
CREATE TABLE IF NOT EXISTS `users`(
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL,
    `mobile_number` varchar(20) NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT NULL,
    `updated_by` varchar(20) NULL,
    INDEX idx_users_email (email)
    );

CREATE TABLE IF NOT EXISTS posts (
   `id`int AUTO_INCREMENT PRIMARY KEY,
   `users_id` int NOT NULL,
   `title` VARCHAR(255) NOT NULL,
   `body` TEXT,
   `created_at` TIMESTAMP NOT NULL,
   `created_by` varchar(20) NOT NULL,
   `updated_at` TIMESTAMP DEFAULT NULL,
   `updated_by` varchar(20) NULL,
    CONSTRAINT fk_user FOREIGN KEY(users_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_posts_users_id (users_id),
    INDEX idx_posts_title (title)
);

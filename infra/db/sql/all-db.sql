-- ==========================
-- 🛡️ Auth_DB (Authentication & Authorization)
-- ==========================
CREATE DATABASE Auth_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE Auth_DB;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NULL, -- NULL for OAuth users
    oauth_provider ENUM(
        'google',
        'facebook',
        'github',
        'local'
    ) DEFAULT 'local',
    oauth_id VARCHAR(255) UNIQUE NULL, -- Unique ID from OAuth provider
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM('admin', 'user', 'moderator') UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ==========================
-- 👤 User_DB (Profiles & Social)
-- ==========================
CREATE DATABASE User_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE User_DB;

CREATE TABLE profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(255),
    bio TEXT,
    profile_picture VARCHAR(255),
    status ENUM(
        'active',
        'inactive',
        'banned'
    ) DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE follows (
    follower_id BIGINT,
    following_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users (id) ON DELETE CASCADE
);

-- ==========================
-- 📝 Post_DB (Posts & Media)
-- ==========================
CREATE DATABASE Post_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE Post_DB;

CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    content TEXT,
    media_url VARCHAR(255) NULL,
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- ==========================
-- 💬 Comment_DB (Comments & Replies)
-- ==========================
CREATE DATABASE Comment_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE Comment_DB;

CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT,
    user_id BIGINT,
    parent_comment_id BIGINT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments (id) ON DELETE CASCADE
);

CREATE TABLE reported_comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id BIGINT,
    reported_by BIGINT,
    reason TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE,
    FOREIGN KEY (reported_by) REFERENCES users (id) ON DELETE CASCADE
);

-- ==========================
-- ❤️ Reaction_DB (Likes & Reactions)
-- ==========================
CREATE DATABASE Reaction_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE Reaction_DB;

CREATE TABLE reactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    post_id BIGINT NULL,
    comment_id BIGINT NULL,
    reaction_type ENUM(
        'like',
        'love',
        'haha',
        'sad',
        'angry'
    ),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE
);

-- ==========================
-- 🔔 Notification_DB (Real-time & Push Notifications)
-- ==========================
CREATE DATABASE Notification_DB DEFAULT CHARACTER SET = 'utf8mb4';

USE Notification_DB;

CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
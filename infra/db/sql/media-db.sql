-- ==========================
-- 📝 Post_DB (Posts & Media)
-- ==========================
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    content TEXT,
    media_url VARCHAR(255) NULL,
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==========================
-- 💬 Comment_DB (Comments & Replies)
-- ==========================
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT,
    user_id BIGINT,
    parent_comment_id BIGINT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments (id) ON DELETE CASCADE
);

CREATE TABLE reported_comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id BIGINT,
    reported_by BIGINT,
    reason TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE
);

-- ==========================
-- ❤️ Reaction_DB (Likes & Reactions)
-- ==========================
CREATE TABLE reactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    post_id BIGINT NULL,
    comment_id BIGINT NULL,
    reaction_type ENUM(
        'like',
        'love',
        'haha',
        'wow',
        'sad',
        'angry'
    ),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE
);
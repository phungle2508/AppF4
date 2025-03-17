# 🛠 **Microservices Architecture & Database Design**  

## 📌 Database Table Diagram: [View Here](https://dbdiagram.io/d/67d84bd475d75cc84466c804)

---

## **1️⃣ Microservices Overview**  

Each core feature is handled by a separate microservice, ensuring **loose coupling**. While some services may share a database, ideally, each should have its own dedicated storage.  

| **Microservice**         | **Responsibilities**                             | **Database**                 |
|--------------------------|--------------------------------------------------|------------------------------|
| 🔐 **Auth Service**      | Authentication, authorization (JWT, OAuth2)     | MySQL (`users`, `roles`)     |
| 👤 **User Service**      | User profiles, followers, relationships         | MySQL (`profiles`, `relations`) |
| 📝 **Post Service**      | Post creation, editing, media storage          | MySQL (`posts`, `media`)     |
| 💬 **Comment Service**   | Comments, replies, reactions on comments       | MySQL (`comments`, `reactions`) |
| ❤️ **Reaction Service**  | Likes & reactions tracking                     | MySQL (`reactions`)          |
| 🔔 **Notification Service** | Real-time & push notifications           | Redis / Kafka                |
| 🔍 **Search Service**    | Search users, posts, topics                    | Elasticsearch / OpenSearch   |

> **Communication:** Microservices expose **RESTful APIs** (or **GraphQL/WebSockets**) for interaction.  

---

## **2️⃣ Database Schema (MySQL)**  

Each microservice maintains its own **isolated database**.  

### 🔐 **Auth Service (Authentication & Authorization)**  
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    oauth_provider ENUM('google', 'facebook', 'github', 'local') DEFAULT 'local',
    oauth_id VARCHAR(255) NULL,
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
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
```

---

### 👤 **User Service (Profile & Social Features)**  
```sql
CREATE TABLE profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(255),
    bio TEXT,
    profile_picture VARCHAR(255),
    status ENUM('active', 'inactive', 'banned') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE follows (
    follower_id BIGINT,
    following_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

### 📝 **Post Service (Posts & Media)**  
```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    content TEXT,
    media_url VARCHAR(255) NULL,
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

### 💬 **Comment Service (Post Interactions)**  
```sql
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT,
    user_id BIGINT,
    parent_comment_id BIGINT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE
);
```

---

### ❤️ **Reaction Service (Likes & Reactions)**  
```sql
CREATE TABLE reactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    post_id BIGINT NULL,
    comment_id BIGINT NULL,
    reaction_type ENUM('like', 'love', 'haha', 'sad', 'angry'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);
```

---

### 🔔 **Notification Service (Using Kafka/Redis)**  

**Persistent Notifications (Database Backup)**  
```sql
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```
> **Real-time notifications** are managed via **Kafka/WebSockets**.

---

### 🔍 **Search Service (Elasticsearch)**  
Instead of storing search data in MySQL, we use **Elasticsearch** for indexing:  
- Index **users**, **posts**, and **hashtags**  
- Keep MySQL as the **source of truth**  
- Sync updates via an **event-driven approach**  

---

## **3️⃣ Microservices Communication**  

Since each microservice has its own **database**, we use different communication strategies:  

### ✅ **Synchronous (REST / gRPC)**
- **User Service → Auth Service** (`GET /users/{id}`)
- **Post Service → User Service** (`GET /profiles/{id}`)

### 🔄 **Asynchronous (Kafka / RabbitMQ)**
- **New Post Event** → Notifies followers via **Notification Service**  
- **New Reaction Event** → Updates analytics in **Engagement Metrics Service**  
- **Deleted User Event** → Removes all posts, comments, and notifications related to the user  

---

## **4️⃣ Optimizations & Best Practices**  

✔ **Read-replicas** for MySQL to handle high read loads 📊  
✔ **Redis caching** for frequently accessed data (e.g., user profiles) ⚡  
✔ **Sharding** for large datasets (e.g., partitioning the `posts` table) 🗂️  
✔ **Change Data Capture (CDC)** to sync MySQL with Elasticsearch 🔍  
✔ **Secure API gateways** with rate-limiting & JWT authentication 🔒  

---

💡 **Scalability & Performance Considerations**  
🔹 **Horizontal Scaling**: Use containerization (Docker, Kubernetes) to scale individual services.  
🔹 **Database Partitioning**: Distribute large tables to improve query performance.  
🔹 **Message Queues (Kafka/RabbitMQ)**: Reduce dependency on synchronous API calls.  
🔹 **CDN for Media**: Optimize media storage & delivery using a Content Delivery Network.  

---
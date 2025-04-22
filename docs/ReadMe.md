# <a name="-microservices-architecture--database-design">🛠 **Microservices Architecture & Database Design**

## <a name="-table-of-contents">📑 Table of Contents

- [🛠 **Microservices Architecture \& Database Design**](#-microservices-architecture--database-design)
  - [📑 Table of Contents](#-table-of-contents)
  - [1️⃣ Microservices Overview](#1️⃣-microservices-overview)
  - [2️⃣ Database Schema Overview (MySQL)](#2️⃣-database-schema-overview-mysql)
    - [👤 User DB](#-user-db)
    - [📝 Reel DB](#-reel-db)
    - [💬 LikeComment DB](#-likecomment-db)
    - [🔔 Notification DB](#-notification-db)
    - [📰 Feed DB](#-feed-db)
    - [🛡 Keycloak DB (Simplified)](#-keycloak-db-simplified)
  - [3️⃣ Microservices Communication](#3️⃣-microservices-communication)
    - [🔗 **Entity Relationships**](#-entity-relationships)
    - [🔄 **Asynchronous (Kafka / RabbitMQ)**](#-asynchronous-kafka--rabbitmq)
  - [4️⃣ Optimizations \& Best Practices](#4️⃣-optimizations--best-practices)


---
## <a name="1-microservices-overview"></a>1️⃣ Microservices Overview

Each core feature is handled by a separate microservice, ensuring **loose coupling**. While some services may share a database, ideally, each should have its own dedicated storage.

| **Microservice**             | **Responsibilities**                                                  | **Database**                                         | **Public APIs**                             |
|------------------------------|-----------------------------------------------------------------------|------------------------------------------------------|---------------------------------------------|
| 👤 **User Service**          | Manage user accounts, credentials, and profile information            | MySQL [(User DB)](#-user-db)                          | [RandomUser API](https://randomuser.me/documentation) |
| 📝 **Reel Service**          | Handle creation, storage, and retrieval of user-generated reels       | MySQL [(Reel DB)](#-reel-db)                          | [News API](https://newsapi.org/)            |
| 💬 **Like & Comment Service** | Enable users to like reels and comment; manage engagement data       | MySQL [(LikeComment DB)](#-likecomment-db)            | [News API](https://newsapi.org/)            |
| 🔔 **Notification Service**  | Generate and manage notifications (likes, comments, etc.)            | MySQL [(Notification DB)](#-notification-db)          | —                                           |
| 📰 **Feed Service**          | Manage user feeds by tracking reels shown to each user               | MySQL [(Feed DB)](#-feed-db)                          | —                                           |
| 🛡 **Auth Service (Keycloak)**| Handle authentication and authorization via Keycloak integration     | [Keycloak DB (simplified)](#-keycloak-db-simplified) | —                                           |

> **Communication:** Microservices expose **RESTful APIs** (or **GraphQL/WebSockets**) for interaction.

---

## <a name="2-database-schema-overview-mysql"></a>2️⃣ Database Schema Overview (MySQL)

This project defines a modular database schema for a social video-sharing platform, structured into multiple logical packages:

---

### <a name="-user-db"></a>👤 User DB

```sql
CREATE TABLE User (
    id UUID PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    created_at TIMESTAMP
);
```

---

### <a name="-reel-db"></a>📝 Reel DB

```sql
CREATE TABLE Reel (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES User(id),
    title VARCHAR(255),
    video_url VARCHAR(255),
    created_at TIMESTAMP
);
```

---

### <a name="-likecomment-db"></a>💬 LikeComment DB

```sql
CREATE TABLE Comment (
    id UUID PRIMARY KEY,
    reel_id UUID REFERENCES Reel(id),
    user_id UUID REFERENCES User(id),
    content TEXT,
    created_at TIMESTAMP
);

CREATE TABLE Like (
    id UUID PRIMARY KEY,
    reel_id UUID REFERENCES Reel(id),
    user_id UUID REFERENCES User(id),
    created_at TIMESTAMP
);
```

---

### <a name="-notification-db"></a>🔔 Notification DB

```sql
CREATE TABLE Notification (
    id UUID PRIMARY KEY,
    recipient_id UUID REFERENCES User(id),
    type VARCHAR(100),
    message TEXT,
    is_read BOOLEAN,
    created_at TIMESTAMP
);
```

---

### <a name="-feed-db"></a>📰 Feed DB

```sql
CREATE TABLE FeedItem (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES User(id),
    reel_id UUID REFERENCES Reel(id),
    timestamp TIMESTAMP
);
```

---

### <a name="-keycloak-db-simplified"></a>🛡 Keycloak DB (Simplified)

```sql
CREATE TABLE KeycloakUser (
    id UUID PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    realm_id UUID
);
```
---

## <a name="3-microservices-communication"></a>3️⃣ Microservices Communication

### <a name="-entity-relationships">🔗 **Entity Relationships**
- **User → Reel**: A user owns many reels  
- **User → Comment**: A user writes many comments  
- **User → Like**: A user gives likes  
- **User → Notification**: A user receives notifications  
- **User → FeedItem**: A user sees feed items  
- **Reel → Comment**: A reel has many comments  
- **Reel → Like**: A reel can be liked  
- **Reel → FeedItem**: A reel appears in user feeds  

### <a name="-asynchronous-kafka--rabbitmq">🔄 **Asynchronous (Kafka / RabbitMQ)**
- **New Reel Event** → Notifies followers via **Notification Service**  
- **New Reaction Event** → Updates analytics in **Engagement Metrics Service**    

---

## <a name="4-optimizations--best-practices"></a>4️⃣ Optimizations & Best Practices

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
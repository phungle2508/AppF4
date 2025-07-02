# 🚀 Project Name

![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=next.js&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

A full-stack microservices project featuring a modern **Next.js** microfrontend and a **Spring Boot** backend, powered by **Kafka**, **Redis**, **Consul**, and more.

---

## 🛠️ Tech Stack

### 🌐 Frontend

- **Framework:** [Next.js](https://nextjs.org/)
- **Micro Frontends:** [Turborepo](https://turbo.build/)
- **Styling:** [Tailwind CSS 4.0](https://tailwindcss.com/)
- **State Management:** [Zustand](https://zustand-demo.pmnd.rs/)
- **Authentication:** [Keycloak](https://www.keycloak.org/)
- **Deployment & Hosting:** [Vercel](https://vercel.com/)

### 🏗️ Backend

- **Framework:** [Spring Boot](https://spring.io/projects/spring-boot)
- **Microservices:** [JHipster](https://www.jhipster.tech/)
- **Event Streaming:** [Apache Kafka](https://kafka.apache.org/)
- **Cache:** [Redis](https://redis.io/)
- **Authentication & Security:** [Keycloak](https://www.keycloak.org/)
- **Search Engine:** [Elasticsearch](https://www.elastic.co/)
- **Service Discovery:** [Consul](https://www.consul.io/)

### 🔧 Infrastructure

- **Containerization:** [Docker](https://www.docker.com/)
- **Orchestration:** [Kubernetes](https://kubernetes.io/)
- **Version Control:** Git + Submodules

---

## 📁 Project Structure

```
📦 project-root
├── 📂 microfrontend              # Next.js + Turborepo setup
│   ├── 📂 apps
│   │   ├── gateway               # Public-facing frontend
│   │   ├── admin                 # Admin dashboard
│   │   └── main                  # Main frontend module
│   ├── 📂 packages               # Shared libraries/components
│   └── 📂 shared                 # Common styles, utilities
│
├── 📂 backend                    # JHipster-based Spring Boot microservices
│   ├── service1                  # Business logic microservice
│   ├── service2                  # Another microservice
│   └── gateway                   # API gateway and routing
│
├── 📂 infra
│   ├── vps-config                # Infrastructure setup: Consul, Kafka, Redis, etc.
│   └── microservices-vps-config # Backend deployment scripts & SQL setup
│
├── 📂 docs                       # Scripts for common dev/admin actions
└── 📜 README.md
```

---

## 🧪 Local Development Setup

### 🔄 Clone with Submodules

```bash
git clone --recurse-submodules 
# or if already cloned
git submodule update --init --recursive
```

### 🚀 Running Backend Services Locally

> All backend services are deployed to a remote VPS for staging or production environments.

To run a specific service locally:
1. You will need my vps password
2. Open the corresponding `Dev*.java` file in your IDE.
3. Run it as a Spring Boot application.

Make sure environment variables (such as database, Kafka, Redis URLs) are properly configured in `application-dev.yml` or `.env` if used.

---

## 📦 Deployment Notes

- **Frontend:** Hosted via [Vercel](https://vercel.com/) with automatic CI/CD.
- **Backend:** Deployed on a VPS with Docker, managed manually or via scripts under `infra/`.
- **Services:** Kafka, Redis, Elasticsearch, and Consul are containerized and managed in the `vps-config` setup.
- **Secrets Management:** Keycloak is used for identity and access, secrets are managed per-environment.

---

## 📌 Git Strategy

This project uses **Git Submodules** for modular separation of concerns:

```bash
git submodule update --init --recursive
```

---



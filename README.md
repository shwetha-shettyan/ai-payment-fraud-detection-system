# AI-Powered Payment Fraud Detection System

An end-to-end **distributed payment fraud detection platform** built using:

- Spring Boot
- FastAPI
- Apache Kafka
- Redis
- PostgreSQL
- Machine Learning
- Multi-Agent AI Architecture
- Docker

This system processes payment events asynchronously, performs fraud analysis using deterministic agents + ML models, and generates explainable fraud decisions using an LLM-powered Investigation Agent.

---

# System Architecture

```text
                +----------------------+
                |   Payment Service    |
                |   (Spring Boot)      |
                +----------+-----------+
                           |
                           | Payment Event
                           v
                    +------+------+
                    |    Kafka    |
                    +------+------+
                           |
                           v
          +----------------------------------+
          | Fraud Detection Service          |
          | (FastAPI + Python + AI Agents)   |
          +----------------------------------+
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
   Velocity Agent    Device Agent     Geo Agent
        |                 |                |
        +-----------------+----------------+
                          |
                          v
                    ML Fraud Agent
                          |
                          v
                Investigation Agent (LLM)
                          |
                          v
                  Supervisor Orchestrator
                          |
                          v
                 Fraud Result Published
                          |
                          v
                        Kafka
                          |
                          v
                 Payment Service Updates
```

---

# Features

## Payment Service (Spring Boot)

- JWT Authentication & Authorization
- Payment Creation & Confirmation APIs
- Idempotency Handling
- Transaction State Management
- Kafka Event Publishing
- PostgreSQL Integration
- Retry-safe Architecture
- RESTful APIs

---

## Fraud Detection Service (FastAPI + Python)

- Kafka Consumer/Producer
- ML-based Fraud Prediction
- Redis-based Velocity Tracking
- Multi-Agent Fraud Analysis
- AI-powered Investigation Agent
- Explainable Fraud Decisions
- Real-time Fraud Scoring

---

# AI Components

## Deterministic Risk Agents

### Velocity Agent
Detects suspicious transaction frequency using Redis sliding-window tracking.

### Device Agent
Evaluates trusted/untrusted device patterns.

### Geo Agent
Analyzes risky geolocation/IP patterns.

### ML Fraud Agent
Uses a RandomForestClassifier to predict fraud probability.

---

## Investigation Agent (LLM)

An LLM-powered reasoning layer built using LangChain.

### Responsibilities
- Analyze outputs from all risk agents
- Resolve conflicting fraud signals
- Generate explainable fraud narratives
- Recommend:
  - APPROVED
  - REVIEW
  - BLOCKED

### Example Output

```text
Decision: REVIEW

Explanation:
The transaction exhibits elevated behavioral velocity with multiple
transactions occurring within a short time window. While device
and geographic signals appear trusted, the behavioral anomaly
requires manual review.
```

---

# Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring Security
- JWT
- Python 3.11
- FastAPI

---

## Messaging & Storage
- Apache Kafka
- Redis
- PostgreSQL

---

## AI / ML
- Scikit-learn
- RandomForestClassifier
- LangChain
- OpenAI / Groq LLMs

---

## DevOps
- Docker
- Docker Compose

---

# Project Structure

```text
payment-service-withAI/
│
├── payment-service/                # Spring Boot Payment Service
│   ├── src/main/java/com/payment
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── error/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── PaymentServiceApplication.java
│   ├── resources
│   │   ├── application.properties
│   │
│   ├── pom.xml
│   └── Dockerfile
│
├── fraud-detection-service/        # FastAPI Fraud Detection Service
│   ├── app/
│   │   ├── agents/
│   │   ├── kafka/
│   │   ├── services/
│   │   ├── schemas/
│   │   └── main.py
│   │
│   ├── train_model.py
│   └── requirements.txt
│
├── docker-compose.yml
└── README.md
```

---

# Kafka Topics

| Topic | Description |
|------|-------------|
| `payment-topic` | Payment events |
| `fraud-result-topic` | Fraud analysis results |

---

# Redis Usage

Redis is used for:

- Transaction velocity tracking
- Sliding-window fraud detection
- Real-time behavioral analysis

### Example

```text
user:user_1 -> transaction timestamps
```

---

# ML Model

### Model
- RandomForestClassifier

### Features
- Transaction Amount
- Velocity Count
- Device Risk
- Geo Risk

### Output
- Fraud Probability Score

---

# Running the Project

## 1. Clone Repository

```bash
git clone https://github.com/shwetha-shettyan/ai-payment-fraud-detection-system.git
```

---

## 2. Start Services

```bash
docker-compose up --build
```

---

# Services

| Service | Port |
|---------|------|
| Payment Service | 8080 |
| Fraud Detection Service | 8001 |
| Kafka | 9092 |
| PostgreSQL | 5432 |
| Redis | 6379 |

---

# Sample API

## Create Payment

### Endpoint

```http
POST /payments
```

### Request Body

```json
{
  "userId": "user_1",
  "amount": 120000,
  "currency": "INR",
  "paymentMethod": "CARD",
  "ipAddress": "192.168.1.10",
  "deviceId": "iphone-15pro"
}
```
## Confirm Payment

### Endpoint

```http
POST /payments/<<paymentIntentId>>/confirm
```

### Request Body

```json
{
  "ipAddress": "192.168.1.10",
  "deviceId": "iphone-15pro"
}
```
### Request Header

```Idempotency-Key = 9029b14e4cca```

## Get Payment Info

### Endpoint

```http
GET /payments/<<paymentIntentId>>
```
### Response Body

```json
{
  "amount": 50000,
  "createdAt": "2026-06-09T08:20:17.42462",
  "currency": "INR",
  "deviceId": "root-device",
  "failureReason": "2 transactions in last 3 minutes, Device checked: root-device, IP analyzed: 192.169.1.1, ML fraud score = 78.0",
  "fraudDecision": "REVIEW",
  "fraudScore": 50.75,
  "id": "aa255495-19c0-4ecf-8545-d9af4dd931f7",
  "ipAddress": "192.169.1.1",
  "paymentIntentId": "23fa2e73-a3a0-44d6-b20b-9029b14e4cca",
  "paymentMethod": "CARD",
  "status": "REVIEW",
  "updatedAt": "2026-06-09T08:20:36.520973",
  "userId": "user_1"
}
```

---

# Author

**Shwetha Shettyan**

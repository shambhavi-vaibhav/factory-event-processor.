# Factory Machine Event Processor

A high-performance Spring Boot REST API designed to process real-time event data from factory machines. The service focuses on high-throughput batch ingestion, data validation, deduplication, updates, and machine health analytics.

## 🚀 Live Deployment

**Live API:** https://factory-event-processor.onrender.com

The application is deployed on **Render** using Docker.

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/events/batch` | Ingest and process a batch of machine events |
| GET | `/events/stats` | Calculate machine/event statistics and health status |

---

## 🚀 Key Features

### High-Speed Batch Ingestion
Processes large batches of machine events using Hibernate JDBC batching to improve database write performance.

### Smart Deduplication
- Ignores identical duplicate events with the same `eventId` and payload.
- Allows valid updates when an existing event ID contains changed event data.

### Data Validation
- Rejects events with timestamps more than 15 minutes in the future.
- Rejects events with invalid duration values.
- Returns structured rejection reasons for invalid events.

### Machine Analytics
The `/events/stats` endpoint calculates:

- Total events
- Total defects
- Average defect rate
- Machine health status

Health status is determined using the defect-rate threshold:

- `Healthy` → average defect rate < 2.0
- `Warning` → average defect rate >= 2.0

---

## 🛠 Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.2.2
- **Database:** H2 (In-Memory)
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Deployment:** Docker + Render

---

## 📊 Performance Benchmark

Tested locally with a batch of 1,000 events.

- **Batch Size:** 1,000 events
- **Ingestion Time:** ~0.737 seconds
- **Throughput:** ~1,350 events/sec
- **Processing:** JDBC batching

> Benchmark results depend on hardware, JVM configuration, database configuration, and workload.

---

## 📖 API Usage

### 1. Batch Ingest Events

**POST**

```text
/events/batch

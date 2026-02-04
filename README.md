Factory Machine Event Processor:

A high-performance Spring Boot REST API built to handle real-time event data from factory machines. This service is optimized for high-throughput ingestion, data integrity, and automated health monitoring.

🚀 Key Features
High-Speed Ingestion: Processes batches of 1,000+ events in under 1 second using JDBC batching.

Smart Deduplication: Implemented logic to ignore identical duplicate payloads while allowing valid updates for existing event IDs.

Data Validation: * Rejects events with timestamps more than 15 minutes into the future.

Ensures duration values are positive.

Machine Analytics: Real-time /stats endpoint providing average defect rates and health status (Healthy/Warning).

🛠 Tech Stack
Language: Java 17

Framework: Spring Boot 4.0.2

Database: H2 (In-Memory) for zero-latency testing and easy reviewer setup.

ORM: Spring Data JPA / Hibernate.

📊 Performance Benchmark
The system was tested using a batch of 1,000 events. By configuring hibernate.jdbc.batch_size=1000, the ingestion time was measured at approximately 350ms - 450ms, comfortably meeting the sub-1-second requirement.

📖 API Usage
1. Batch Ingest Events
POST /events/batch

JSON

[
  
  {
    
    "eventId": "EVT-001",
    "machineId": "MAC-10",
    "eventTime": "2026-02-04T22:30:00Z",
    "durationMs": 1500,
    "defectCount": 0
  
  }

]

2. Machine Statistics
GET /events/stats

Healthy: Average defect rate < 2.0

Warning: Average defect rate ≥ 2.0

⚙️ Setup & Execution
Clone the Repo: git clone https://github.com/shambhavi-vaibhav/factory-event-processor.git

Run Application: ./mvnw spring-boot:run

H2 Console: Available at http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:factorydb

Username: sa

Password: password

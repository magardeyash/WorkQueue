# WorkQueue — Spring Boot Distributed Background Task Processing System

Spring Boot implementation of the Redis-based distributed background task processing queue.

## 🏗️ Architecture

- **Producer (`port 8080`)**: Spring Boot REST service accepting tasks via `POST /enqueue` and pushing to Redis `task_queue`.
- **Worker (`port 8081`)**: Spring Boot background service running 3 worker threads popping tasks via blocking pop (`BLPOP` simulation), processing tasks asynchronously, logging to `logs/workqueue.log`, and exposing metrics via `GET /metrics`.
- **Redis (`port 6379`)**: Task queue broker.

---

## 🚀 How to Run

### Option 1: Using Docker Compose
```bash
docker-compose up --build
```

### Option 2: Running Locally
1. Start Redis:
   ```bash
   docker run -d -p 6379:6379 redis:7-alpine
   ```
2. Start Producer (Port 8080):
   ```bash
   cd producer
   mvn spring-boot:run
   ```
3. Start Worker (Port 8081):
   ```bash
   cd worker
   mvn spring-boot:run
   ```

---

## 🧪 Testing Endpoints

### 1. Enqueue `send_email` task
```bash
curl -X POST http://localhost:8080/enqueue \
  -H "Content-Type: application/json" \
  -d '{"type":"send_email","retries":3,"payload":{"to":"user@example.com","subject":"Welcome"}}'
```

### 2. Enqueue `resize_image` task
```bash
curl -X POST http://localhost:8080/enqueue \
  -H "Content-Type: application/json" \
  -d '{"type":"resize_image","retries":2,"payload":{"new_x":1920,"new_y":1080}}'
```

### 3. Enqueue `generate_pdf` task
```bash
curl -X POST http://localhost:8080/enqueue \
  -H "Content-Type: application/json" \
  -d '{"type":"generate_pdf","retries":1,"payload":{"document_id":123}}'
```

### 4. Fetch Worker Metrics
```bash
curl http://localhost:8081/metrics
```

# 🎬 CreatorVault API Gateway

CreatorVault API is the **core backend gateway** for the CreatorVault platform — a toolkit designed to help creators analyze, clip, and manage their video content across platforms.

---

## 🚀 Overview

This service is built with **Kotlin + Spring Boot** and serves as the **entry point** for all CreatorVault backend operations.  
It integrates directly with the **YouTube Data API v3**, manages authentication, and coordinates downstream microservices for video downloads and highlight generation.

---

## 🧩 Architecture (Part 1: API Gateway)

| Layer | Technology | Purpose |
|--------|-------------|----------|
| API | Kotlin, Spring Boot | Expose REST endpoints (`/videos`, `/highlights`, `/me`) |
| HTTP Client | Spring WebClient | Communicate with YouTube & microservices |
| Security | Spring Security (API key) | Basic request filtering |
| Testing | JUnit 5, Mockito Kotlin, MockMvc | Unit & controller tests |

---

## 🧠 Features Implemented (MVP)

- **YouTube Integration:**  
  Fetch creator uploads using OAuth or API key.

- **Highlights API (Stub):**  
  Communicates with Highlight microservice (`/analyze`, `/highlights/{videoId}`).

- **Auth Endpoint:**  
  Returns static user data (to be replaced by OAuth user info).

- **Download Service Stub:**  
  Prepares integration with Python-based video downloader.

---

## 📡 Endpoints

| Method | Endpoint | Description |
|--------|-----------|--------------|
| `GET` | `/videos` | Fetch uploaded YouTube videos |
| `POST` | `/videos/{youtubeId}/download` | Start video download job *(stub)* |
| `GET` | `/videos/{jobId}/status` | Check download status *(stub)* |
| `POST` | `/highlights/analyze` | Trigger video analysis *(mocked)* |
| `GET` | `/highlights/{videoId}` | Get highlights for a given video *(mocked)* |
| `GET` | `/me` | Return current user info |

---

## 🧪 Testing

To run all tests:
```bash
./gradlew clean test

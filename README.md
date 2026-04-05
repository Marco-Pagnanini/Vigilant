<p align="center">
  <img width="120" src="https://github.com/user-attachments/assets/9bbcd338-2460-456c-9778-411fcc4bc30a" alt="Vigilant logo" />
</p>

<h1 align="center">Vigilant</h1>
<p align="center">Open-source, lightweight log monitoring — a simpler alternative to Sentry.</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MongoDB-7-47A248?style=flat-square&logo=mongodb&logoColor=white" />
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/TypeScript-5-3178C6?style=flat-square&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/Java%20SDK-Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/C%23%20SDK-NuGet-004880?style=flat-square&logo=nuget&logoColor=white" />
</p>

<br />

<p align="center">
  <img src="https://github.com/user-attachments/assets/572e37ee-4c9e-45c3-b8ee-f38a121b18f0" alt="Vigilant dashboard" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/f4dfe0d8-e116-42a2-bdf2-192793ce9136" alt="Vigilant logs view" />
</p>

---

## What is Vigilant?

Vigilant is a self-hosted log monitoring platform that lets you collect, store, and visualize logs from any application in real time.

It consists of three parts:

| Component | Description |
|-----------|-------------|
| **Backend** | Spring Boot REST API — receives and persists logs in MongoDB |
| **Frontend** | React dashboard — browse projects and view logs in real time |
| **SDKs** | Drop-in libraries for Java/Spring Boot and C#/ASP.NET |

---

## Architecture

```
Your App (Java / C#)
       │
       │  POST /api/logs/{projectId}
       │  X-API-Key: <project-api-key>
       ▼
  ┌─────────────┐        ┌───────────────┐
  │  Spring Boot │──────▶│   MongoDB 7   │
  │   Backend    │        │  (Docker)     │
  └─────────────┘        └───────────────┘
       │
       │  GET /api/logs/{projectId}
       ▼
  ┌─────────────┐
  │    React    │
  │  Dashboard  │
  └─────────────┘
```

Each project gets its own MongoDB collection (`logs_<projectId>`), keeping data isolated and queries fast.

---

## Getting Started

### Prerequisites

- Docker
- Java 17+
- Node.js 18+

### 1. Start MongoDB

```bash
docker-compose up -d
```

MongoDB will be available at `mongodb://vigilant:vigilant@localhost:27017/vigilant`.

### 2. Start the Backend

```bash
cd backend
./gradlew bootRun
```

The API will be available at `http://localhost:8080`.

### 3. Start the Frontend

```bash
cd frontend
npm install
npm run dev
```

The dashboard will be available at `http://localhost:5173`.

---

## API Reference

### Projects

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/projects` | — | List all projects |
| `POST` | `/api/projects` | — | Create a new project (returns API key) |

**Create project — request body:**
```json
{
  "name": "My App",
  "description": "Production backend"
}
```

**Create project — response:**
```json
{
  "id": "abc123",
  "name": "My App",
  "description": "Production backend",
  "apiKey": "ca496947-42a8-4918-a2da-9c895f30ac79",
  "createdAt": "2025-01-01T12:00:00"
}
```

> **Keep the API key safe** — it will only be returned once at creation time.

### Logs

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/logs/{projectId}?page=0&size=20` | — | Paginated log list |
| `POST` | `/api/logs/{projectId}` | `X-API-Key` | Send a new log |

**Send a log — request body:**
```json
{
  "level": "ERROR",
  "message": "NullPointerException at UserService.java:42",
  "stackTrace": "java.lang.NullPointerException\n\tat com.example..."
}
```

Log levels: `INFO` · `WARN` · `ERROR` · `SUCCESS`

---

## SDKs

### Java / Spring Boot

Add the dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'io.vigilant:vigilant-sdk:0.0.1'
}
```

Configure in `application.properties`:

```properties
vigilant.server-url=http://localhost:8080
vigilant.project-id=your-project-id
vigilant.api-key=your-api-key
vigilant.min-level=INFO
```

That's it. All logs from your Spring Boot application are automatically forwarded to Vigilant via a Logback appender — no code changes needed.

---

### C# / ASP.NET

Install from NuGet:

```bash
dotnet add package Vigilant.Sdk
```

Register in `Program.cs`:

```csharp
builder.Logging.AddVigilant(options =>
{
    options.ServerUrl = "http://localhost:8080";
    options.ProjectId = "your-project-id";
    options.ApiKey   = "your-api-key";
    options.MinLevel = LogLevel.Information;
});
```

All `ILogger` calls are automatically forwarded. Log levels are mapped as follows:

| .NET | Vigilant |
|------|----------|
| `Information` | `INFO` |
| `Warning` | `WARN` |
| `Error` | `ERROR` |
| `Critical` | `ERROR` |

---

## Project Structure

```
Vigilant/
├── backend/                  # Spring Boot API
│   └── src/main/java/
│       ├── Api/              # Controllers + DTOs
│       ├── Application/      # Services + Repository interfaces
│       ├── Infrastructure/   # MongoDB impl + Security
│       └── Model/            # Domain entities
│
├── frontend/                 # React dashboard
│   └── src/
│       ├── api/              # Axios calls
│       ├── components/       # UI components (shadcn/ui)
│       └── types/            # TypeScript interfaces
│
├── SDK/
│   ├── java-springboot/      # Java SDK (Logback + AutoConfiguration)
│   └── csharp/               # C# SDK (ILoggerProvider)
│
└── docker-compose.yml        # MongoDB
```

---

## Security

- **Write** (`POST /api/logs`) requires a valid `X-API-Key` header matched against the project's stored key
- **Read** (`GET /api/logs`, `GET /api/projects`) is open — intended for the internal dashboard
- Each project has its own unique API key, generated at creation time

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 4, MongoDB, Lombok, Clean Architecture |
| Database | MongoDB 7 via Docker |
| Frontend | React 19, TypeScript, Vite, TanStack Query, shadcn/ui, Tailwind CSS v4 |
| Java SDK | Logback `AppenderBase`, Spring Boot AutoConfiguration |
| C# SDK | `ILoggerProvider`, `ILogger`, .NET 8 / 9 / 10 |

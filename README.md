<p align="center">
  <img width="120" src="https://github.com/user-attachments/assets/9bbcd338-2460-456c-9778-411fcc4bc30a" alt="Vigilant logo" />
</p>

<h1 align="center">Vigilant</h1>
<p align="center">Open-source, self-hosted log monitoring — a simpler alternative to Sentry.</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MongoDB-7-47A248?style=flat-square&logo=mongodb&logoColor=white" />
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/TypeScript-5-3178C6?style=flat-square&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white" />
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

Vigilant is a self-hosted log monitoring platform that lets you collect, store, and visualize logs from any application in real time. Everything runs on your own machine or server — no third-party services, no data leaving your infrastructure.

| Component | Description |
|-----------|-------------|
| **Backend** | Spring Boot REST API — receives and persists logs in MongoDB |
| **Frontend** | React dashboard — browse projects and view logs in real time |
| **SDKs** | Drop-in libraries for Java/Spring Boot and C#/ASP.NET |

---

## Quick Start

The only prerequisite is **Docker**. Clone the repo and start everything with one command.

### 1. Clone the repository

```bash
git clone https://github.com/your-username/vigilant.git
cd vigilant
```

### 2. Configure credentials

Open `docker-compose.yml` and set your admin credentials and a strong JWT secret before starting:

```yaml
backend:
  environment:
    ADMIN_USERNAME: admin          # change this
    ADMIN_PASSWORD: changeme       # change this
    JWT_SECRET: change-this-secret-to-something-long-and-random  # change this (min 32 chars)
```

> These are the credentials you will use to log in to the dashboard. The JWT secret is used to sign authentication tokens — use a long random string.

### 3. Start everything

```bash
docker compose up --build
```

This starts three services:
- `mongodb` — database (internal, not exposed)
- `backend` — Spring Boot API on port 8080 (internal)
- `frontend` — Nginx serving the React app on **port 80**

Wait for the logs to show `Started BackendApplication`, then open your browser.

### 4. Open the dashboard

```
http://localhost
```

You will be redirected to the login page. Sign in with the credentials you set in step 2 (defaults: `admin` / `changeme`).

### 5. Create your first project

Once logged in, you will see the dashboard. Create a project to get started — each project gets a unique **API key** that your application uses to send logs.

> The API key is shown **only once** at creation time. Copy it and store it somewhere safe.

### 6. Connect your application

Use the API key and project ID from the previous step to configure one of the SDKs (see [SDKs](#sdks) below). Your logs will appear in the dashboard in real time.

---

## Architecture

```
Your App (Java / C# / any HTTP client)
       │
       │  POST /api/logs/{projectId}
       │  X-API-Key: <project-api-key>
       ▼
┌──────────────┐     ┌──────────────────┐
│    Nginx     │────▶│   Spring Boot    │────▶  MongoDB 7
│  (port 80)   │     │    Backend       │
│              │     │  (port 8080,     │
│  React SPA   │     │   internal)      │
└──────────────┘     └──────────────────┘
       ▲
       │  Browser
       │  http://localhost
```

The frontend is served by Nginx, which also acts as a reverse proxy — all `/api/` calls from the browser are forwarded internally to the backend. The backend and MongoDB are not exposed outside Docker.

Each project stores logs in its own MongoDB collection (`logs_<projectId>`), keeping data isolated and queries fast.

---

## Authentication

Vigilant uses **JWT-based authentication** for the dashboard and **API key authentication** for log ingestion.

| Route | Auth |
|-------|------|
| `POST /api/auth/login` | None — returns a JWT token |
| `GET /api/projects` | JWT (`Authorization: Bearer <token>`) |
| `POST /api/projects` | JWT |
| `GET /api/logs/{projectId}` | JWT |
| `POST /api/logs/{projectId}` | API Key (`X-API-Key` header) |

Dashboard credentials are configured via environment variables in `docker-compose.yml`. There is a single admin user — this is intentional for a self-hosted tool.

---

## API Reference

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/login` | Login and receive a JWT token |

**Request:**
```json
{
  "username": "admin",
  "password": "changeme"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Projects

> All project endpoints require `Authorization: Bearer <token>` header.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/projects` | List all projects |
| `POST` | `/api/projects` | Create a new project (returns API key) |

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

> **Keep the API key safe** — it is returned only once at creation time.

---

### Logs

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/logs/{projectId}?page=0&size=20` | JWT | Paginated log list |
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

## Configuration Reference

All backend configuration is done through environment variables in `docker-compose.yml`:

| Variable | Default | Description |
|----------|---------|-------------|
| `ADMIN_USERNAME` | `admin` | Dashboard login username |
| `ADMIN_PASSWORD` | `changeme` | Dashboard login password |
| `JWT_SECRET` | *(insecure default)* | Secret key for signing JWT tokens — **must be changed** |
| `JWT_EXPIRATION` | `86400000` | Token expiry in milliseconds (default: 24h) |
| `MONGODB_URI` | local MongoDB | Full MongoDB connection string |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173,http://localhost` | Comma-separated list of allowed CORS origins |

---

## Local Development (without Docker)

If you want to run services individually for development:

**Prerequisites:** Java 17+, Node.js 18+, MongoDB running locally

**Backend:**
```bash
cd backend
./gradlew bootRun
```
API available at `http://localhost:8080`.

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```
Dashboard available at `http://localhost:5173`. The `.env.development` file points the API to `http://localhost:8080` automatically.

---

## Project Structure

```
Vigilant/
├── backend/                  # Spring Boot API
│   ├── Dockerfile
│   └── src/main/java/
│       ├── Api/              # Controllers (Auth, Projects, Logs)
│       ├── Application/      # Services + Repository interfaces
│       ├── Infrastructure/   # MongoDB impl + Security (JWT, ApiKey, CORS)
│       └── Model/            # Domain entities
│
├── frontend/                 # React dashboard
│   ├── Dockerfile
│   ├── nginx.conf            # Reverse proxy config
│   └── src/
│       ├── api/              # Axios calls (auth, projects, logs)
│       ├── components/       # UI components (shadcn/ui)
│       ├── pages/            # LoginPage
│       └── types/            # TypeScript interfaces
│
├── SDK/
│   ├── java-springboot/      # Java SDK (Logback + AutoConfiguration)
│   └── csharp/               # C# SDK (ILoggerProvider)
│
└── docker-compose.yml        # Full stack: MongoDB + Backend + Frontend
```

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 4, MongoDB, Lombok, Clean Architecture |
| Database | MongoDB 7 |
| Frontend | React 19, TypeScript, Vite, TanStack Query, shadcn/ui, Tailwind CSS v4 |
| Serving | Nginx (reverse proxy + static files) |
| Auth | JWT (dashboard) + API Key per project (log ingestion) |
| Java SDK | Logback `AppenderBase`, Spring Boot AutoConfiguration |
| C# SDK | `ILoggerProvider`, `ILogger`, .NET 8 / 9 / 10 |
| Infrastructure | Docker Compose |

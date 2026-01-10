# Gradle Monorepo: Spring Boot 4, Kotlin 2.3 & Observability

This repository serves as a **reference architecture** and **starter template** for building modern, scalable microservices using **Spring Boot 4** and **Kotlin 2.3** within a strictly typed **Gradle Monorepo**.

It demonstrates best practices for build logic reuse, dependency management, and a complete **OpenTelemetry-based Observability Stack** (Prometheus, Loki, Tempo, Alloy, Grafana).

---

## 🚀 Key Features

### 1. Modern Toolchain
- **Spring Boot 4.0.1**: ready for the next generation of Java/Kotlin development.
- **Kotlin 2.3.0**: Leveraging the latest language features.
- **Gradle 8+**: High-performance build tool.

### 2. Gradle Build Logic & Version Catalog
Move away from repetitive build scripts. This repo uses **Convention Plugins** in `build-logic` to share configuration across the monorepo:
- **`buildlogic.kotlin-common-conventions`**: Shared Kotlin settings.
- **`buildlogic.kotlin-application-conventions`**: Spring Boot & Docker setup for services.
- **`libs.versions.toml`**: Centralized dependency management (Version Catalog).

### 3. Full Observability Stack (LGTM + Alloy)
A pre-configured observability pipeline using **Grafana Alloy** as the central collector:
- **Metrics**: Spring Boot Micrometer $\rightarrow$ Alloy $\rightarrow$ **Prometheus**
- **Logs**: Logback OTLP Appender $\rightarrow$ Alloy $\rightarrow$ **Loki**
- **Traces**: Micrometer Tracing $\rightarrow$ Alloy $\rightarrow$ **Tempo**
- **Visualization**: **Grafana** pre-wired with datasources.

### 4. Infrastructure as Code
- **Docker Compose**: Orchestrates the entire stack.
- **Gradle Tasks**: Custom tasks to build containers, run infrastructure, and deploy the app seamlessly.

---

## 🛠️ Getting Started

### Prerequisites
- **Docker & Docker Compose** (running)
- **JDK 23** (recommended, though Gradle handles toolchains)

### Build & Run Application
This repository includes custom Gradle tasks to simplify the Docker workflow.

| Task | Command | Description |
|------|---------|-------------|
| **Build Image** | `./gradlew :app-service:dockerBuild` | Builds the Spring Boot JAR and Docker image (`app-service:latest`). |
| **Run Container** | `./gradlew :app-service:dockerRun` | Runs the container in detached mode on port `8080`. |
| **Stop** | `./gradlew :app-service:dockerStop` | Stops and removes the application container. |
| **Cycle** | `./gradlew :app-service:dockerBuildAndRun` | Stops, builds, and runs the fresh container. |

### Run Full Stack (Infra + App)
To spin up the entire observability stack alongside the application:

```bash
./gradlew :app-service:dockerComposeUp
```
This runs `docker-compose.infra.yaml` (Observability) + `app-service/docker-compose.yaml` (App).

To tear everything down:
```bash
./gradlew :app-service:dockerComposeDown
```

---

## 📦 Project Structure

```text
├── build-logic/                  # Custom Convention Plugins
│   └── src/main/kotlin/          # Kotlin source for build logic
├── gradle/
│   └── libs.versions.toml        # Version Catalog (Dependencies)
├── app-service/                  # Example Spring Boot 4 Service
│   ├── src/                      # Source code
│   ├── Dockerfile                # Configured for Layered JARs
│   └── docker-compose.yaml       # App specific compose file
├── observability/                # Configs for Observability Stack
│   ├── alloy/                    # Alloy collector config
│   ├── grafana/                  # Datasources
│   ├── loki/                     # Loki config
│   ├── prometheus/               # Prometheus config
│   └── tempo/                    # Tempo config
└── docker-compose.infra.yaml     # Infra (Prometheus, Loki, Tempo, Alloy, Grafana)
```

---

## 📊 Observability Endpoints

Once the stack is running via `dockerComposeUp`, access the following dashboards:

| Service | URL | Usage |
|---------|-----|-------|
| **Application** | `http://localhost:8080` | The generic Spring Boot app. |
| **Grafana** | `http://localhost:3000` | Visualize Metrics, Logs, and Traces. (Login: admin/admin) |
| **Prometheus** | `http://localhost:9090` | Raw Metrics Query. |
| **Tempo** | `http://localhost:3200` | Trace backend (usually accessed via Grafana). |
| **Alloy** | `http://localhost:12345` | Collector UI (Status & Targets). |

---

## 📝 How to Use This Template

1.  **Copy `build-logic`**: Drop the folder into your root to get instant Kotlin & Spring Boot conventions.
2.  **Copy `gradle/libs.versions.toml`**: Use this as a base for your dependency versions.
3.  **Adapt `settings.gradle.kts`**: meaningful project structure.
4.  **Use Convention Plugins**: In your modules, just apply the plugin:
    ```kotlin
    plugins {
        id("buildlogic.kotlin-application-conventions")
    }
    ```
    This automatically gives you `bootJar`, `dockerBuild`, and OTLP readiness.

---

### Author
Designed to demonstrate the power of **Gradle Monorepos** and **Spring Boot 4 Observability**.

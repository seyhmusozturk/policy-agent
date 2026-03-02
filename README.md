# Policy Agent 🤖

**AI-Powered Insurance Policy Analysis Engine**

A sophisticated Spring Boot application that leverages Large Language Models (LLM) powered by Ollama to automatically analyze insurance policies, identify high-risk clauses, detect fraud indicators, and provide comprehensive risk assessments.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Reference](#api-reference)
- [Project Structure](#project-structure)
- [Core Components](#core-components)
- [Development](#development)
- [Docker Support](#docker-support)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

Policy Agent is an advanced AI system designed to:

- **Analyze insurance policy documents** using natural language processing
- **Extract and score risks** from policy clauses
- **Detect suspicious patterns** that may indicate potential fraud
- **Provide actionable insights** through structured JSON responses
- **Scale efficiently** with Spring Boot's production-ready architecture

The system uses the Ollama LLM framework with the Llama 3 model as default, providing local, privacy-focused policy analysis without relying on external cloud APIs.

---

## ✨ Features

### Core Capabilities

✅ **Policy Text Analysis** - Comprehensive analysis of insurance policy documents  
✅ **Risk Scoring** - Quantitative risk assessment using intelligent algorithms  
✅ **Fraud Detection** - Identifies suspicious patterns and potential fraud indicators  
✅ **Clause Extraction** - Automatically identifies high-risk clauses and terms  
✅ **Structured Output** - Returns well-formatted JSON with actionable insights  
✅ **Extensible Tool System** - Plugin architecture for custom analysis tools  
✅ **Conversation Management** - Support for multi-turn analysis conversations  

### Technical Features

✅ **Spring Boot 3.2.5** - Latest Spring Boot framework  
✅ **Java 21** - Modern Java capabilities  
✅ **Ollama Integration** - Local LLM execution for privacy  
✅ **OkHttp Client** - Reliable HTTP communication with retry logic  
✅ **Jackson Databind** - Robust JSON processing  
✅ **Actuator Endpoints** - Built-in health checks and metrics  
✅ **Validation Framework** - Input validation and error handling  

---

## 🏗️ Architecture

### High-Level Design

```
┌─────────────────────────────────────────────┐
│          REST API Layer                      │
│     (PolicyController)                       │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│      Policy Analysis Agent                   │
│    (PolicyAnalysisAgent)                     │
│  - Conversation Management                   │
│  - LLM Orchestration                         │
│  - Response Parsing                          │
└────────────────────┬────────────────────────┘
                     │
        ┌────────────┴─────────────┐
        │                          │
┌───────▼────────┐      ┌─────────▼──────────┐
│   Tool Registry│      │   LLM Client       │
│  - Risk Scoring│      │  (OllamaLlmClient) │
│  - Custom Tools│      │  - HTTP Requests   │
└────────────────┘      │  - Response Parsing│
                        └──────────┬─────────┘
                                   │
                        ┌──────────▼──────────┐
                        │  Ollama LLM Server  │
                        │  (Local 11434)      │
                        └─────────────────────┘
```

### Component Interaction Flow

```
User Request
    ↓
[REST API] → /api/policies/analyze
    ↓
[PolicyAnalysisAgent] → Prepare system prompt + policy text
    ↓
[Conversation] → Build message structure
    ↓
[OllamaLlmClient] → HTTP request to Ollama server
    ↓
[Ollama Server] → Process with Llama 3 model
    ↓
[Response Parsing] → Extract & validate JSON
    ↓
[PolicyAnalysisResult] → Return structured analysis
    ↓
User Response ← JSON with risk data
```

---

## 📋 Prerequisites

### System Requirements

- **Java 21 or higher**
- **Maven 3.8.1 or higher**
- **Ollama Runtime** (for LLM functionality)
- **8GB RAM minimum** (recommended 16GB)
- **2GB disk space** for model caching

### Software Dependencies

1. **Java Development Kit (JDK) 21**
   ```bash
   # Verify installation
   java -version
   ```

2. **Apache Maven**
   ```bash
   # Verify installation
   mvn --version
   ```

3. **Ollama** (required for LLM)
   - Download from: https://ollama.ai
   - Install and start the Ollama service
   - Default listens on: `http://127.0.0.1:11434`

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/seyhmus/policy-agent.git
cd policy-agent
```

### Step 2: Install Ollama

**macOS/Linux:**
```bash
curl https://ollama.ai/install.sh | sh
```

**Windows:**
Download from [Ollama Release](https://github.com/ollama/ollama/releases) and run the installer.

### Step 3: Start Ollama Server

```bash
ollama serve
```

The server starts on `http://127.0.0.1:11434` by default.

### Step 4: Pull the Required Model

In a new terminal:
```bash
ollama pull llama3
```

This downloads the Llama 3 model (~4.7GB).

### Step 5: Build the Application

```bash
mvn clean install
```

### Step 6: Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/policy-agent-0.0.1-SNAPSHOT.jar
```

The application starts on `http://localhost:8080`

---

## ⚙️ Configuration

### application.yml

Configuration file: `src/main/resources/application.yml`

```yaml
# Ollama Configuration
ollama:
  model: llama3              # LLM model name (default: llama3)
  base-url: http://127.0.0.1:11434  # Ollama server URL

# Spring Boot Server
server:
  port: 8080
  servlet:
    context-path: /

# Logging (optional)
logging:
  level:
    root: INFO
    com.seyhmus.ai: DEBUG
```

### Environment Variables

You can override configuration via environment variables:

```bash
export OLLAMA_MODEL=llama2
export OLLAMA_BASE_URL=http://custom-ollama-host:11434

mvn spring-boot:run
```

### Customization Options

**Change LLM Model:**
```yaml
ollama:
  model: llama2  # or mistral, neural-chat, etc.
```

**Change Ollama Server Address:**
```yaml
ollama:
  base-url: http://192.168.1.100:11434
```

**Adjust Timeouts:**
Modify `OllamaLlmClient.java`:
```java
.connectTimeout(30, TimeUnit.SECONDS)    // Connection timeout
.readTimeout(180, TimeUnit.SECONDS)      // Read timeout
.writeTimeout(180, TimeUnit.SECONDS)     // Write timeout
```

---

## 💡 Usage

### Basic API Usage

#### Analyze a Policy

**Endpoint:** `POST /api/policies/analyze`

**Request:**
```bash
curl -X POST http://localhost:8080/api/policies/analyze \
  -H "Content-Type: application/json" \
  -d "Your complete insurance policy document text here..."
```

Or with a file:
```bash
curl -X POST http://localhost:8080/api/policies/analyze \
  -H "Content-Type: application/json" \
  --data-raw "$(cat policy.txt)"
```

**Example Policy Text:**
```
POLICY DOCUMENT
Policy Number: POL-2024-001234
Coverage: Comprehensive Health Insurance
Exclusions: Pre-existing conditions excluded. Emergency dental care excluded.
Deductible: $500 per year
Coverage limit: 75% of treatment costs
```

**Response:**
```json
{
  "riskScore": 65,
  "highRiskClauses": [
    "Pre-existing conditions excluded",
    "Emergency dental care excluded"
  ],
  "fraudSuspicion": false,
  "summary": "Policy contains significant exclusion clauses that limit coverage. The 75% coverage limit is moderate. No obvious fraud indicators detected."
}
```

### Understanding the Response

| Field | Type | Description |
|-------|------|-------------|
| `riskScore` | Integer | Risk level from 0-100 (higher = riskier) |
| `highRiskClauses` | String[] | List of problematic policy clauses |
| `fraudSuspicion` | Boolean | Whether suspicious patterns detected |
| `summary` | String | Human-readable analysis summary |

---

## 🔌 API Reference

### POST /api/policies/analyze

Analyzes an insurance policy document and returns risk assessment.

**Request:**
- **Method:** POST
- **Content-Type:** application/json
- **Body:** Plain text or raw policy document

**Response Status:**
- `200 OK` - Analysis successful
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - LLM or server error

**Response Body:**
```json
{
  "riskScore": number,
  "highRiskClauses": [string],
  "fraudSuspicion": boolean,
  "summary": string
}
```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/policies/analyze \
  -H "Content-Type: application/json" \
  -d "$(cat policy_document.txt)"
```

**Example Python:**
```python
import requests
import json

url = "http://localhost:8080/api/policies/analyze"
policy_text = "Your policy document..."

response = requests.post(url, data=policy_text)
result = response.json()

print(f"Risk Score: {result['riskScore']}")
print(f"Fraud Risk: {result['fraudSuspicion']}")
print(f"Summary: {result['summary']}")
```

**Example JavaScript:**
```javascript
const policyText = "Your policy document...";

fetch('http://localhost:8080/api/policies/analyze', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: policyText
})
  .then(res => res.json())
  .then(data => console.log(data))
  .catch(err => console.error(err));
```

---

## 📁 Project Structure

```
policy-agent/
├── pom.xml                          # Maven configuration
├── README.md                         # This file
├── src/
│   ├── main/
│   │   ├── java/com/seyhmus/ai/policyagent/
│   │   │   ├── PolicyAgentApplication.java     # Spring Boot entry point
│   │   │   ├── agent/
│   │   │   │   ├── engine/
│   │   │   │   │   └── PolicyAnalysisAgent.java        # Main analysis engine
│   │   │   │   └── tool/
│   │   │   │       ├── AgentTool.java                  # Tool interface
│   │   │   │       ├── ToolRegistry.java               # Tool registry
│   │   │   │       └── RiskScoringTool.java            # Risk scoring implementation
│   │   │   ├── controller/
│   │   │   │   └── PolicyController.java      # REST endpoints
│   │   │   ├── llm/
│   │   │   │   ├── LlmClient.java             # LLM client interface
│   │   │   │   ├── OllamaLlmClient.java       # Ollama implementation
│   │   │   │   └── model/
│   │   │   │       ├── Conversation.java      # Conversation management
│   │   │   │       ├── Message.java           # Message model
│   │   │   │       ├── LlmRequest.java        # Request model
│   │   │   │       └── LlmResponse.java       # Response model
│   │   │   ├── domain/
│   │   │   │   └── PolicyAnalysisResult.java  # Result DTO
│   │   │   ├── config/                        # Spring configuration
│   │   │   ├── application/                   # Application logic
│   │   │   ├── document/                      # Document handling
│   │   │   └── document/              # Additional services
│   │   └── resources/
│   │       └── application.yml                # Configuration file
│   └── test/
│       └── java/com/seyhmus/ai/policyagent/   # Unit tests
└── target/                          # Compiled output
```

---

## 🧩 Core Components

### 1. PolicyAnalysisAgent

**Location:** [src/main/java/com/seyhmus/ai/policyagent/agent/engine/PolicyAnalysisAgent.java](src/main/java/com/seyhmus/ai/policyagent/agent/engine/PolicyAnalysisAgent.java)

Main orchestration engine that:
- Constructs system prompts for policy analysis
- Manages conversation flow
- Parses LLM responses
- Validates and returns structured results

**Key Method:**
```java
public PolicyAnalysisResult analyze(String policyText)
```

### 2. OllamaLlmClient

**Location:** [src/main/java/com/seyhmus/ai/policyagent/llm/OllamaLlmClient.java](src/main/java/com/seyhmus/ai/policyagent/llm/OllamaLlmClient.java)

HTTP client for Ollama integration:
- Constructs requests to Ollama API
- Handles network communication with retry logic
- Parses JSON responses
- Manages HTTP timeouts (180s read timeout)

**Features:**
- Automatic socket exception handling
- Retry mechanism for transient failures
- Configurable connection/read/write timeouts

### 3. ToolRegistry

**Location:** [src/main/java/com/seyhmus/ai/policyagent/agent/tool/ToolRegistry.java](src/main/java/com/seyhmus/ai/policyagent/agent/tool/ToolRegistry.java)

Plugin registry system for extending analysis capabilities:
- Auto-discovers `AgentTool` implementations
- Provides tool lookup by name
- Enables extensible analysis framework

### 4. RiskScoringTool

**Location:** [src/main/java/com/seyhmus/ai/policyagent/agent/tool/RiskScoringTool.java](src/main/java/com/seyhmus/ai/policyagent/agent/tool/RiskScoringTool.java)

Example tool implementation:
- Calculates risk scores based on clause analysis
- Detects Turkish language risk indicators (e.g., "hariç" = excluded)
- Extensible scoring algorithm

**Scoring Rules:**
- Base score: 10
- Contains "hariç" (excluded): +30
- Contains "%25" (25%): +20

### 5. PolicyController

**Location:** [src/main/java/com/seyhmus/ai/policyagent/controller/PolicyController.java](src/main/java/com/seyhmus/ai/policyagent/controller/PolicyController.java)

REST API entry point:
- Exposes `/api/policies/analyze` endpoint
- Delegates to PolicyAnalysisAgent
- Handles HTTP requests/responses

### 6. Data Models

**PolicyAnalysisResult:**
```java
{
  int riskScore;
  List<String> highRiskClauses;
  boolean fraudSuspicion;
  String summary;
}
```

**Conversation:**
- Multi-turn conversation support
- Message role management (system, user, assistant, tool)
- OpenAI-compatible message format

---

## 🔧 Development

### Building from Source

```bash
# Full clean build
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Build with specific Java version
mvn clean install -Djava.version=21
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PolicyControllerTest

# Run with coverage
mvn test jacoco:report
```

### IDE Setup

**IntelliJ IDEA:**
1. Open project as Maven project
2. Enable annotation processing for Lombok
3. Configure Java 21 as project SDK

**VSCode:**
1. Install Extension Pack for Java
2. Install Spring Boot Extension Pack
3. Add to `.vscode/settings.json`:
```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-21",
      "path": "/path/to/jdk-21"
    }
  ]
}
```

### Code Style

Project follows standard Java conventions:
- 4-space indentation
- Google Java Style Guide
- Lombok for boilerplate reduction

### Adding New Tools

1. Implement `AgentTool` interface:
```java
@Component
public class MyCustomTool implements AgentTool {
    
    @Override
    public String getName() {
        return "my_tool";
    }
    
    @Override
    public String getDescription() {
        return "Tool description";
    }
    
    @Override
    public Map<String, Object> getJsonSchema() {
        return Map.of(...);
    }
    
    @Override
    public Map<String, Object> execute(Map<String, Object> input) {
        // Implementation
        return Map.of(...);
    }
}
```

2. Tool auto-registers via Spring component scanning
3. Access via `ToolRegistry.getTool("my_tool")`

---

## 🐳 Docker Support

### Build Docker Image

```dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src src
RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/policy-agent-0.0.1-SNAPSHOT.jar"]
```

**Build:**
```bash
docker build -t policy-agent:latest .
```

**Run:**
```bash
docker run -p 8080:8080 \
  -e OLLAMA_BASE_URL=http://host.docker.internal:11434 \
  policy-agent:latest
```

### Docker Compose

```yaml
version: '3.8'
services:
  ollama:
    image: ollama/ollama:latest
    ports:
      - "11434:11434"
    volumes:
      - ollama_data:/root/.ollama

  policy-agent:
    build: .
    ports:
      - "8080:8080"
    environment:
      OLLAMA_BASE_URL: http://ollama:11434
    depends_on:
      - ollama

volumes:
  ollama_data:
```

**Start services:**
```bash
docker-compose up
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Workflow

1. Ensure tests pass: `mvn test`
2. Follow code style conventions
3. Update README for significant changes
4. Add tests for new features

---

## 📊 Performance Considerations

### Response Times

- **Typical Analysis:** 3-10 seconds (depends on policy length and Ollama model)
- **Model Load Time:** 2-5 seconds (first request)
- **Timeout:** 180 seconds total

### Optimization Tips

1. **Run Ollama on GPU** for 2-5x faster response times
2. **Use smaller models** (neural-chat, mistral) for faster processing
3. **Enable caching** for similar policies
4. **Batch multiple requests** for efficiency
5. **Use connection pooling** in client applications

### Resource Requirements

| Component | CPU | Memory | Disk |
|-----------|-----|--------|------|
| Spring Boot App | 1-2 cores | 512MB | 500MB |
| Ollama + Model | 2-8 cores | 4-8GB | 4-8GB |
| **Total** | **4-10 cores** | **5-9GB** | **5-9GB** |

---

## 🔒 Security Considerations

- Policy documents are processed locally; no external data transmission
- Input validation prevents injection attacks
- JSON response escaping prevents XSS
- Configurable Ollama authentication (if needed)

---

## 📝 Logging

Adjust logging level in `application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.seyhmus.ai: DEBUG
    okhttp3: DEBUG  # For HTTP request/response logging
```

View logs:
```bash
tail -f logs/application.log
```

---

## 🐛 Troubleshooting

### Issue: "Connection refused" to Ollama

**Solution:**
```bash
# Ensure Ollama is running
ollama serve

# Check if accessible
curl http://127.0.0.1:11434/api/tags

# Verify configuration in application.yml
ollama:
  base-url: http://127.0.0.1:11434
```

### Issue: "Empty content from LLM"

**Solution:**
1. Verify Ollama model is properly loaded: `ollama pull llama3`
2. Check network connectivity
3. Increase timeout in `OllamaLlmClient`
4. Check Ollama server logs

### Issue: "Invalid JSON response"

**Solution:**
1. Verify policy text is valid
2. Check LLM response in logs
3. Try with a shorter policy text
4. Verify using `curl`:
```bash
curl -X POST http://127.0.0.1:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{"model": "llama3", "prompt": "test", "stream": false}'
```

### Issue: Slow Response Times

**Solution:**
1. Verify Ollama isn't processing another request
2. Use GPU acceleration for Ollama
3. Switch to smaller model (mistral, neural-chat)
4. Monitor system resources: `htop` or Task Manager

---

## 📚 Resources

- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **Ollama Documentation:** https://github.com/ollama/ollama
- **Llama 3 Model:** https://ollama.ai/library/llama3
- **OkHttp Client:** https://square.github.io/okhttp/

---

## 📄 License

This project is licensed under the MIT License. See LICENSE file for details.

---

## 👨‍💻 Author

**Şeyhmus Öztürk**

- GitHub: [@seyhmusOzturk](https://github.com/seyhmusOzturk)

---

## 🙏 Acknowledgments

- Spring Boot community for excellent framework
- Ollama team for local LLM capabilities
- Meta for Llama 3 model

---

**Last Updated:** March 2, 2026  
**Status:** ✅ Active Development

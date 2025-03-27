# Healthcare gRPC Microservices
**GitHub Repository**: [healthcare-grpcMicroservices](https://github.com/SAMKE-glitch/healthcare-grpcMicorservices)  


---

## **Overview**
This project demonstrates a **high-performance microservices architecture** for a healthcare system, built using **gRPC**, **Java**, and **Spring Boot**. It showcases efficient communication between distributed services, leveraging Protocol Buffers for serialization and HTTP/2 for transport. Designed as a learning sample, it highlights:
- **gRPC** for type-safe, low-latency service-to-service communication.
- **Java 17** and **Spring Boot** for backend development and dependency injection.
- **PostgreSQL** for persistent data storage, managed via **Docker** for simplicity.
- **Maven** for multi-module project management and code generation.

### **Key Features**
- **Unary, Streaming, and Bidirectional RPCs**:
    - Patient registration (Unary).
    - Real-time appointment availability streaming (Server Streaming).
    - Doctor-patient chat (Bidirectional Streaming).
- **Strong API Contracts**: Enforced via `.proto` files.
- **Scalability**: Containerized database and stateless services.

---
---

## 1. Prerequisites
- **Java 21+** (OpenJDK or Oracle JDK).
- **Maven 3.8+** (Build tool).
- **Docker & Docker Compose** (For PostgreSQL and pgAdmin).
- **gRPC Tools**:
    - **grpcurl** (CLI testing): [Installation Guide](https://github.com/fullstorydev/grpcurl)
    - **grpcui** (Web UI): [Installation Guide](https://github.com/fullstorydev/grpcui)
    - **postman** 
---

---
## 2. Project Setup
### Clone the Repository
```bash  
git clone https://github.com/SAMKE-glitch/healthcare-grpcMicorservices.git  
cd healthcare-grpcMicorservices 

### Project Structure
```bash 
healthcare-grpcMicorservices/  
├── common-proto/          # Shared Protocol Buffers definitions  
├── patient-service/       # Patient registration and management  
├── doctor-service/        # Doctor data management  
├── appointment-service/   # Appointment booking and streaming  
└── compose.yml     # PostgreSQL and pgAdmin setup  
```
---

---
## 3. Build the Project
### Generate Protobuf Code:
        ```bash
        cd common-proto  
        mvn clean install       # Generates Java classes from .proto files  
        ```

### Build All Modules:
        ```
        cd ..  
        mvn clean install       # Builds patient, doctor, and appointment services  
        ```
---

## 4. Database Setup

### 1. Start PostgreSQL and pgAdmin:
    ```
    docker-compose up -d    # Launches PostgreSQL (port 5432) and pgAdmin (port 8081)  
    ```

### 2. Access pgAdmin:
Open http://localhost:8081/browser/ in your browser.

Login with admin@example.com / password:admin.

Connect to PostgreSQL:
    - Host: postgres (Docker service name)
    
    - Port: 5432
    
    - Database: healthcare-app-db
    
    - Username/Password: admin / admin
---
## 5. Run the Services
Run all the 3 services for testing.

Refer to this [Schema.sql](https://github.com/SAMKE-glitch/healthcare-grpcMicorservices/blob/main/schema.sql) to create Tables on your databases
Refer to this [file](https://github.com/SAMKE-glitch/healthcare-grpcMicorservices/blob/main/TestingWalkThrough.md) when running queries either on your grpcurl for CLI Testing, or POSTMAN, and grpcui for WebUI Testing,
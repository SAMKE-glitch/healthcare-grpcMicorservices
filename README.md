# Healthcare gRPC Microservices
**GitHub Repository**: [healthcare-grpcMicroservices](https://github.com/SAMKE-glitch/healthcare-grpcMicorservices)  

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
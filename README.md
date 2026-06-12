# WorkforceIQ

WorkforceIQ is a full-stack **HR analytics and workforce management platform**. It helps organizations manage employees and departments, track hiring and attrition, analyze salary and gender pay gaps, and generate AI-powered workforce insights.

---

## Features

### Employee Management
- Add, update, view, and remove employees
- Auto-generated secure passwords sent by email on onboarding
- Department slot tracking (capacity limits per department)
- Removed employees archived for monthly attrition reporting
- Automatic years-of-experience increment on hire-date anniversaries (scheduled job)

### Department Management
- Create, update, and delete departments
- View department headcount and available hiring slots
- Browse employees within each department

### Analytics Dashboard
- Hiring statistics by date range (total hires, gender breakdown, per-department counts)
- Salary gap analysis (between departments, within departments, gender pay gap)
- Monthly fired/removed employee count
- AI-generated narrative summaries for salary insights

### AI Workforce Insights
- Department health metrics: headcount, gender ratio, salary health index, pay equity flags
- Company-wide comparisons (lowest female representation, lowest average salary)
- Executive summary powered by **Groq LLM** (`llama-3.1-8b-instant`)

### Promotions (HR only)
- Eligibility rules: 3+ years experience, 2+ years since last promotion increment, salary below department average
- Record promotion history (old/new role and salary)

### Authentication & Authorization
- JWT-based stateless login (7-day token expiry)
- Role-based access:
  - **HR** — full access (add/update/delete employees, manage departments, promotions)
  - **Other roles** — read access to dashboard, departments, and AI insights

---

## Tech Stack

| Layer | Technologies |
|-------|--------------|
| **Backend** | Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA, JWT (jjwt), Spring Mail |
| **Frontend** | React 19, Vite 8, React Router 7, Axios |
| **Database** | MySQL |
| **AI** | Groq API (OpenAI-compatible chat completions) |
| **Build** | Maven (backend), npm (frontend) |

---

## Project Structure

```
WorkforceIQ/
├── WorkforceIQ/                          # Spring Boot backend
│   ├── src/main/java/com/example/WorkforceIQ/
│   │   ├── Controller/                   # REST API endpoints
│   │   ├── Service/                      # Business logic
│   │   ├── Repository/                   # JPA repositories
│   │   ├── entity/                       # JPA entities
│   │   ├── dto/                          # Data transfer objects
│   │   ├── config/                       # Security, CORS
│   │   └── security/                     # JWT filter & service
│   ├── src/main/resources/
│   │   └── application.properties        # Local config (not in git)
│   └── pom.xml
│
└── workforceIQ_frontend/                 # React frontend
    ├── src/
    │   ├── Pages/                        # Route pages
    │   ├── components/                   # Layout, route guards
    │   ├── api/                          # Axios setup & base URL
    │   └── auth/                         # Session & role helpers
    └── package.json
```

---

## Prerequisites

- **Java 17+**
- **Maven 3.6+** (or use included `./mvnw`)
- **Node.js 18+** and **npm**
- **MySQL** database server
- **Groq API key** (for AI analysis features)
- **SMTP credentials** (for sending new employee login emails)

---

## Configuration

Create `WorkforceIQ/src/main/resources/application.properties` (this file is gitignored):

```properties
# Server
server.port=8086

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/workforceiq
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Groq AI
groq.api.key=your_groq_api_key

# Email (example: Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Create the MySQL database before starting the backend:

```sql
CREATE DATABASE workforceiq;
```

---

## Running the Application

### 1. Start the backend

```bash
cd WorkforceIQ
./mvnw spring-boot:run
```

On Windows:

```bash
cd WorkforceIQ
mvnw.cmd spring-boot:run
```

Backend runs at **http://localhost:8086**

### 2. Start the frontend

```bash
cd WorkforceIQ/workforceIQ_frontend
npm install
npm run dev
```

Frontend runs at **http://localhost:5173** (or the next available Vite port, e.g. `5174`).

> **Note:** The frontend API base URL is set in `workforceIQ_frontend/src/api/config.js` as `http://localhost:8086`. CORS is configured for `http://localhost:5173` and `http://localhost:5174`.

---

## API Overview

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `GET` | `/employee/login` | Public | Login with email & password |
| `GET` | `/employee` | Authenticated | List all employees |
| `GET` | `/employee/{id}` | Authenticated | Get employee by ID |
| `POST` | `/employee` | HR | Add employee |
| `PUT` | `/employee/{id}` | HR | Update employee |
| `DELETE` | `/employee/{id}` | HR | Remove employee |
| `GET` | `/employee/monthly-fired` | Authenticated | Monthly removal count |
| `GET` | `/employee/roles` | Authenticated | List available roles |
| `GET` | `/departments` | Authenticated | List departments |
| `GET` | `/departments/available` | Authenticated | Departments with open slots |
| `POST` | `/department` | HR | Create department |
| `PUT` | `/department/{id}` | HR | Update department |
| `DELETE` | `/department/{id}` | HR | Delete department |
| `GET` | `/analytics/hiring` | Authenticated | Hiring stats (`startDate`, `endDate`) |
| `GET` | `/analytics/salary-gap` | Authenticated | Salary gap analysis |
| `GET` | `/ai/departments` | Authenticated | AI department health analysis |
| `GET` | `/promotion/eligible` | HR | List promotion-eligible employees |
| `PUT` | `/promotion/{id}` | HR | Promote employee |

Authenticated requests require the JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

---

## Frontend Routes

| Route | Access | Page |
|-------|--------|------|
| `/` | Public | Login |
| `/home` | Authenticated | Analytics dashboard |
| `/department` | Authenticated | Department list |
| `/department/:id` | Authenticated | Employees in department |
| `/addemployee` | HR | Add employee |
| `/update/:id` | HR | Update employee |
| `/ai-analysis` | Authenticated | AI workforce insights |
| `/promotion` | HR | Employee promotions |

---

## Employee Roles

Supported roles include: `HR`, `EMPLOYEE`, `MANAGER`, `TEAM_LEAD`, `SENIOR_EMPLOYEE`, `JUNIOR_EMPLOYEE`, `INTERN`, `CONTRACTOR`, `DIRECTOR`, `VP`, `CEO`, `ADMIN`.

Only users with the **HR** role can perform write operations on employees, departments, and promotions.

---

## Promotion Eligibility

An employee is eligible for promotion when all of the following are true:

1. **3+ years** of experience
2. **2+ years** since last experience increment year (or never incremented)
3. Assigned to a department
4. Current salary is **below** the department average salary

---

## Building for Production

**Backend:**

```bash
cd WorkforceIQ
./mvnw clean package
java -jar target/WorkforceIQ-0.0.1-SNAPSHOT.jar
```

**Frontend:**

```bash
cd WorkforceIQ/workforceIQ_frontend
npm run build
npm run preview
```

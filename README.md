# 🏥 Electronic Medical Records System

![Java](https://img.shields.io/badge/Java-21-orange?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.5-brightgreen?logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-green?logo=thymeleaf)
![JWT](https://img.shields.io/badge/JWT-0.12.6-black?logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?logo=apachemaven)
![License](https://img.shields.io/badge/License-Academic-lightgrey)

A full-stack web application for managing electronic medical records, built with Spring Boot. The system supports three user roles — **Admin**, **Doctor**, and **Patient** — each with role-specific access to medical data.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [SOLID Principles](#solid-principles)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Database Schema](#database-schema)
- [Statistics & Reports](#statistics--reports)
- [Role-Based Access](#role-based-access)
- [Known Issues & TODOs](#known-issues--todos)
- [Best Practices Applied](#best-practices-applied)

---

## Overview

This application provides a complete solution for maintaining electronic patient medical records. It supports full CRUD operations for doctors, patients, diagnoses, visits and sick leaves, along with comprehensive statistics and reporting.

The system is built as a **RESTful web service** with a **Thymeleaf-based UI**, supporting both browser-based session authentication and JWT-based API authentication simultaneously.

---

## Features

### Core Functionality
- Full CRUD for Doctors, Patients, Diagnoses, Visits and Sick Leaves
- Patient self-registration with GP selection
- Admin-managed doctor accounts
- Automatic NHIF/patient payment determination based on insurance status
- Sick leave management linked to specific visits

### Statistics & Reports
- Most common diagnosis
- Diagnosis frequency ranking
- Patients by diagnosis
- Patients by GP
- Total amount paid by patients
- Revenue per doctor
- Visit count per doctor
- Patient count per GP
- Patient visit history
- Visits by doctor and/or period
- Month with most sick leaves issued
- Doctor(s) with most sick leaves issued

### Security
- JWT authentication for REST API
- Session-based authentication for browser UI
- BCrypt password hashing
- Role-based access control (Admin, Doctor, Patient)
- Automatic admin account seeding on startup

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.14 |
| Security | Spring Security 6.5 + JWT (jjwt 0.12.6) |
| Persistence | Spring Data JPA + Hibernate 6.6 |
| Database | PostgreSQL 16 |
| Connection Pool | HikariCP |
| Template Engine | Thymeleaf + Thymeleaf Security Extras |
| Build Tool | Maven |
| Utilities | Lombok |
| Validation | Jakarta Bean Validation |
| Version Control | Git + GitHub |

---

## Architecture

The application follows a strict **4-layer architecture** (Separation of Concerns):

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│   REST Controllers + Web Controllers    │
│   + Thymeleaf Templates                 │
└─────────────────┬───────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│           SERVICE LAYER                 │
│       Business Logic & Rules            │
└─────────────────┬───────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         PERSISTENCE LAYER               │
│      Spring Data JPA Repositories       │
└─────────────────┬───────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          DATABASE LAYER                 │
│            PostgreSQL                   │
└─────────────────────────────────────────┘
```

**Key principles applied:**
- Controllers are dumb — zero business logic
- Services contain all business rules
- Repositories only handle data access
- DTOs separate internal entities from external API

---

## SOLID Principles

The project consciously applies all five SOLID principles throughout the codebase.

### ✅ Single Responsibility Principle (SRP)
Every class has exactly one reason to change:

```
DoctorController   → only handles HTTP requests/responses for doctors
DoctorService      → only contains doctor business rules
DoctorRepository   → only handles doctor database operations
EntityMapper       → only converts between entities and DTOs
GlobalExceptionHandler → only handles exceptions from all controllers
AdminSeeder        → only creates the initial admin account
```

### ✅ Open/Closed Principle (OCP)
The system is open for extension, closed for modification:

- New statistics can be added to `StatisticsService` without modifying existing methods
- New roles can be added to the `Role` enum without changing security infrastructure
- New exception types can be added to `GlobalExceptionHandler` without modifying existing handlers
- New endpoints can be added to controllers without touching existing ones

### ✅ Liskov Substitution Principle (LSP)
Spring interfaces are used throughout — implementations are interchangeable:

- `UserDetailsService` is implemented by `UserDetailsServiceImpl` — Spring Security only depends on the interface
- `PasswordEncoder` is defined as a `@Bean` returning `BCryptPasswordEncoder` — any `PasswordEncoder` implementation could replace it
- All repositories extend `JpaRepository` — Spring generates the implementation at runtime

### ✅ Interface Segregation Principle (ISP)
Spring Data JPA repositories define only the methods they need:

```java
// DoctorRepository only exposes what's needed for doctors:
boolean existsByUin(String uin);
// Not bloated with unrelated methods

// UserRepository only exposes what's needed for users:
Optional<User> findByUsername(String username);
boolean existsByUsername(String username);
```

### ✅ Dependency Inversion Principle (DIP)
High-level modules depend on abstractions, not concrete implementations:

```java
// DoctorService depends on abstractions — not concrete classes:
private final DoctorRepository doctorRepository;    // interface
private final UserRepository userRepository;         // interface
private final PasswordEncoder passwordEncoder;       // interface
// Spring injects the concrete implementations at runtime
```

Constructor injection is used throughout via `@RequiredArgsConstructor` — dependencies are never instantiated with `new` inside classes. The IoC container manages all object creation and wiring.

---

## Project Structure

```
src/main/java/com/medicalrecords/medical_records/
├── config/
│   ├── ApplicationConfig.java      # Beans: PasswordEncoder, AuthenticationProvider
│   ├── SecurityConfig.java         # Security filter chain, JWT + session config
│   └── AdminSeeder.java            # Auto-creates admin on startup
├── controller/
│   ├── AuthController.java         # REST: /api/auth/login, /api/auth/register
│   ├── DoctorController.java       # REST: /api/doctors
│   ├── PatientController.java      # REST: /api/patients
│   ├── VisitController.java        # REST: /api/visits
│   ├── DiagnosisController.java    # REST: /api/diagnoses
│   ├── SickLeaveController.java    # REST: /api/sick-leaves
│   ├── StatisticsController.java   # REST: /api/statistics
│   └── web/
│       ├── AuthWebController.java          # Web: /login, /register
│       ├── DashboardWebController.java     # Web: /dashboard
│       ├── DoctorWebController.java        # Web: /doctors
│       ├── PatientWebController.java       # Web: /patients
│       ├── VisitWebController.java         # Web: /visits
│       ├── DiagnosisWebController.java     # Web: /diagnoses
│       ├── SickLeaveWebController.java     # Web: /sick-leaves
│       ├── StatisticsWebController.java    # Web: /statistics
│       └── AdminWebController.java         # Web: /admin
├── dto/
│   ├── request/                    # Incoming data: CreateDoctorRequest etc.
│   └── response/                   # Outgoing data: DoctorResponse etc.
├── entity/
│   ├── Doctor.java
│   ├── Patient.java
│   ├── Visit.java
│   ├── Diagnosis.java
│   ├── SickLeave.java
│   ├── User.java                   # Implements UserDetails
│   └── Role.java                   # Enum: ADMIN, DOCTOR, PATIENT
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java # @RestControllerAdvice
├── mapper/
│   └── EntityMapper.java           # Entity ↔ DTO conversion
├── repository/
│   ├── DoctorRepository.java
│   ├── PatientRepository.java
│   ├── VisitRepository.java
│   ├── DiagnosisRepository.java
│   ├── SickLeaveRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtService.java             # Token generation and validation
│   ├── JwtAuthFilter.java          # OncePerRequestFilter for JWT
│   └── UserDetailsServiceImpl.java # Loads user from DB for Spring Security
└── service/
    ├── AuthService.java
    ├── DoctorService.java
    ├── PatientService.java
    ├── VisitService.java
    ├── DiagnosisService.java
    ├── SickLeaveService.java
    └── StatisticsService.java

src/main/resources/
├── templates/
│   ├── layout.html                 # Base navbar fragment
│   ├── dashboard.html
│   ├── auth/
│   │   ├── login.html
│   │   └── register.html
│   ├── doctors/
│   │   ├── list.html
│   │   └── form.html
│   ├── patients/
│   │   ├── list.html
│   │   └── form.html
│   ├── visits/
│   │   ├── list.html
│   │   └── form.html
│   ├── diagnoses/
│   │   ├── list.html
│   │   └── form.html
│   ├── sick-leaves/
│   │   ├── list.html
│   │   └── form.html
│   ├── statistics/
│   │   └── dashboard.html
│   ├── admin/
│   │   └── panel.html
│   └── error.html
├── static/
│   └── css/
│       └── style.css
└── application.properties
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 16+
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/MaximSimeonovIvanov/medical-records.git
cd medical-records
```

### 2. Create the Database

```bash
sudo -u postgres psql -c "CREATE DATABASE medical_records;"
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'yourpassword';"
```

### 3. Configure `application.properties`

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_records
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or run `MedicalRecordsApplication.java` from your IDE.

### 5. Access the Application

| URL | Description |
|---|---|
| `http://localhost:8080/login` | Browser UI login page |
| `http://localhost:8080/dashboard` | Main dashboard |
| `http://localhost:8080/admin` | Admin panel |

### Default Admin Credentials

```
Username: admin
Password: admin123
```

The admin account is automatically created on first startup via `AdminSeeder`.

---

## Configuration

### `application.properties`

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_records
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

# JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

# Error handling
spring.mvc.problemdetails.enabled=false
server.error.include-message=always
```

> ⚠️ **Security Note:** In production, never commit the JWT secret to version control. Use environment variables instead.

---

## API Endpoints

### Authentication (Public)

| Method | URL | Description |
|---|---|---|
| POST | `/api/auth/login` | Login, returns JWT token |
| POST | `/api/auth/register` | Patient self-registration |

### Doctors

| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/doctors` | Any role | Get all doctors |
| GET | `/api/doctors/{id}` | Any role | Get doctor by ID |
| GET | `/api/doctors/gps` | Any role | Get all GPs |
| POST | `/api/doctors` | Admin | Create doctor |
| PUT | `/api/doctors/{id}` | Admin | Update doctor |
| DELETE | `/api/doctors/{id}` | Admin | Delete doctor |

### Patients

| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/patients` | Admin, Doctor | Get all patients |
| GET | `/api/patients/{id}` | Admin, Doctor | Get patient by ID |
| GET | `/api/patients/by-gp/{gpId}` | Admin, Doctor | Patients by GP |
| POST | `/api/patients` | Admin | Create patient |
| PUT | `/api/patients/{id}` | Admin | Update patient |
| DELETE | `/api/patients/{id}` | Admin | Delete patient |

### Visits

| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/visits` | Any role | Get all visits |
| GET | `/api/visits/{id}` | Any role | Get visit by ID |
| GET | `/api/visits/patient/{id}` | Any role | Visits by patient |
| GET | `/api/visits/doctor/{id}` | Any role | Visits by doctor |
| GET | `/api/visits/doctor/{id}/period` | Any role | Visits by doctor and period |
| POST | `/api/visits` | Admin, Doctor | Create visit |
| PUT | `/api/visits/{id}` | Admin, Doctor | Update visit |
| DELETE | `/api/visits/{id}` | Admin | Delete visit |

### Diagnoses

| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/diagnoses` | Any role | Get all diagnoses |
| GET | `/api/diagnoses/{id}` | Any role | Get diagnosis by ID |
| POST | `/api/diagnoses` | Admin, Doctor | Create diagnosis |
| PUT | `/api/diagnoses/{id}` | Admin, Doctor | Update diagnosis |
| DELETE | `/api/diagnoses/{id}` | Admin, Doctor | Delete diagnosis |

### Sick Leaves

| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/sick-leaves` | Any role | Get all sick leaves |
| GET | `/api/sick-leaves/{id}` | Any role | Get sick leave by ID |
| GET | `/api/sick-leaves/patient/{id}` | Any role | Sick leaves by patient |
| POST | `/api/sick-leaves` | Admin, Doctor | Create sick leave |
| PUT | `/api/sick-leaves/{id}` | Admin, Doctor | Update sick leave |
| DELETE | `/api/sick-leaves/{id}` | Admin | Delete sick leave |

### Statistics

| Method | URL | Description |
|---|---|---|
| GET | `/api/statistics/diagnoses/most-common` | Most common diagnosis |
| GET | `/api/statistics/diagnoses/frequency` | Diagnosis frequency list |
| GET | `/api/statistics/patients/by-diagnosis/{id}` | Patients with given diagnosis |
| GET | `/api/statistics/patients/by-gp/{gpId}` | Patients by GP |
| GET | `/api/statistics/patients/count-per-gp` | Patient count per GP |
| GET | `/api/statistics/visits/total-paid-by-patients` | Total paid by patients |
| GET | `/api/statistics/visits/paid-by-patients-per-doctor` | Revenue per doctor |
| GET | `/api/statistics/visits/count-per-doctor` | Visit count per doctor |
| GET | `/api/statistics/visits/patient-history/{id}` | Patient visit history |
| GET | `/api/statistics/visits/by-doctor-and-period` | Visits by doctor/period |
| GET | `/api/statistics/sick-leaves/month-most-issued` | Month with most sick leaves |
| GET | `/api/statistics/sick-leaves/doctor-most-issued` | Doctor(s) with most sick leaves |

### Using the REST API with JWT

All protected endpoints require the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
```

**Login to get a token:**
```json
POST /api/auth/login
{
    "username": "admin",
    "password": "admin123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "role": "ROLE_ADMIN",
    "username": "admin"
}
```

---

## Security

### Authentication Flow

```
Browser (Thymeleaf)          REST API (Postman)
        |                           |
  POST /login                POST /api/auth/login
  (form submission)          (JSON body)
        |                           |
  Spring Security            AuthService.login()
  UsernamePasswordFilter     authenticationManager
        |                           |
  DaoAuthenticationProvider ←-------+
  loadUserByUsername()
  BCrypt.matches()
        |                           |
  Session created            JWT token generated
  JSESSIONID cookie          Token returned in response
        |                           |
  Browser stores cookie      Client stores token
  Sends automatically        Sends in Authorization header
```

### Password Security

All passwords are hashed using BCrypt before storage:
```java
passwordEncoder.encode(rawPassword)
// "admin123" → "$2a$10$N9qo8uLOickgx2ZMRZoMye..."
```

Verification uses `matches()` — passwords are never decrypted.

### JWT Token Structure

```
Header.Payload.Signature
```

Payload contains:
- `sub` — username
- `role` — user role (ROLE_ADMIN, ROLE_DOCTOR, ROLE_PATIENT)
- `iat` — issued at timestamp
- `exp` — expiration timestamp (24 hours)

---

## Database Schema

### Entity Relationships

```
users ──────────── doctors (OneToOne)
users ──────────── patients (OneToOne)
patients ────────── doctors/GP (ManyToOne) → gp_id
visits ──────────── doctors (ManyToOne) → doctor_id
visits ──────────── patients (ManyToOne) → patient_id
visits ──────────── diagnoses (ManyToOne) → diagnosis_id
sick_leaves ──────── visits (OneToOne) → visit_id
```

### Tables

| Table | Key Columns |
|---|---|
| `doctors` | id, uin (unique), name, specialty, is_gp |
| `patients` | id, egn (unique), name, health_insured, gp_id |
| `visits` | id, date, doctor_id, patient_id, diagnosis_id, price, paid_by_nhif |
| `diagnoses` | id, name (unique), description |
| `sick_leaves` | id, start_date, number_of_days, visit_id (unique) |
| `users` | id, username (unique), password, role, doctor_id, patient_id |

---

## Statistics & Reports

All statistics are accessible via:
- REST API: `/api/statistics/**`
- Browser UI: `http://localhost:8080/statistics`

### NHIF vs Patient Payment

The system automatically determines who pays for each visit:

```java
boolean paidByNhif = patient.isHealthInsured();
// true  → NHIF (Национална здравноосигурителна каса) pays
// false → Patient pays out of pocket
```

`healthInsured` is set by admin — new patients default to `false` on self-registration.

---

## Role-Based Access

### Admin
- Full access to all data
- Creates and manages doctor accounts
- Creates, updates and deletes all entities
- Access to admin panel and all statistics
- User management

### Doctor
- Views all patients and their complete visit history
- Creates visits for any patient (automatically assigned to themselves)
- Edits only their own visits
- Creates and manages sick leaves for their own visits
- Access to statistics

### Patient
- Views only their own visits and sick leaves
- Views doctors list and diagnoses (read-only)
- Self-registers with GP selection
- No access to statistics or admin panel

### Access Control Layers

**Layer 1 — URL level** (`SecurityConfig.java`):
```java
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/statistics/**").hasAnyRole("ADMIN", "DOCTOR")
```

**Layer 2 — Data level** (Controllers):
```java
if (currentUser.getRole() == DOCTOR) {
    return visitService.getAllVisits(); // sees all
} else if (currentUser.getRole() == PATIENT) {
    return visitService.getVisitsByPatient(currentUser.getPatient().getId());
}
```

**Layer 3 — UI level** (Thymeleaf):
```html
<a sec:authorize="hasRole('ADMIN')" th:href="@{/admin}">Admin</a>
<button th:if="${visit.doctorName == #authentication.principal.doctor?.name}">
    Edit
</button>
```

---

## Known Issues & TODOs

### 🐛 Known Bugs

- **Delete Diagnosis with linked visits** — attempting to delete a diagnosis that has visits referencing it returns a 500 error due to PostgreSQL foreign key constraint. Fix: set `diagnosis = null` on all linked visits before deleting.

- **Edit form pre-filling** — visit edit form doesn't pre-populate existing values. User must re-enter all fields when editing.

### 📝 TODO

- [ ] Fix `deleteDiagnosis` — set `diagnosis = null` on linked visits before deleting
- [ ] Pre-fill visit edit form with existing values (`th:value` on all fields)
- [ ] Optimize `deleteDoctor` and `deletePatient` — replace `findAll()` + Java filter with `findByVisitDoctorId()` and `findByVisitPatientId()`
- [ ] Add `@PreAuthorize` on service methods for method-level security
- [ ] Add service-level check in `updateVisit` — verify doctor owns the visit
- [ ] Replace `System.out.println` in `AdminSeeder` with proper SLF4J logger
- [ ] Add pagination to list endpoints for large datasets
- [ ] Add audit fields (`createdAt`, `updatedAt`) to entities using `@CreatedDate`
- [ ] Move JWT secret to environment variable instead of `application.properties`
- [ ] Add comprehensive unit and integration tests (Phase 10)
- [ ] Add duplicate sick leave check — prevent two sick leaves for the same visit
- [ ] Add Swagger/OpenAPI documentation

### ⚡ Performance Improvements

- [ ] Replace `sickLeaveRepository.findAll()` in delete methods with targeted queries
- [ ] Add database indexes on frequently queried columns (EGN, UIN, username)
- [ ] Consider adding second-level Hibernate cache for diagnoses (rarely changes)

---

## Best Practices Applied

### Architecture
- ✅ Strict layered architecture — controllers call services, services call repositories
- ✅ DTO pattern — entities never exposed directly in API responses
- ✅ Constructor injection via `@RequiredArgsConstructor` — not field injection
- ✅ `private final` dependencies — immutable after injection

### Security
- ✅ BCrypt password hashing — never plain text
- ✅ JWT stateless authentication for REST API
- ✅ Session-based authentication for browser UI
- ✅ Role-based access control at URL, data and UI levels
- ✅ Generic error messages for failed login — no information leakage
- ✅ CSRF disabled for REST API (JWT immune to CSRF)

### Database
- ✅ Two-layer validation — DTO level (`@NotBlank`) and DB level (`nullable=false`)
- ✅ `@Transactional` on all write operations — atomicity guaranteed
- ✅ Correct cascade deletion order — children before parents
- ✅ `BigDecimal` for financial values — no floating point errors
- ✅ `EnumType.STRING` for role storage — safe against enum reordering

### Code Quality
- ✅ Custom exceptions with meaningful names and HTTP mappings
- ✅ `GlobalExceptionHandler` — centralized error handling
- ✅ `Optional.orElseThrow()` — no NullPointerExceptions
- ✅ Idempotent admin seeding — safe to restart multiple times
- ✅ Meaningful commit messages
- ✅ Code comments in methods explaining business logic

### REST API
- ✅ Correct HTTP status codes (200, 201, 204, 400, 401, 403, 404, 409)
- ✅ `@Valid` on all request bodies — validation before business logic
- ✅ POST → Redirect → GET pattern in web controllers
- ✅ Separate REST and Web controllers — clean separation of concerns

---

## Course Information

**Course:** CSCB869 Java Web Services

**Topics covered:**
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf Template Engine
- RESTful Web Services
- JWT Authentication
- Layered Architecture

---

## 👤 Author & Contact

**Maxim Simeonov Ivanov** — Java Developer

- maksimivanov@tutamail.com

This project was developed as part of university coursework in **CSCB869 Java Web Services**, demonstrating modern Java development practices, Spring Framework expertise, clean code principles, layered architecture, and security best practices.

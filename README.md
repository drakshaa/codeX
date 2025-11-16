# 🚀 CodeX: Hackathon Management Platform

CodeX is a comprehensive, **Spring Boot (Java)** web application that manages the full lifecycle of a national-level hackathon. It handles participant registration, **payment verification**, multi-round screening (MCQ, prototype submission), finalist selection, and logistics using a robust **flag-based access control** system.

---

## 🏗️ Technical Stack Summary

| Component | Technology | Role |
| :--- | :--- | :--- |
| **Backend Core** | **Java JDK 17+ / Spring Boot** | MVC framework for business logic, services, and APIs. |
| **Data Access** | **Spring Data JPA** | Handles persistence; maps **Models** to the database. |
| **Database** | **MySQL/MariaDB** | Stores all application data (Users, Submissions, Status Flags). |
| **Frontend** | **Thymeleaf (SSR)** | Server-Side Rendering engine for dynamic HTML generation. |
| **Styling** | **Bootstrap 4/5** | Provides a modern, responsive User Interface (UI). |

---

## ⚙️ Getting Started

### Prerequisites

* **Java JDK 17+**
* **Apache Maven**
* **MySQL or MariaDB** Server

### Installation Guide

1.  **Clone:** `git clone https://github.com/drakshaa/codeX.git`
2.  **Configure DB:** Update `application.properties` with your MySQL credentials.
3.  **Build:** `mvn clean install`
4.  **Run:** `mvn spring-boot:run`

The application will start, typically accessible at `http://localhost:9090`.

### Admin Access (For Testing)

* **URL:** `/admin/login`
* **Credentials:** `Username: admin_nits` | `Password: nits@2026`

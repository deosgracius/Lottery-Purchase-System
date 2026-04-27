# Project Description — Lottery Purchase System (LPS)

## Overview

The **Lottery Purchase System (LPS)** is a web-based platform developed for the Texas Lottery Commission that enables customers to purchase and manage lottery tickets entirely online. The system eliminates the need to visit a physical retail location by providing a complete digital experience — from account registration and ticket purchasing to result checking and prize claiming.

The application is built using **Java 21** with the **Spark web framework**, uses **JSON file-based persistence** (no external database), and delivers a fully styled browser-based interface served over HTTP.

---

## Problem It Solves

Before this system, Texas Lottery customers were required to visit a retail store to purchase tickets. Long wait times and limited store hours created friction that directly impacted ticket sales. The LPS solves this by giving customers 24/7 access to the lottery from any web browser, on any device.

---

## How It Works

The system follows a classic **MVC architecture** implemented entirely in Java:

- **Model** — `User`, `Ticket`, `PurchasedTicket`, and `Order` classes store all application data. Data is persisted to JSON files on the local file system using the Gson library, eliminating the need for a database.

- **View** — Three renderer classes (`LoginRender`, `UserRender`, `AdminRender`) generate HTML pages dynamically and return them as strings to the Spark framework, which serves them to the browser.

- **Controller** — `ServerBackend` defines all HTTP routes using Spark's `get()` and `post()` methods. It wires together the managers and renderers, handling every user interaction from login to ticket purchase to prize claiming.

---

## Core Functionality

**Users** can register an account (with age verification enforced at both client and server level — Texas state law requires players to be 18 or older), complete a profile with identity verification fields, browse the four available lottery types, purchase up to 10 tickets per transaction by either picking 5 numbers manually or using auto-select, view their full order history with real-time PENDING status and winning number highlighting, display and print winning tickets for in-person claiming, and claim prizes under $600 directly through the platform.

**Administrators** land on a system status dashboard upon login that shows total tickets sold and revenue broken down by lottery type. They can add new lottery tickets, edit existing ones, toggle ticket availability, and remove discontinued tickets — all through a dedicated admin interface, with no direct file or database access required.

**Email notifications** are sent automatically using the Jakarta Mail library connected to a Gmail SMTP server. Emails go out on registration, purchase confirmation, password reset, winner notification, and prize claim confirmation. Each email is rendered as a styled HTML template.

---

## Technical Decisions

**Why Spark Java instead of Spring Boot?** Spark is lightweight and requires minimal configuration, making it appropriate for a self-contained academic project. All routing, session management, and request handling fit within a single `ServerBackend.java` file.

**Why JSON files instead of a database?** The SRS specification explicitly required text file-based storage on the Windows file system. Gson was chosen to handle serialization cleanly without requiring manual JSON parsing.

**Why inline HTML in Java?** To keep the project self-contained with no external template engine dependency. Each renderer class generates complete HTML pages using Java text blocks, with embedded CSS and JavaScript.

**Why SHA-256 + salt for passwords?** Industry-standard password hashing that can be implemented without third-party auth libraries, satisfying the in-house development requirement from the SRS.

---

## What Was Built vs. What Was Specified

The system implements all 13 functional requirements from the SRS, including the optional prize claiming flow for prizes under $600. The claiming center flow for prizes of $600 or more is acknowledged in the UI (users are directed to visit a center in person) but the IRS tax reporting integration is noted as a future extension, consistent with the SRS note that it is optional for this phase.

---

## Running the Project

1. Install Java 21 and Maven
2. Clone the repository
3. Run `mvn install` to download dependencies
4. Add your Gmail App Password to `ServerBackend.buildSmtpConfig()`
5. Run `ServerBackend.main()`
6. Visit `http://localhost:4567`

The application creates `users.json`, `tickets.json`, `orders.json`, and `email.log` automatically on first run. The four required lottery tickets (Powerball, Mega Millions, Lotto Texas, Texas Two Step) are seeded automatically if no ticket file exists.


# 🎰 Lottery Purchase System (LPS)

A full-stack web application built for the **Texas Lottery Commission** that allows customers to purchase, manage, and claim lottery tickets entirely online — no retail visit required.

Built with **Java**, **Spark Framework**, and **Gson** for CS3365 Software Engineering at Texas Tech University.

---

## 📸 Preview

| Login | Dashboard | Purchase |
|-------|-----------|----------|
| Sign in securely | View your stats | Pick numbers or auto-select |

---

## ✨ Features

### 👤 User Features
- **Account Registration** with age verification (18+ Texas state law)
- **Secure Login** with SHA-256 hashed passwords and salted credentials
- **Password Reset** via email link
- **Profile Management** — address, sex, last 4 of SSN and State ID required before purchasing
- **Browse Tickets** — Powerball, Mega Millions, Lotto Texas, Texas Two Step
- **Purchase Tickets** — pick 5 numbers (1–50) manually or auto-select, up to 10 per transaction
- **Order History** — view all past orders with PENDING status and winning number highlighting
- **Display & Print Winning Tickets** — for claiming prizes at a lottery center
- **Claim Prizes Online** — for prizes under $600 directly through the platform
- **Browse Previous Winners** — with fireworks animation 🎆

### 🔐 Admin Features
- **System Status Dashboard** — total tickets sold, revenue breakdown per ticket type
- **Manage Tickets** — add, remove, activate/deactivate lottery tickets
- **Role-based Access Control** — admin pages are fully protected from regular users

### 📧 Email Notifications
- Welcome email on registration
- Purchase confirmation with order details
- Password reset link
- Prize claim confirmation
- Winner notification after Saturday draw

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Web Framework | Spark Java 2.9.4 |
| JSON Persistence | Gson 2.10.1 |
| Email | Jakarta Mail 2.0.1 (Gmail SMTP) |
| Logging | SLF4J Simple 1.7.36 |
| Build Tool | Maven |
| Frontend | HTML / CSS / JavaScript (inline with Spark) |

---

## 📁 Project Structure

```
SWEFinal/
├── pom.xml                    # Maven dependencies
├── users.json                 # User data (auto-created)
├── tickets.json               # Ticket data (auto-created)
├── orders.json                # Order data (auto-created)
├── email.log                  # Email activity log (auto-created)
└── src/
    ├── ServerBackend.java     # Main entry point + all HTTP routes
    ├── models/
    │   ├── User.java
    │   ├── Ticket.java
    │   ├── PurchasedTicket.java
    │   └── Order.java
    ├── enums/
    │   ├── UserType.java
    │   └── AccountCreationStatus.java
    ├── managers/
    │   ├── UserManager.java
    │   ├── TicketManager.java
    │   ├── OrderManager.java
    │   ├── PaymentProcessor.java
    │   └── EmailNotifier.java
    └── renderers/
        ├── LoginRender.java
        ├── UserRender.java
        └── AdminRender.java
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21 (Amazon Corretto or OpenJDK)
- Maven 3.8+
- IntelliJ IDEA (recommended)

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/lottery-purchase-system.git
cd lottery-purchase-system
```

**2. Install dependencies**
```bash
mvn install
```

**3. Configure Gmail (for email notifications)**

Open `ServerBackend.java` and find `buildSmtpConfig()`:
```java
config.put("user",     "YOUR_GMAIL@gmail.com");
config.put("password", "YOUR_APP_PASSWORD");
config.put("from",     "YOUR_GMAIL@gmail.com");
```

> To get an App Password: Google Account → Security → 2-Step Verification → App Passwords

**4. Run the application**
```bash
mvn compile exec:java -Dexec.mainClass="ServerBackend"
```
Or run `ServerBackend.main()` directly from IntelliJ.

**5. Open in your browser**
```
http://localhost:4567
```

---

## 🔑 Default Admin Account

To create an admin account, register normally then manually edit `users.json` and change:
```json
"role": "user"
```
to:
```json
"role": "ADMIN"
```
Then restart the server.

---

## 🎮 How to Use

### As a User
1. Register at `/register` — must be 18 or older
2. Complete your profile at `/profile/edit` (address, sex, last 4 of SSN and State ID)
3. Browse tickets at `/tickets`
4. Purchase a ticket — pick 5 numbers or use Auto-select
5. View your orders at `/orders`
6. After Saturday's draw, winning tickets appear in `/tickets/my`

### As an Admin
1. Log in with an admin account
2. You are redirected to `/admin` — system status dashboard
3. Manage tickets at `/admin/tickets`

---

## 📋 Lottery Rules

| Matches | Prize |
|---------|-------|
| 5 numbers | 100% of prize pool |
| 4 numbers | 20% of prize pool |
| 3 numbers | 5% of prize pool |
| 2 numbers | 1% of prize pool |
| 0–1 numbers | No prize |

- All tickets require exactly **5 numbers** selected from **1–50**
- Drawings occur every **Saturday**
- Maximum **10 tickets** per transaction
- Prizes **under $600** can be claimed online
- Prizes **$600 or more** must be claimed in person at a lottery center

---

## 🔒 Security

- Passwords hashed with **SHA-256 + random salt**
- Session-based authentication via Spark sessions
- Role-based access control — admin routes blocked at server level
- Profile completion required before any purchase
- Age verification enforced at both client and server level

---

## 📧 Email Events

| Trigger | Email Sent |
|---------|-----------|
| New registration | Welcome email |
| Ticket purchase | Order confirmation |
| Forgot password | Reset link |
| Winning ticket | Winner notification |
| Prize claimed | Claim confirmation |

---

## 👥 Team — Group 3

| Name | Role |
|------|------|
| Raj Patel | Backend |
| Cole Daniel  | Backend |
| Deo Mwala | Full Stack |
| Osman Sen | Backend |

---

## 📚 Course Information

**Course:**  Software Engineering  
**Institution:** Texas Tech University  


---

## 📄 License

This project was developed for educational purposes as part of CS3365 at Texas Tech University.

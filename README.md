# Lottery Purchase System (LPS)

[![CI](https://github.com/deosgracius/Lottery-Purchase-System/actions/workflows/ci.yml/badge.svg)](https://github.com/deosgracius/Lottery-Purchase-System/actions/workflows/ci.yml)

**Live demo:** https://lottery-purchase-system.fly.dev/

A full-stack web application that lets customers register, purchase, manage, and claim
lottery tickets entirely online — no retail visit required. Built with **Java 21** and the
**Spark** micro-framework for CS 3365 (Software Engineering) at Texas Tech University.

> Academic project. Not affiliated with the Texas Lottery Commission.

## Features

**Customers**
- Account registration with **18+ age verification** (client- and server-side)
- Secure login — **SHA-256 + per-user salt** password hashing (no plaintext stored)
- Password reset via emailed link
- Profile management (address, sex, last-4 SSN / State ID) required before purchase
- Browse four lottery games (Powerball, Mega Millions, Lotto Texas, Texas Two Step)
- Purchase up to 10 tickets per order — pick 5 numbers (1–50) or auto-select
- Order history with PENDING status and winning-number highlighting
- Display/print winning tickets; claim prizes under $600 online

**Administrators**
- Status dashboard — total tickets sold and revenue by game
- Manage the catalog — add, edit, toggle availability, remove tickets
- Role-based access control (admin routes protected from regular users)

**Email** — registration, purchase confirmation, password reset, winner, and prize-claim
notifications via Jakarta Mail (Gmail SMTP), each a styled HTML template.

## Architecture

Classic **MVC**, all in Java:

- **Model** — `User`, `Ticket`, `PurchasedTicket`, `Order` (+ `UserType`, `AccountCreationStatus` enums)
- **View** — `LoginRender`, `UserRender`, `AdminRender` build HTML pages with Java text blocks
- **Controller** — `ServerBackend` defines the Spark routes and wires managers to renderers
- **Services** — `UserManager`, `TicketManager`, `OrderManager`, `PaymentProcessor`, `EmailNotifier`

Persistence is JSON-file based (Gson). `tickets.json` ships as seed data; `users.json` and
`orders.json` are generated at runtime and are **not** committed.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Web framework | Spark Java 2.9.4 |
| JSON persistence | Gson 2.10.1 |
| Email | Jakarta Mail 2.0.1 (Gmail SMTP) |
| Logging | SLF4J Simple 1.7.36 |
| Testing | JUnit 5 (Jupiter) |
| Build | Maven |
| CI | GitHub Actions |

## Project structure

```
lottery-purchase-system/
├── pom.xml
├── tickets.json                       # seed catalog (Powerball, Mega Millions, …)
├── LPS_UML.puml / LPS_UML.svg          # UML class diagram
├── .github/workflows/ci.yml            # build + test on every push / PR
└── src/
    ├── main/java/com/deomwala/lps/
    │   ├── ServerBackend.java          # entry point + all HTTP routes (controller)
    │   ├── User / Ticket / PurchasedTicket / Order.java     # models
    │   ├── UserType / AccountCreationStatus.java            # enums
    │   ├── UserManager / TicketManager / OrderManager.java  # services
    │   ├── PaymentProcessor.java / EmailNotifier.java
    │   └── LoginRender / UserRender / AdminRender.java      # views
    └── test/java/com/deomwala/lps/
        ├── UserManagerTest.java
        └── TicketManagerTest.java
```

## Build, test & run

Requires **JDK 21** and **Maven**.

```bash
mvn test        # run the JUnit test suite
mvn package     # build a runnable fat JAR -> target/lottery-purchase-system.jar
java -jar target/lottery-purchase-system.jar   # serves http://localhost:4567
```

### Email configuration (optional)

Email sending is disabled unless SMTP credentials are provided via environment variables
(otherwise messages are logged instead of sent — credentials are never hard-coded or committed):

```bash
export SMTP_USER="you@gmail.com"
export SMTP_PASSWORD="your-gmail-app-password"
export SMTP_FROM="you@gmail.com"        # optional, defaults to SMTP_USER
```

## Deployment

The app is container-ready and configured entirely through environment variables:

| Variable | Purpose | Default |
|---|---|---|
| `PORT` | HTTP port to bind | `4567` |
| `APP_BASE_URL` | Absolute base URL used in emailed links | `http://localhost:4567` |
| `DATA_DIR` | Directory for the JSON data files | `.` |
| `SMTP_USER` / `SMTP_PASSWORD` / `SMTP_FROM` | Gmail SMTP (optional) | unset → emails logged |

```bash
# Docker
docker build -t lps .
docker run -p 8080:8080 -e PORT=8080 -e DATA_DIR=/data -v lps_data:/data lps

# Fly.io (config in fly.toml; /data is a persistent volume)
flyctl deploy
```

A `GET /health` endpoint (`{"status":"ok"}`) is exposed for load-balancer / uptime checks.
The live demo runs on Fly.io with a persistent volume so data survives redeploys.

## Design notes & limitations

- **Payment is simulated.** `PaymentProcessor` validates inputs and approves the
  transaction; integrating a real gateway (e.g. Stripe) is future work.
- **File-based persistence** suits a single-instance demo; a production build would use a
  database.
- **Security:** passwords are salted + SHA-256 hashed; reflected-XSS in the auth pages was
  hardened by rendering user-supplied query params with `textContent` instead of `innerHTML`.
- **Why Spark over Spring Boot?** Lightweight and self-contained, appropriate for the scope.
- **Why inline HTML?** No external template-engine dependency; each view uses Java text blocks.

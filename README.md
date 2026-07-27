# ParkEase - Parking Reservation System (Armendo)

## Overview

ParkEase is a Spring Boot application that provides a parking reservation system with:
    
- Parking lot and parking slot management
- Reservation creation
- Check-in / Check-out
- Reservation cancellation
- Reservation extension
- Billing and invoice generation
- Parking reports

---

# Technology Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- Mockito
- Lombok
- MapStruct

---

# Build and Run

Build the project:

```bash
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

Or simply:

```bash
mvn clean spring-boot:run
```

---

# Run Tests

Execute all unit tests:

```bash
mvn test
```
---

# Features

### Reservation

- Create reservation
- Prevent double booking
- Automatic slot reservation
- Check-in
- Check-out
- Cancel reservation
- Extend reservation

### Billing

- Grace period support
- 30-minute billing blocks
- Daily cap
- Overnight surcharge
- Automatic invoice generation

### Reports

- Daily revenue report
- Parking usage report

---

# Assumptions

The following assumptions were made because they were not explicitly specified in the requirements:

1. Parking slots are locked using **Pessimistic Write Lock** during reservation creation to prevent concurrent double booking.

2. Parking invoice is generated automatically during check-out.

---

# Known Limitations / Trade-offs

- Authentication and authorization are not implemented because there is customer id.
- Dynamic pricing is not implemented.
- Payment processing is not implemented.
- Reservation expiration scheduler is not implemented.
- Reports provide basic summary information only.
- Unit tests are provided for service layer. API integration tests are not included.

---

# Project Structure

```
src
 ├── main
 │   ├── api
 │   ├── domain
 │   ├── repository
 │   ├── service
 │   └── config
 │
 └── test
     └── service
```

---

# Running Example

I've provided a Collection API file for testing in Postman. Please import this file to test the app's REST API.

1. Create Parking Lot
2. Create Parking Slot
3. Create Reservation
4. Check In
5. Check Out
6. Invoice is generated automatically
7. Revenue report can be viewed

---

# Author

Armendo Chandra - Back End Engineer
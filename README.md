# Java Train Ticketing Application

This project is a console-based Java application for managing a train ticketing system. It supports booking tickets, route searching, and a full suite of admin operations, all without external dependencies — pure Java SE.

---

## Architecture & Design

The application follows an MVC-like, layered architecture to separate concerns without requiring external dependencies (like Spring):

| Layer | Package | Responsibility |
|---|---|---|
| **Models** | `Model` | Domain entities: `Train`, `Route`, `Station`, `Booking`, `Ticket`, `Customer`, `RouteOption` |
| **Repositories** | `Model.Repository` | In-memory CRUD stores for each entity |
| **Services** | `Service` | Core business logic and route optimization |
| **Controllers** | `Controller` | Manages the application flow (`AppController`) |
| **Entry Point** | `Main.java` | Wires dependencies and runs the controller |

### Key Design Decisions
- **`Customer` Model Integration:** The system uses a dedicated `Customer` entity rather than raw strings for improved extensibility.
- **`Station` Identification:** Proper `equals()` and `hashCode()` are implemented on `Station` to safely use it as keys in HashMaps for scheduling.
- **Type-safe Timings:** Uses `java.time.LocalTime` and `java.time.LocalDateTime` instead of generic strings.
- **Email Simulation (Important):** As requested by the assignment, "a confirmation mail should be sent". Since this is a standalone Java SE app with no SMTP configured, the `EmailService` *simulates* delivery by printing realistic email bodies to `System.out`. 
- **Admin Audit Trail:** All admin operations print a clear `[ADMIN] operation: SUCCESS / FAILED` message for traceability.
- **Smart Route Optimization:** The optional solution introduces a route optimization engine capable of comparing multiple travel options based on duration, number of changes, and estimated ticket price.

---

## Setup & Execution

The application starts with an interactive console menu. At startup, predefined demo data is loaded automatically, so the user can immediately test route searching, ticket booking, admin operations, delays, and route optimization.

### Compile and run

Navigate to the `src` directory and compile all source files:

```bash
javac Main.java Controller/*.java Service/*.java Model/*.java Model/Repository/*.java
```

Then run:

```bash
java Main
```

### Main menu

```text
===== TRAIN TICKETING APPLICATION =====
1. Customer Menu
2. Admin Menu
0. Exit
```

### Customer operations

```text
1. Search routes
2. Book tickets
3. Show optimized routes
0. Back
```

### Administrator operations

```text
1. Station management
2. Route management
3. Train management
4. View bookings for a train
5. Report train delay
0. Back
```
---

## Features & I/O Examples

The examples below show representative input and output for each supported functionality. They match the behavior available through the interactive console menus.

---

### Admin: Station Management

Administrators can **add**, **update**, and **remove** stations. Failed operations (e.g., duplicate ID or not found) print a descriptive error.

**Example Output:**
```
=== Admin: Station Management ===
[ADMIN] addStation('Bucuresti Nord'): SUCCESS
[ADMIN] addStation('Sinaia'): SUCCESS
[ADMIN] updateStation('Sinaia Centrala'): SUCCESS
[ADMIN] removeStation(id=6): SUCCESS
[ADMIN] removeStation(id=99): FAILED (not found)
```

---

### Admin: Route Management

Administrators can **add**, **update**, and **remove** routes. They can also **add** or **remove individual stations** from an existing route.

**Example Output:**
```
=== Admin: Route Management ===
[ADMIN] addRoute('Route 1: Buc-Brasov'): SUCCESS
[ADMIN] addRoute('Route 3: Temporary'): SUCCESS
[ADMIN] updateRoute('Route 3: Sibiu-Cluj (renamed)'): SUCCESS
[ADMIN] removeRoute(id=3): SUCCESS
[ADMIN] addStationToRoute('Sinaia' -> 'Route 1: Buc-Brasov'): SUCCESS
[ADMIN] removeStationFromRoute('Sinaia' from 'Route 1: Buc-Brasov'): SUCCESS
Route 1 stations after modifications: Bucuresti Nord -> Ploiesti -> Brasov
```

---

### Admin: Train Management

Administrators can **add**, **update** (rename, change seats, etc.), and **remove** trains.

**Example Output:**
```
=== Admin: Train Management ===
[ADMIN] addTrain('IR 1001'): SUCCESS
[ADMIN] addTrain('IR 9999 (Temporary)'): SUCCESS
[ADMIN] updateTrain('IR 9999 (Charter)'): SUCCESS
[ADMIN] removeTrain(id=3): SUCCESS
[ADMIN] removeTrain(id=99): FAILED (not found)
```

---

### Route Search

Customers can search for travel options between two stations. The system finds:
- **Direct trains** (same train, departure station before arrival station).
- **1-stop changeovers** (two trains sharing an intermediate station with ≥5 min connection time).
- **No route found** error if neither applies.

**Example Input:**
```java
searchService.searchRoutes(bucurestiNord, ploiesti);  // direct
searchService.searchRoutes(bucurestiNord, cluj);       // changeover
searchService.searchRoutes(cluj, bucurestiNord);       // impossible
```

**Example Output:**
```
Searching routes from Bucuresti Nord to Ploiesti...
-> Direct Train: IR 1001 | Departs: 08:00 | Arrives: 08:50

Searching routes from Bucuresti Nord to Cluj...
-> Changeover at Brasov:
   Train 1: IR 1001 | Departs Bucuresti Nord: 08:00 | Arrives Brasov: 10:30
   Train 2: IR 1002 | Departs Brasov: 11:00 | Arrives Cluj: 15:30

Searching routes from Cluj to Bucuresti Nord...
Error: No possible link found between Cluj and Bucuresti Nord.
```

---

### Ticket Booking & Overbooking Prevention

Customers can book **one or multiple** tickets in a single call. The system checks available seats before confirming. A confirmation email is sent per ticket.

**Example Input:**
```java
// Book 2 tickets — succeeds (50 total, 0 booked)
bookingService.bookTickets(t2, brasov, cluj, 2, "John Doe", "john.doe@example.com", 45.0);

// Attempt to book 3 — fails (100 total, 98 booked, only 2 left)
bookingService.bookTickets(t1, bucurestiNord, brasov, 3, "Jane Smith", "jane.smith@example.com", 25.0);
```

**Example Output:**
```
--------------------------------------------------
EMAIL TO: john.doe@example.com
SUBJECT: Booking Confirmation - Ticket #1
BODY: Dear John Doe,
Your booking is confirmed.
Train: IR 1002
Route: Brasov -> Cluj
Seat Number: 1
Price: $45.0
Date: 2026-05-10T12:22:08
Thank you for choosing us!
--------------------------------------------------

Error: Overbooking prevented. Train IR 1001 does not have enough seats.
```

---

### Admin: View Bookings

Administrators can retrieve all bookings made for a specific train, including full ticket and customer details.

**Example Input:**
```java
List<Booking> bookings = adminService.getBookingsForTrain(t2);
```

**Example Output:**
```
=== Admin: View Bookings ===
Bookings for Train IR 1002:
  - Ticket ID: 1 | Seat: 1 | Customer: John Doe | Email: john.doe@example.com | Route: Brasov -> Cluj
  - Ticket ID: 2 | Seat: 2 | Customer: John Doe | Email: john.doe@example.com | Route: Brasov -> Cluj
```

---

### Admin: Report Train Delay

Administrators can log a delay on any train. All customers who have a booking on that train are automatically notified via email. If no customers are booked, an appropriate message is shown.

**Example Input:**
```java
adminService.delayTrain(t2, 15);  // IR 1002 is 15 minutes late — 2 customers notified
adminService.delayTrain(t1, 10);  // IR 1001 is 10 minutes late — no customers booked
```

**Example Output:**
```
=== Admin: Report Train Delay ===
[ADMIN] Train 'IR 1002' is now delayed by 15 minute(s). Notifying customers...
--------------------------------------------------
EMAIL TO: john.doe@example.com
SUBJECT: Train Delay Notification - IR 1002
BODY: We regret to inform you that train IR 1002 is delayed by 15 minutes.
We apologize for the inconvenience.
--------------------------------------------------

[ADMIN] Train 'IR 1001' is now delayed by 10 minute(s). Notifying customers...
[ADMIN] No customers to notify.
```

---

### Optional Problem 2: Smart Route Optimizer

### Problem Definition

A common problem in railway systems is selecting the “best” route between two stations when multiple travel options exist.

Different customers may prefer:
- the fastest route,
- the cheapest route,
- or the route with the fewest train changes.

The standard route search implemented in Problem 1 only determines whether a valid connection exists.  
The optional extension introduces a **Smart Route Optimizer** capable of evaluating and ranking all possible travel options.

---

### Implemented Solution

The application introduces:
- a new `RouteOption` model,
- and a dedicated `RouteOptimizerService`.

The optimizer:
- searches all direct and one-change routes,
- computes:
   - total duration,
   - estimated ticket price,
   - number of train changes,
- and selects the optimal route depending on the optimization criteria.

The optimizer supports:
1. Fastest route
2. Cheapest route
3. Listing all possible route options

---

### Optimization Criteria

#### Fastest Route
Routes are sorted by:
1. total duration,
2. number of changes,
3. total price.

#### Cheapest Route
Routes are sorted by:
1. total estimated price,
2. total duration,
3. number of changes.

---

### Example Output

```text
=== Optional Problem 2: Smart Route Optimizer ===

[OPTIMIZER] All route options from Bucuresti Nord to Cluj:

Option 1:
Stations: Bucuresti Nord -> Brasov -> Cluj
Trains: IR 1001 + IR 1002
Departure: 08:00
Arrival: 15:30
Total duration: 7h 30m
Changes: 1
Estimated price: $80.0

[OPTIMIZER] Optimization criteria: FASTEST
Best route found:
Stations: Bucuresti Nord -> Brasov -> Cluj
Trains: IR 1001 + IR 1002
Departure: 08:00
Arrival: 15:30
Total duration: 7h 30m
Changes: 1
Estimated price: $80.0
```

## Automated Tests
```bash
mvn test
```

The project includes JUnit 5 tests for the main business logic:

- booking tickets successfully
- overbooking prevention
- direct route search
- route search with changeover
- smart route optimization

The tests are located in the `src/tests` folder and can be run directly from IntelliJ using JUnit 5.
The tests validate the most important business rules of the application, including booking validation, route searching, and route optimization behavior.
## Limitations

- The application uses in-memory repositories, so data is reset when the program stops.
- Email sending is simulated through console output because no SMTP server is configured.
- The optional route optimizer estimates ticket prices based on route duration and predefined logic.
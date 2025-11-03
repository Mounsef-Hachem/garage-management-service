# Renault Garage Service

This application is a REST API to manage garages, vehicles and accessories designed to be clear, testable, and scalable.

### **1) Architecture**
- Java 21, Spring Boot, Spring Data JPA, MapStruct, Lombok, OpenAPI, H2 Database.

### **2) Project layout**
- controllers, services, repositories, models (entities), dtos, mappers, exceptions.

### **3) Core domain**
- **Garage**: name, address, telephone, email, openingHours (Map<DayOfWeek, List<OpeningTime>>).
- **Vehicle**: brand, manufacturingYear, fuelType, Garage _(belongs to a Garage)_.
- **Accessory**: name, description, price, type, Vehicle _(belongs to a Vehicle)_.

### **4) Key rules**
- Garage vehicle quota = 50 vehicles (enforced in the service layer).
- Garage search by accessory: returns garages that have at least one vehicle containing the accessory.

### **5) Representative endpoints**
- /api/garages — create / get / update / delete, supported-vehicle and search/by-accessory
- /api/vehicles — create (in garage), update, delete, list by garage, search by brand (optional garageIds)
- /api/accessories — add to vehicle, update, delete, list by vehicle

### **6) Persisting opening hours**

**Goal**: persist `Map<DayOfWeek, List<OpeningTime>> openingHours` where each day may hold time ranges. 

Project implements the simple approach required by the specification; an alternative, more flexible model is shown for clarity.

**Solution 1** — `@ElementCollection` (implemented, per spec)
- Simple and compact: suitable when the domain needs at most one opening range per day or when you prefer a minimal schema.

```java
@Embeddable
public class OpeningTime {
    private LocalTime openTime;
    private LocalTime closeTime;
}

@Entity
public class Garage {
    @Id @GeneratedValue
    private Long id;

    @ElementCollection
    @CollectionTable(name = "garage_opening_times", joinColumns = @JoinColumn(name = "garage_id"))
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, OpeningTime> openingHours = new HashMap<>();
}
```

**Solution 2** — Dedicated entities (`@OneToMany`) — (alternative)
- Use when you need multiple ranges per day.

```java
@Entity
public class Garage {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHour> openingHours = new ArrayList<>();
}

@Entity
public class OpeningHour {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "openingHour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningTime> openingTimes = new ArrayList<>();
}

@Entity
public class OpeningTime {
    @Id @GeneratedValue
    private Long id;
    private LocalTime openTime;
    private LocalTime closeTime;
}
```

**Recommendation:** 

Solution 1 is implemented to match the specification and keep the implementation compact; Solution 2 is preferable if the domain requires multiple ranges per day or advanced time queries.

---

### **7) Running & testing**

- **Run the application**

1. Make sure you have **Java 21** and **Docker Engine** installed.
2. Build and run the application:

```bash

./mvnw clean install
./mvnw spring-boot:run

```

-  The application will start on 👉 http://localhost:8080

-  Swagger UI documentation is available at: 👉 http://localhost:8080/swagger-ui/index.html


- **Run unit tests**

    Unit tests are located in `src/test/java` (same package structure as main code).
    To execute all unit and integration tests:

```bash

./mvnw test

```
    
    This will:
        - Run all JUnit tests located under src/test/java.
        - Display test results in the console.

- **Run Integration Tests**

    Integration tests verify that multiple components work together, including REST controllers, services, repositories, and Kafka messaging.

**Setup**

  * Uses `@SpringBootTest` to load the full Spring context.
  * Uses `@AutoConfigureMockMvc` to simulate `HTTP` requests.
  * Uses `@EmbeddedKafka` to run an in-memory Kafka broker for event testing.

**Run Integration Tests**

```bash

./mvnw verify

```
* Integration tests are located in `src/test/java` under integration packages.
* They start the full Spring context and verify the interaction between multiple layers.
* Verify both `HTTP` responses and database persistence.

---

### **8) Kafka Integration**

The project integrates Apache Kafka for event-driven communication.

- **Producer**: Publishes a `VehicleResponseDTO` event whenever a new vehicle is created.
- **Consumer**: Listens to the `vehicle-events` topic and processes the received events.

#### **Prerequisites**

Before running Kafka, make sure:
- **Docker Engine** is installed and running on your machine.
- **Docker Compose** is available.

You can verify the installation with:
```bash

docker --version
docker compose version
```

#### To start Kafka:

```bash

docker compose up -d

```

#### To stop Kafka:

```bash

docker compose down

```

### Implementation Overview

**Vehicle Publisher**

Publishes a message when a new vehicle is created:

```java

@Component
@RequiredArgsConstructor
public class VehicleEventPublisher {

    private final KafkaTemplate<String, VehicleResponseDTO> kafkaTemplate;
    private static final String TOPIC = "vehicle-events";

    public void publishVehicleCreated(VehicleResponseDTO vehicleResponse) {
        kafkaTemplate.send(TOPIC, vehicleResponse);
        System.out.println("Vehicle event published to Kafka: " + vehicleResponse);
    }
}

```

Vehicle Consumer

Listens for messages from the same Kafka topic:

```java

@Component
public class VehicleEventConsumer {

    @KafkaListener(topics = "vehicle-events", groupId = "garage-service-group")
    public void consumeVehicleEvent(VehicleResponseDTO event) {
        System.out.println("Vehicle event consumed: " + event);
        // Here we can add business logic: e.g.: record an audit, notify an external service, etc.
    }
}
```

---



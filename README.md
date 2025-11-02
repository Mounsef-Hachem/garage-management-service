# Renault Garage Service

This application is a REST API to manage garages, vehicles and accessories designed to be clear, testable, and scalable.

**1) Architecture**
- Java 21, Spring Boot, Spring Data JPA, MapStruct, Lombok, OpenAPI, H2 Database.

**2) Project layout**
- controllers, services, repositories, models (entities), dtos, mappers, exceptions.

**3) Core domain**
- **Garage**: name, address, telephone, email, openingHours (Map<DayOfWeek, List<OpeningTime>>).
- **Vehicle**: brand, manufacturingYear, fuelType, Garage _(belongs to a Garage)_.
- **Accessory**: name, description, price, type, Vehicle _(belongs to a Vehicle)_.

**4) Key rules**
- Garage vehicle quota = 50 vehicles (enforced in the service layer).
- Garage search by accessory: returns garages that have at least one vehicle containing the accessory.

**5) Representative endpoints**
- /api/garages — create / get / update / delete, supported-vehicle and search/by-accessory
- /api/vehicles — create (in garage), update, delete, list by garage, search by brand (optional garageIds)
- /api/accessories — add to vehicle, update, delete, list by vehicle

**6) Persisting opening hours**

Goal: persist `Map<DayOfWeek, List<OpeningTime>> openingHours` where each day may hold time ranges. 

Project implements the simple approach required by the specification; an alternative, more flexible model is shown for clarity.

Solution 1 — `@ElementCollection` (implemented, per spec)
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

Solution 2 — Dedicated entities (`@OneToMany`) — (alternative)
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

**7) Running & testing**

- **Run the application**

  To start the API locally:

```bash

./mvnw spring-boot:run

```

-  The application will start on 👉 http://localhost:8080

-  Swagger UI documentation is available at: 👉 http://localhost:8080/swagger-ui/index.html


- **Run unit tests**

    To execute all unit and integration tests:

```bash

./mvnw test

```

    This will:
        - Automatically start an in-memory H2 database.
        - Run all JUnit tests located under src/test/java.
        - Display test results in the console.
---


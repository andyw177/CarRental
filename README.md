# Car Rental System

## Requirements

- The system allows reservation of a car of a given type at a desired
  date and time for a specified number of days.
- There are three supported car types:
  - SEDAN
  - SUV
  - VAN
- The number of cars available for each type is limited.

## Assumptions

- Inventory contains a fixed number of cars for each car type.
- A reservation is made for one specific car of the requested type.
- A car is available when it has no existing reservation that overlaps
  the requested reservation period.
- If one car is already reserved, another car of the same type may be
  assigned if one is available.
- If all cars of the requested type have overlapping reservations,
  the reservation request cannot be completed.
- A car becomes available again after its existing reservation ends.
- A reservation may start exactly when a previous reservation ends.
- The number of rental days must be greater than zero.
- Customer accounts, payments, pricing, cancellations, persistence,
  and rental locations are outside the scope of this implementation.

## Design

### CarType

Enum representing the supported car types.

    SEDAN
    SUV
    VAN

### Car

Represents an individual car in the inventory.

Properties:

    id
    type

### Reservation

Represents the reservation of a specific car for a period of time.

Properties:

    id
    car
    startTime
    endTime

The end time is calculated using:

    endTime = startTime + numberOfDays

### CarInventory

Maintains the fixed collection of cars available through the
rental system.

    CarInventory
          |
          +-- List<Car>

### CarRentalService

Contains the business logic for checking availability and creating
reservations.

    CarRentalService
          |
          +-- CarInventory
          |
          +-- List<Reservation>

## Reservation Flow

    Reservation Request
            |
            v
    Validate car type,
    start time and days
            |
            v
    Calculate end time
            |
            v
    Find cars matching
    requested CarType
            |
            v
    Check reservations
    for each matching car
            |
       +----+----+
       |         |
    Available   None Available
       |         |
       v         v
    Create      Reject
    Reservation Request
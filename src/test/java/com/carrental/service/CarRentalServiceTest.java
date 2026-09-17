package com.carrental.service;

import com.carrental.exceptions.CarUnavailableException;
import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarInventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalServiceTest {

    private CarRentalService service;

    @BeforeEach
    void setUp() {

        CarInventory inventory = new CarInventory(List.of(
                new Car("SEDAN-1", CarType.SEDAN),
                new Car("SEDAN-2", CarType.SEDAN),
                new Car("SUV-1", CarType.SUV),
                new Car("SUV-2", CarType.SUV),
                new Car("VAN-1", CarType.VAN)
        ));

        service = new CarRentalService(inventory);
    }

    @Test
    void shouldReserveAvailableCar() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        Reservation reservation =
                service.reserve(CarType.SUV, start, 3);

        assertNotNull(reservation);
        assertEquals(CarType.SUV, reservation.getCar().getCarType());
        assertEquals(start, reservation.getStartTime());
        assertEquals(start.plusDays(3), reservation.getEndTime());
    }

    @Test
    void shouldAssignDifferentCarWhenFirstCarIsReserved() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        Reservation first =
                service.reserve(CarType.SUV, start, 3);

        Reservation second =
                service.reserve(CarType.SUV, start, 3);

        assertNotNull(first);
        assertNotNull(second);

        assertNotEquals(
                first.getCar().getId(),
                second.getCar().getId()
        );
    }

    @Test
    void shouldRejectReservationWhenAllCarsOfTypeAreUnavailable() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        // There are only two SUVs
        service.reserve(CarType.SUV, start, 3);
        service.reserve(CarType.SUV, start, 3);

        assertThrows(
                CarUnavailableException.class,
                () -> service.reserve(
                        CarType.SUV,
                        start,
                        3)
        );
    }

    @Test
    void shouldAllowCarToBeReservedAfterPreviousReservationEnds() {

        LocalDateTime firstStart =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        Reservation first =
                service.reserve(CarType.VAN, firstStart, 3);

        // First reservation ends exactly here
        LocalDateTime secondStart =
                firstStart.plusDays(3);

        Reservation second =
                service.reserve(CarType.VAN, secondStart, 2);

        assertNotNull(second);

        assertEquals(
                first.getCar().getId(),
                second.getCar().getId()
        );

        assertEquals(secondStart, second.getStartTime());
    }

    @Test
    void shouldRejectOverlappingReservationForOnlyAvailableCar() {

        LocalDateTime firstStart =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        // Only one van exists
        service.reserve(CarType.VAN, firstStart, 3);

        // Starts while existing reservation is active
        LocalDateTime overlappingStart =
                LocalDateTime.of(2026, 10, 2, 10, 0);

        assertThrows(
                CarUnavailableException.class,
                () -> service.reserve(
                        CarType.VAN,
                        overlappingStart,
                        2)
        );
    }

    @Test
    void shouldAllowDifferentCarTypesAtSameTime() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        Reservation sedan =
                service.reserve(CarType.SEDAN, start, 3);

        Reservation suv =
                service.reserve(CarType.SUV, start, 3);

        Reservation van =
                service.reserve(CarType.VAN, start, 3);

        assertEquals(CarType.SEDAN, sedan.getCar().getCarType());
        assertEquals(CarType.SUV, suv.getCar().getCarType());
        assertEquals(CarType.VAN, van.getCar().getCarType());
    }

    @Test
    void shouldRejectZeroRentalDays() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reserve(
                        CarType.SEDAN,
                        start,
                        0)
        );
    }

    @Test
    void shouldRejectNegativeRentalDays() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reserve(
                        CarType.SEDAN,
                        start,
                        -1)
        );
    }

    @Test
    void shouldRejectNullCarType() {

        LocalDateTime start =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reserve(
                        null,
                        start,
                        3)
        );
    }

    @Test
    void shouldRejectNullStartTime() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reserve(
                        CarType.SEDAN,
                        null,
                        3)
        );
    }
}
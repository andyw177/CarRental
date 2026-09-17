package com.carrental.service;

import com.carrental.exceptions.CarUnavailableException;
import com.carrental.repository.CarInventory;
import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;

import java.time.LocalDateTime;
import java.util.*;

public class CarRentalService {

    private final CarInventory inventory;
    private final List<Reservation> reservations;

    public CarRentalService(CarInventory inventory) {
        this.inventory = Objects.requireNonNull(inventory);
        this.reservations = new ArrayList<>();
    }

    public Reservation reserve(
            CarType type,
            LocalDateTime startTime,
            int numberOfDays) {

        //
        // type != null
        // startTime != null
        // numberOfDays > 0

        if (type == null) {
            throw new IllegalArgumentException(
                    "Car type cannot be null");
        }

        if (startTime == null) {
            throw new IllegalArgumentException(
                    "Start time cannot be null");
        }

        if (numberOfDays <= 0) {
            throw new IllegalArgumentException(
                    "Number of days must be greater than zero");
        }

        // Calculate requestedEnd using numberOfDays.
        LocalDateTime requestedEndDate;
        requestedEndDate = startTime.plusDays(numberOfDays);


        // Get all cars matching the requested CarType.
        List<Car> cars = inventory.getCarsByType(type);

        // Find a car that does NOT have an overlapping reservation.
        Optional<Car> availCar = cars.stream().filter(car ->
                isCarAvailable(car, startTime,requestedEndDate)
        ).findFirst();


        // If a car is available:
        //      create Reservation
        //      add it to reservations
        //      return it
        // If none are available, indicate that the
        //     reservation cannot be completed.
        if(availCar.isEmpty()){
            throw new CarUnavailableException("No cars available for given type " + type);
        }

        Reservation reservation = new Reservation(availCar.get(),startTime,requestedEndDate);
        reservations.add(reservation);

        return reservation;

    }

    // Look through reservations belonging to this car.
    // If ANY reservation overlaps then returns false
    private boolean isCarAvailable(Car car,LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        return !reservations.stream().
                filter(reservation -> reservation.getCar().equals(car))
                .anyMatch(reservation -> reservation.overlaps(requestedStart,requestedEnd));
    }

    public List<Reservation> getReservations() {
        List<Reservation> res = new ArrayList<>();
        res.addAll(reservations);
        return res;
    }
}
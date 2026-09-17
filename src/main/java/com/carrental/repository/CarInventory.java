package com.carrental.repository;

import com.carrental.model.Car;
import com.carrental.model.CarType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CarInventory {

    private final List<Car> cars;

    public CarInventory(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCarsByType(CarType type) {
        // Return only cars whose type matches the requested type.

        return getCars().stream().filter(car -> car.getCarType() == type).
                collect(Collectors.toList());
    }

    public List<Car> getCars() {
        // Return a read only
        return Collections.unmodifiableList(cars);
    }
}
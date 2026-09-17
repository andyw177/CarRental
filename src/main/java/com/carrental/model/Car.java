package com.carrental.model;

import java.util.Objects;
import java.util.Random;

public class Car {
    private final String id;
    private final CarType carType;

    public String getId() {
        return id;
    }

    public CarType getCarType() {
        return carType;
    }

    public Car(String id, CarType carType) {
        this.id = id;
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "model.Car{" +
                "id=" + id +
                ", carType=" + carType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) &&
                carType == car.carType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carType);
    }
}

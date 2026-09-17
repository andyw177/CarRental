package com.carrental.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Reservation {

    private final String id;
    private final Car car;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Reservation(
            Car car,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        this.id = UUID.randomUUID().toString();
        this.car = Objects.requireNonNull(car);
        this.startTime = Objects.requireNonNull(startTime);
        this.endTime = Objects.requireNonNull(endTime);
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd) {

        // Determine whether [requestedStart, requestedEnd)
        // overlaps [startTime, endTime). (intersection problem)

        // the 4 cases
        // 1. overlapped
        // 2. request is before
        // 3. request is after
        // 4. request is subsequence

        //requested start begins before existing ends
        //start is fine but request end after existing start date
        return requestedStart.isBefore(endTime)
                && requestedEnd.isAfter(startTime);
    }
}
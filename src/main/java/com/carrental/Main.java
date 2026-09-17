package com.carrental;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarInventory;
import com.carrental.service.CarRentalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create sample inventory
        CarInventory inventory = new CarInventory(List.of(
                new Car("SEDAN-1", CarType.SEDAN),
                new Car("SEDAN-2", CarType.SEDAN),
                new Car("SUV-1", CarType.SUV),
                new Car("SUV-2", CarType.SUV),
                new Car("VAN-1", CarType.VAN)
        ));

        CarRentalService rentalService =
                new CarRentalService(inventory);
//
//        LocalDateTime start =
//                LocalDateTime.of(2026, 10, 1, 10, 0);
//
//        // First SUV reservation
//        Reservation first =
//                rentalService.reserve(
//                        CarType.SUV,
//                        start,
//                        3);
//
//        System.out.println("Reservation created:");
//        printReservation(first);
//
//        // Second overlapping SUV reservation
//        Reservation second =
//                rentalService.reserve(
//                        CarType.SUV,
//                        start,
//                        3);
//
//        System.out.println("\nSecond reservation created:");
//        printReservation(second);

        Scanner sc = new Scanner(System.in);
        int input = -1;
        while(input != 0) {
            System.out.println("--------------------------------");
            System.out.println("Welcome to Car Rental System");
            System.out.println("Press 1 to view all reservations");
            System.out.println("Press 2 to make a reservation");
            System.out.println("Press 0 to exit");
            input = sc.nextInt();
            switch (input){
                case 1:
                    List<Reservation> res = rentalService.getReservations();

                    if(res.isEmpty()){
                        System.out.println("No Reservations");
                    }
                   res.stream().forEach(reservation -> printReservation(reservation));
                    break;
                case 2:
                    System.out.println("Enter car type: SEDAN, SUV, SEDAN");
                    String type = sc.next();
                    System.out.println("Enter date and time (2026-09-16T23:12:00 formatting): ");
                    String datetime = sc.next();
                    System.out.println("Enter number days");
                    int numDays = sc.nextInt();

                    try{
                        Reservation reservation = rentalService.reserve(CarType.valueOf(type), LocalDateTime.parse(datetime),numDays);
                        printReservation(reservation);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Thank you for using CarRentalService");
                default:
                    System.out.println("invalid input");
            }

        }

    }

    private static void printReservation(
            Reservation reservation) {

        System.out.println("-------------------------------");
        System.out.println(
                "Car: " + reservation.getCar().getId());

        System.out.println(
                "Type: " + reservation.getCar().getCarType());

        System.out.println(
                "Start: " + reservation.getStartTime());

        System.out.println(
                "End: " + reservation.getEndTime());
    }
}
package tests;

import Model.Customer;
import Model.Route;
import Model.Station;
import Model.Train;
import Model.Repository.BookingRepository;
import Model.Repository.TrainRepository;
import Service.BookingService;
import Service.EmailService;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    @Test
    public void testOverbookingPrevention() {

        BookingRepository bookingRepository = new BookingRepository();
        TrainRepository trainRepository = new TrainRepository();
        EmailService emailService = new EmailService();

        BookingService bookingService =
                new BookingService(bookingRepository, trainRepository, emailService);

        Station s1 = new Station(1, "Bucuresti", "Bucuresti");
        Station s2 = new Station(2, "Brasov", "Brasov");

        Route route = new Route(
                1,
                "Route",
                new ArrayList<>() {{
                    add(s1);
                    add(s2);
                }}
        );

        Map<Station, LocalTime> times = new HashMap<>();
        times.put(s1, LocalTime.of(8, 0));
        times.put(s2, LocalTime.of(10, 0));

        Train train = new Train(
                1,
                "IR100",
                route,
                times,
                10,
                9,
                0
        );

        Customer customer =
                new Customer(1, "John", "john@test.com");

        String result = bookingService.bookTickets(
                train,
                s1,
                s2,
                2,
                customer,
                50.0
        );

        assertTrue(result.contains("Overbooking prevented"));
    }


    @Test
    public void testSuccessfulBooking() {

        BookingRepository bookingRepository = new BookingRepository();
        TrainRepository trainRepository = new TrainRepository();
        EmailService emailService = new EmailService();

        BookingService bookingService =
                new BookingService(bookingRepository, trainRepository, emailService);

        Station s1 = new Station(1, "Bucuresti", "Bucuresti");
        Station s2 = new Station(2, "Brasov", "Brasov");

        Route route = new Route(
                1,
                "Route",
                new ArrayList<>() {{
                    add(s1);
                    add(s2);
                }}
        );

        Map<Station, LocalTime> times = new HashMap<>();
        times.put(s1, LocalTime.of(8, 0));
        times.put(s2, LocalTime.of(10, 0));

        Train train = new Train(
                1,
                "IR100",
                route,
                times,
                10,
                0,
                0
        );

        Customer customer =
                new Customer(1, "John", "john@test.com");

        String result = bookingService.bookTickets(
                train,
                s1,
                s2,
                2,
                customer,
                50.0
        );

        assertTrue(result.contains("Booking Confirmation"));
    }



}
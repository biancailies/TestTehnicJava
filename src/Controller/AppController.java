package Controller;

import Model.Booking;
import Model.Customer;
import Model.Route;
import Model.Station;
import Model.Train;
import Service.AdminService;
import Service.BookingService;
import Service.SearchService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {

    private final AdminService adminService;
    private final BookingService bookingService;
    private final SearchService searchService;

    public AppController(AdminService adminService, BookingService bookingService, SearchService searchService) {
        this.adminService = adminService;
        this.bookingService = bookingService;
        this.searchService = searchService;
    }

    public void runDemo() {
        System.out.println("============================================================");
        System.out.println("       Java Train Ticketing Application - Full Demo         ");
        System.out.println("============================================================\n");

        System.out.println("=== [SECTION 1] Admin: Station Management ===");

        Station s1 = new Station(1, "Bucuresti Nord", "Bucuresti");
        Station s2 = new Station(2, "Ploiesti", "Ploiesti");
        Station s3 = new Station(3, "Brasov", "Brasov");
        Station s4 = new Station(4, "Sibiu", "Sibiu");
        Station s5 = new Station(5, "Cluj", "Cluj");
        Station s6 = new Station(6, "Sinaia", "Sinaia");

        adminService.addStation(s1);
        adminService.addStation(s2);
        adminService.addStation(s3);
        adminService.addStation(s4);
        adminService.addStation(s5);
        adminService.addStation(s6);

        s6.setName("Sinaia Centrala");
        System.out.println(adminService.updateStation(s6));
        System.out.println(adminService.removeStation(6));
        System.out.println(adminService.removeStation(99));
        System.out.println();

        System.out.println("=== [SECTION 2] Admin: Route Management ===");

        Route r1 = new Route(1, "Route 1: Buc-Brasov", new ArrayList<>(Arrays.asList(s1, s2, s3)));
        Route r2 = new Route(2, "Route 2: Brasov-Cluj", new ArrayList<>(Arrays.asList(s3, s4, s5)));
        Route r3 = new Route(3, "Route 3: Temporary", new ArrayList<>(Arrays.asList(s4, s5)));

        adminService.addRoute(r1);
        adminService.addRoute(r2);
        adminService.addRoute(r3);

        r3.setName("Route 3: Sibiu-Cluj (renamed)");
        System.out.println(adminService.updateRoute(r3));
        System.out.println(adminService.removeRoute(3));

        Station sSinaia = new Station(7, "Sinaia", "Sinaia");
        System.out.println(adminService.addStation(sSinaia));
        System.out.println(adminService.addStationToRoute(r1, sSinaia));
        System.out.println(adminService.removeStationFromRoute(r1, sSinaia));

        System.out.println("Route 1 stations after modifications: " +
                r1.getStations().stream().map(Station::getName).reduce((a, b) -> a + " -> " + b).orElse("(empty)"));
        System.out.println();

        System.out.println("=== [SECTION 3] Admin: Train Management ===");

        Map<Station, LocalTime> timesT1 = new HashMap<>();
        timesT1.put(s1, LocalTime.of(8, 0));
        timesT1.put(s2, LocalTime.of(8, 50));
        timesT1.put(s3, LocalTime.of(10, 30));
        Train t1 = new Train(1, "IR 1001", r1, timesT1, 100, 98, 0);

        Map<Station, LocalTime> timesT2 = new HashMap<>();
        timesT2.put(s3, LocalTime.of(11, 0));
        timesT2.put(s4, LocalTime.of(13, 0));
        timesT2.put(s5, LocalTime.of(15, 30));
        Train t2 = new Train(2, "IR 1002", r2, timesT2, 50, 0, 0);

        Train t3 = new Train(3, "IR 9999 (Temporary)", r2, new HashMap<>(), 30, 0, 0);

        adminService.addTrain(t1);
        adminService.addTrain(t2);
        adminService.addTrain(t3);

        t3.setName("IR 9999 (Charter)");
        t3.setTotalSeats(60);
        System.out.println(adminService.updateTrain(t3));
        System.out.println(adminService.removeTrain(3));
        System.out.println(adminService.removeTrain(99));
        System.out.println();

        System.out.println("=== [SECTION 4] Route Search ===");

        System.out.print(searchService.searchRoutes(s1, s2));
        System.out.println();
        System.out.print(searchService.searchRoutes(s1, s5));
        System.out.println();
        System.out.print(searchService.searchRoutes(s5, s1));
        System.out.println();

        System.out.println("=== [SECTION 5] Ticket Booking ===");

        Customer c1 = new Customer(1, "John Doe", "john.doe@example.com");
        Customer c2 = new Customer(2, "Jane Smith", "jane.smith@example.com");

        System.out.print(bookingService.bookTickets(t2, s3, s5, 2, c1, 45.0));
        System.out.println();

        System.out.print(bookingService.bookTickets(t1, s1, s3, 3, c2, 25.0));
        System.out.println();

        System.out.println("=== [SECTION 6] Admin: View Bookings ===");

        List<Booking> t2Bookings = adminService.getBookingsForTrain(t2);
        System.out.println("Bookings for Train " + t2.getName() + ":");
        for (Booking b : t2Bookings) {
            System.out.println("  Booking ID: " + b.getId() + " | Customer: " + b.getCustomer().getName() + " | Email: " + b.getCustomer().getEmail());
            for (Model.Ticket tk : b.getTickets()) {
                System.out.println("    - Ticket ID: " + tk.getId()
                        + " | Seat: " + tk.getSeatNumber()
                        + " | Route: " + tk.getDepartureStation().getName()
                        + " -> " + tk.getArrivalStation().getName());
            }
        }
        System.out.println();

        System.out.println("=== [SECTION 7] Admin: Report Train Delay ===");
        System.out.print(adminService.delayTrain(t2, 15));
        System.out.println();
        System.out.print(adminService.delayTrain(t1, 10));
    }
}

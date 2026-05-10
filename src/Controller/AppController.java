package Controller;

import Model.Booking;
import Model.Customer;
import Model.Route;
import Model.Station;
import Model.Train;
import Service.AdminService;
import Service.BookingService;
import Service.RouteOptimizerService;
import Service.SearchService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppController {

    private final AdminService adminService;
    private final BookingService bookingService;
    private final SearchService searchService;
    private final RouteOptimizerService routeOptimizerService;
    private final Scanner scanner = new Scanner(System.in);

    private int nextStationId = 100;
    private int nextRouteId = 100;
    private int nextTrainId = 100;
    private int nextCustomerId = 100;

    public AppController(AdminService adminService,
                         BookingService bookingService,
                         SearchService searchService,
                         RouteOptimizerService routeOptimizerService) {
        this.adminService = adminService;
        this.bookingService = bookingService;
        this.searchService = searchService;
        this.routeOptimizerService = routeOptimizerService;
    }

    public void runDemo() {
        initializeData();
        runInteractiveMenu();
    }

    private void initializeData() {
        Station s1 = new Station(1, "Bucuresti Nord", "Bucuresti");
        Station s2 = new Station(2, "Ploiesti", "Ploiesti");
        Station s3 = new Station(3, "Brasov", "Brasov");
        Station s4 = new Station(4, "Sibiu", "Sibiu");
        Station s5 = new Station(5, "Cluj", "Cluj");

        adminService.addStation(s1);
        adminService.addStation(s2);
        adminService.addStation(s3);
        adminService.addStation(s4);
        adminService.addStation(s5);

        List<Station> route1Stations = new ArrayList<>();
        route1Stations.add(s1);
        route1Stations.add(s2);
        route1Stations.add(s3);

        List<Station> route2Stations = new ArrayList<>();
        route2Stations.add(s3);
        route2Stations.add(s4);
        route2Stations.add(s5);

        Route r1 = new Route(1, "Route 1: Bucuresti-Brasov", route1Stations);
        Route r2 = new Route(2, "Route 2: Brasov-Cluj", route2Stations);

        adminService.addRoute(r1);
        adminService.addRoute(r2);

        Map<Station, LocalTime> timesT1 = new HashMap<>();
        timesT1.put(s1, LocalTime.of(8, 0));
        timesT1.put(s2, LocalTime.of(8, 50));
        timesT1.put(s3, LocalTime.of(10, 30));

        Map<Station, LocalTime> timesT2 = new HashMap<>();
        timesT2.put(s3, LocalTime.of(11, 0));
        timesT2.put(s4, LocalTime.of(13, 0));
        timesT2.put(s5, LocalTime.of(15, 30));

        Train t1 = new Train(1, "IR 1001", r1, timesT1, 100, 0, 0);
        Train t2 = new Train(2, "IR 1002", r2, timesT2, 50, 0, 0);

        adminService.addTrain(t1);
        adminService.addTrain(t2);
    }

    private void runInteractiveMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=================================================");
            System.out.println("        JAVA TRAIN TICKETING APPLICATION         ");
            System.out.println("=================================================");
            System.out.println("1. Customer menu");
            System.out.println("2. Admin menu");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int option = readInt();

            switch (option) {
                case 1 -> customerMenu();
                case 2 -> adminMenu();
                case 0 -> {
                    System.out.println("Exiting application...");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void customerMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Show all trains");
            System.out.println("2. Search route");
            System.out.println("3. Book tickets");
            System.out.println("4. Smart route optimizer");
            System.out.println("0. Back");
            System.out.print("Choose option: ");

            int option = readInt();

            switch (option) {
                case 1 -> showAllTrains();
                case 2 -> searchRoute();
                case 3 -> bookTickets();
                case 4 -> smartRouteOptimizer();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void adminMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Show stations");
            System.out.println("2. Add station");
            System.out.println("3. Show routes");
            System.out.println("4. Add route");
            System.out.println("5. Update route name");
            System.out.println("6. Remove route");
            System.out.println("7. Show trains");
            System.out.println("8. Add train");
            System.out.println("9. Update train");
            System.out.println("10. Remove train");
            System.out.println("11. Show bookings for train");
            System.out.println("12. Report train delay");
            System.out.println("0. Back");
            System.out.print("Choose option: ");

            int option = readInt();

            switch (option) {
                case 1 -> showAllStations();
                case 2 -> addStation();
                case 3 -> showAllRoutes();
                case 4 -> addRoute();
                case 5 -> updateRouteName();
                case 6 -> removeRoute();
                case 7 -> showAllTrains();
                case 8 -> addTrain();
                case 9 -> updateTrain();
                case 10 -> removeTrain();
                case 11 -> showBookingsForTrain();
                case 12 -> reportDelay();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showAllStations() {
        System.out.println("\nAvailable stations:");
        for (Station station : adminService.getAllStations()) {
            System.out.println(station.getId() + ". " + station.getName() + " (" + station.getCity() + ")");
        }
    }

    private void showAllRoutes() {
        System.out.println("\nAvailable routes:");
        for (Route route : adminService.getAllRoutes()) {
            System.out.print(route.getId() + ". " + route.getName() + ": ");
            printStationPath(route.getStations());
        }
    }

    private void showAllTrains() {
        System.out.println("\nAvailable trains:");
        for (Train train : adminService.getAllTrains()) {
            System.out.println(train.getId() + ". " + train.getName()
                    + " | Route: " + train.getRoute().getName()
                    + " | Seats: " + train.getBookedSeats() + "/" + train.getTotalSeats()
                    + " | Delay: " + train.getDelayMinutes() + " min");

            System.out.print("   Stations: ");
            printStationPath(train.getRoute().getStations());
        }
    }

    private void addStation() {
        System.out.print("Station name: ");
        String name = scanner.nextLine();

        System.out.print("City: ");
        String city = scanner.nextLine();

        Station station = new Station(nextStationId++, name, city);
        System.out.println(adminService.addStation(station));
    }

    private void addRoute() {
        showAllStations();

        System.out.print("Route name: ");
        String name = scanner.nextLine();

        System.out.print("Enter station IDs separated by comma, in order: ");
        String input = scanner.nextLine();

        List<Station> stations = parseStations(input);

        Route route = new Route(nextRouteId++, name, stations);
        System.out.println(adminService.addRoute(route));
    }

    private void updateRouteName() {
        showAllRoutes();

        System.out.print("Route ID: ");
        int id = readInt();

        Route route = adminService.findRouteById(id);
        if (route == null) {
            System.out.println("Route not found.");
            return;
        }

        System.out.print("New route name: ");
        String newName = scanner.nextLine();

        route.setName(newName);
        System.out.println(adminService.updateRoute(route));
    }

    private void removeRoute() {
        showAllRoutes();

        System.out.print("Route ID to remove: ");
        int id = readInt();

        System.out.println(adminService.removeRoute(id));
    }

    private void addTrain() {
        showAllRoutes();

        System.out.print("Train name: ");
        String name = scanner.nextLine();

        System.out.print("Route ID: ");
        int routeId = readInt();

        Route route = adminService.findRouteById(routeId);
        if (route == null) {
            System.out.println("Route not found.");
            return;
        }

        Map<Station, LocalTime> stationTimes = new HashMap<>();

        for (Station station : route.getStations()) {
            System.out.print("Time for " + station.getName() + " (HH:mm): ");
            String timeText = scanner.nextLine();

            try {
                stationTimes.put(station, LocalTime.parse(timeText));
            } catch (Exception e) {
                System.out.println("Invalid time format. Train was not added.");
                return;
            }
        }

        System.out.print("Total seats: ");
        int totalSeats = readInt();

        Train train = new Train(nextTrainId++, name, route, stationTimes, totalSeats, 0, 0);
        System.out.println(adminService.addTrain(train));
    }

    private void updateTrain() {
        showAllTrains();

        System.out.print("Train ID: ");
        int id = readInt();

        Train train = adminService.findTrainById(id);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        System.out.print("New train name: ");
        String name = scanner.nextLine();

        System.out.print("New total seats: ");
        int totalSeats = readInt();

        train.setName(name);
        train.setTotalSeats(totalSeats);

        System.out.println(adminService.updateTrain(train));
    }

    private void removeTrain() {
        showAllTrains();

        System.out.print("Train ID to remove: ");
        int id = readInt();

        System.out.println(adminService.removeTrain(id));
    }

    private void searchRoute() {
        showAllStations();

        System.out.print("Departure station ID: ");
        int fromId = readInt();

        System.out.print("Arrival station ID: ");
        int toId = readInt();

        Station from = adminService.findStationById(fromId);
        Station to = adminService.findStationById(toId);

        if (from == null || to == null) {
            System.out.println("Invalid station selected.");
            return;
        }

        System.out.println(searchService.searchRoutes(from, to));
    }

    private void bookTickets() {
        showAllTrains();

        System.out.print("Train ID: ");
        int trainId = readInt();

        Train train = adminService.findTrainById(trainId);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        System.out.println("Stations on selected train:");
        printStationPath(train.getRoute().getStations());

        System.out.print("Departure station ID: ");
        int departureId = readInt();

        System.out.print("Arrival station ID: ");
        int arrivalId = readInt();

        Station departure = adminService.findStationById(departureId);
        Station arrival = adminService.findStationById(arrivalId);

        if (departure == null || arrival == null) {
            System.out.println("Invalid stations.");
            return;
        }

        System.out.print("Number of tickets: ");
        int count = readInt();

        System.out.print("Customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Customer email: ");
        String customerEmail = scanner.nextLine();

        System.out.print("Price per ticket: ");
        double price = readDouble();

        Customer customer = new Customer(nextCustomerId++, customerName, customerEmail);

        System.out.println(bookingService.bookTickets(
                train,
                departure,
                arrival,
                count,
                customer,
                price
        ));
    }

    private void smartRouteOptimizer() {
        showAllStations();

        System.out.print("Departure station ID: ");
        int fromId = readInt();

        System.out.print("Arrival station ID: ");
        int toId = readInt();

        Station from = adminService.findStationById(fromId);
        Station to = adminService.findStationById(toId);

        if (from == null || to == null) {
            System.out.println("Invalid stations.");
            return;
        }

        System.out.println("\nOptimizer options:");
        System.out.println("1. Show all route options");
        System.out.println("2. Find fastest route");
        System.out.println("3. Find cheapest route");
        System.out.print("Choose option: ");

        int option = readInt();

        switch (option) {
            case 1 -> System.out.println(routeOptimizerService.showAllRouteOptions(from, to));
            case 2 -> System.out.println(routeOptimizerService.findFastestRoute(from, to));
            case 3 -> System.out.println(routeOptimizerService.findCheapestRoute(from, to));
            default -> System.out.println("Invalid option.");
        }
    }

    private void showBookingsForTrain() {
        showAllTrains();

        System.out.print("Train ID: ");
        int id = readInt();

        Train train = adminService.findTrainById(id);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        List<Booking> bookings = adminService.getBookingsForTrain(train);

        if (bookings.isEmpty()) {
            System.out.println("No bookings found for this train.");
            return;
        }

        System.out.println("Bookings for train " + train.getName() + ":");

        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getId()
                    + " | Customer: " + booking.getCustomer().getName()
                    + " | Email: " + booking.getCustomer().getEmail());

            booking.getTickets().forEach(ticket ->
                    System.out.println("   Ticket ID: " + ticket.getId()
                            + " | Seat: " + ticket.getSeatNumber()
                            + " | " + ticket.getDepartureStation().getName()
                            + " -> " + ticket.getArrivalStation().getName())
            );
        }
    }

    private void reportDelay() {
        showAllTrains();

        System.out.print("Train ID: ");
        int id = readInt();

        Train train = adminService.findTrainById(id);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        System.out.print("Delay minutes: ");
        int delay = readInt();

        System.out.println(adminService.delayTrain(train, delay));
    }

    private List<Station> parseStations(String input) {
        List<Station> stations = new ArrayList<>();
        String[] ids = input.split(",");

        for (String idText : ids) {
            try {
                int id = Integer.parseInt(idText.trim());
                Station station = adminService.findStationById(id);

                if (station != null) {
                    stations.add(station);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return stations;
    }

    private void printStationPath(List<Station> stations) {
        for (int i = 0; i < stations.size(); i++) {
            System.out.print(stations.get(i).getName());

            if (i < stations.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    private int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}
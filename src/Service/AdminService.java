package Service;

import Model.Booking;
import Model.Route;
import Model.Station;
import Model.Train;
import Model.Repository.BookingRepository;
import Model.Repository.RouteRepository;
import Model.Repository.StationRepository;
import Model.Repository.TrainRepository;

import java.util.List;

public class AdminService {

    private final RouteRepository routeRepository;
    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final StationRepository stationRepository;
    private final EmailService emailService;

    public AdminService(RouteRepository routeRepository, TrainRepository trainRepository,
                        BookingRepository bookingRepository, StationRepository stationRepository,
                        EmailService emailService) {
        this.routeRepository = routeRepository;
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
        this.stationRepository = stationRepository;
        this.emailService = emailService;
    }

    public List<Station> getAllStations() {
        return stationRepository.getAllStations();
    }

    public List<Route> getAllRoutes() {
        return routeRepository.getAllRoutes();
    }

    public List<Train> getAllTrains() {
        return trainRepository.getAllTrains();
    }

    public Station findStationById(int id) {
        return stationRepository.findById(id);
    }

    public Route findRouteById(int id) {
        return routeRepository.findById(id);
    }

    public Train findTrainById(int id) {
        return trainRepository.findById(id);
    }

    public String addStation(Station station) {
        if (station == null) return "[ADMIN] addStation: FAILED (station is null)";
        if (station.getName() == null || station.getName().isBlank()) return "[ADMIN] addStation: FAILED (station name is empty)";
        if (station.getCity() == null || station.getCity().isBlank()) return "[ADMIN] addStation: FAILED (station city is empty)";

        boolean result = stationRepository.addStation(station);
        return "[ADMIN] addStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeStation(int id) {
        boolean result = stationRepository.removeStation(id);
        return "[ADMIN] removeStation(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateStation(Station station) {
        if (station == null) return "[ADMIN] updateStation: FAILED (station is null)";
        if (station.getName() == null || station.getName().isBlank()) return "[ADMIN] updateStation: FAILED (station name is empty)";
        if (station.getCity() == null || station.getCity().isBlank()) return "[ADMIN] updateStation: FAILED (station city is empty)";

        boolean result = stationRepository.updateStation(station);
        return "[ADMIN] updateStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String addRoute(Route route) {
        if (route == null) return "[ADMIN] addRoute: FAILED (route is null)";
        if (route.getName() == null || route.getName().isBlank()) return "[ADMIN] addRoute: FAILED (route name is empty)";
        if (route.getStations() == null || route.getStations().size() < 2) return "[ADMIN] addRoute: FAILED (route must contain at least two stations)";

        boolean result = routeRepository.addRoute(route);
        return "[ADMIN] addRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeRoute(int id) {
        boolean result = routeRepository.removeRoute(id);
        return "[ADMIN] removeRoute(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateRoute(Route route) {
        if (route == null) return "[ADMIN] updateRoute: FAILED (route is null)";
        if (route.getName() == null || route.getName().isBlank()) return "[ADMIN] updateRoute: FAILED (route name is empty)";
        if (route.getStations() == null || route.getStations().size() < 2) return "[ADMIN] updateRoute: FAILED (route must contain at least two stations)";

        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] updateRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String addStationToRoute(Route route, Station station) {
        if (route == null || station == null) return "[ADMIN] addStationToRoute: FAILED (null parameters)";

        if (route.getStations().stream().anyMatch(s -> s.getId() == station.getId())) {
            return "[ADMIN] addStationToRoute: Station already exists in route.";
        }

        route.getStations().add(station);
        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] addStationToRoute('" + station.getName() + "' -> '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED");
    }

    public String removeStationFromRoute(Route route, Station station) {
        if (route == null || station == null) return "[ADMIN] removeStationFromRoute: FAILED (null parameters)";

        boolean removed = route.getStations().removeIf(s -> s.getId() == station.getId());
        if (!removed) return "[ADMIN] removeStationFromRoute: Station not found in route.";

        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] removeStationFromRoute('" + station.getName() + "' from '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED");
    }

    public String addTrain(Train train) {
        if (train == null) return "[ADMIN] addTrain: FAILED (train is null)";
        if (train.getName() == null || train.getName().isBlank()) return "[ADMIN] addTrain: FAILED (train name is empty)";
        if (train.getRoute() == null) return "[ADMIN] addTrain: FAILED (train route is null)";
        if (train.getStationTimes() == null || train.getStationTimes().isEmpty()) return "[ADMIN] addTrain: FAILED (train schedule is empty)";
        if (train.getTotalSeats() <= 0) return "[ADMIN] addTrain: FAILED (total seats must be greater than 0)";
        if (train.getBookedSeats() < 0) return "[ADMIN] addTrain: FAILED (booked seats cannot be negative)";
        if (train.getBookedSeats() > train.getTotalSeats()) return "[ADMIN] addTrain: FAILED (booked seats cannot exceed total seats)";
        if (train.getDelayMinutes() < 0) return "[ADMIN] addTrain: FAILED (delay minutes cannot be negative)";

        boolean result = trainRepository.addTrain(train);
        return "[ADMIN] addTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeTrain(int id) {
        boolean result = trainRepository.removeTrain(id);
        return "[ADMIN] removeTrain(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateTrain(Train train) {
        if (train == null) return "[ADMIN] updateTrain: FAILED (train is null)";
        if (train.getName() == null || train.getName().isBlank()) return "[ADMIN] updateTrain: FAILED (train name is empty)";
        if (train.getRoute() == null) return "[ADMIN] updateTrain: FAILED (train route is null)";
        if (train.getStationTimes() == null || train.getStationTimes().isEmpty()) return "[ADMIN] updateTrain: FAILED (train schedule is empty)";
        if (train.getTotalSeats() <= 0) return "[ADMIN] updateTrain: FAILED (total seats must be greater than 0)";
        if (train.getBookedSeats() < 0) return "[ADMIN] updateTrain: FAILED (booked seats cannot be negative)";
        if (train.getBookedSeats() > train.getTotalSeats()) return "[ADMIN] updateTrain: FAILED (booked seats cannot exceed total seats)";
        if (train.getDelayMinutes() < 0) return "[ADMIN] updateTrain: FAILED (delay minutes cannot be negative)";

        boolean result = trainRepository.updateTrain(train);
        return "[ADMIN] updateTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public List<Booking> getBookingsForTrain(Train train) {
        return bookingRepository.getBookingsForTrain(train);
    }

    public String delayTrain(Train train, int delayMinutes) {
        if (train == null) return "[ADMIN] delayTrain: FAILED (train is null)";
        if (delayMinutes <= 0) return "[ADMIN] delayTrain: FAILED (delay must be greater than 0)";

        train.setDelayMinutes(train.getDelayMinutes() + delayMinutes);
        trainRepository.updateTrain(train);

        StringBuilder sb = new StringBuilder();
        sb.append("[ADMIN] Train '").append(train.getName())
                .append("' is now delayed by ")
                .append(train.getDelayMinutes())
                .append(" minute(s). Notifying customers...\n");

        List<Booking> affectedBookings = bookingRepository.getBookingsForTrain(train);

        if (affectedBookings.isEmpty()) {
            sb.append("[ADMIN] No customers to notify.\n");
        }

        for (Booking booking : affectedBookings) {
            sb.append(emailService.sendDelayNotification(
                    booking.getCustomer().getEmail(),
                    booking.getCustomer().getName(),
                    train,
                    train.getDelayMinutes()
            ));
        }

        return sb.toString();
    }
}
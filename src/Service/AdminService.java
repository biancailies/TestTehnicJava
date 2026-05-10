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

    // --- Station Management ---

    public String addStation(Station station) {
        boolean result = stationRepository.addStation(station);
        return "[ADMIN] addStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeStation(int id) {
        boolean result = stationRepository.removeStation(id);
        return "[ADMIN] removeStation(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateStation(Station station) {
        boolean result = stationRepository.updateStation(station);
        return "[ADMIN] updateStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    // --- Route Management ---

    public String addRoute(Route route) {
        boolean result = routeRepository.addRoute(route);
        return "[ADMIN] addRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeRoute(int id) {
        boolean result = routeRepository.removeRoute(id);
        return "[ADMIN] removeRoute(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateRoute(Route route) {
        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] updateRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String addStationToRoute(Route route, Station station) {
        if (route == null || station == null) return "[ADMIN] addStationToRoute: FAILED (null parameters)";
        if (route.getStations().stream().anyMatch(s -> s.getId() == station.getId())) {
            return "[ADMIN] addStationToRoute: Station '" + station.getName() + "' already exists in route '" + route.getName() + "'.";
        }
        route.getStations().add(station);
        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] addStationToRoute('" + station.getName() + "' -> '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED");
    }

    public String removeStationFromRoute(Route route, Station station) {
        if (route == null || station == null) return "[ADMIN] removeStationFromRoute: FAILED (null parameters)";
        boolean removed = route.getStations().removeIf(s -> s.getId() == station.getId());
        if (!removed) {
            return "[ADMIN] removeStationFromRoute: Station '" + station.getName() + "' not found in route '" + route.getName() + "'.";
        }
        boolean result = routeRepository.updateRoute(route);
        return "[ADMIN] removeStationFromRoute('" + station.getName() + "' from '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED");
    }

    // --- Train Management ---

    public String addTrain(Train train) {
        boolean result = trainRepository.addTrain(train);
        return "[ADMIN] addTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)");
    }

    public String removeTrain(int id) {
        boolean result = trainRepository.removeTrain(id);
        return "[ADMIN] removeTrain(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    public String updateTrain(Train train) {
        boolean result = trainRepository.updateTrain(train);
        return "[ADMIN] updateTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)");
    }

    // --- Booking & Delays ---

    public List<Booking> getBookingsForTrain(Train train) {
        return bookingRepository.getBookingsForTrain(train);
    }

    public String delayTrain(Train train, int delayMinutes) {
        if (train == null) return "[ADMIN] delayTrain: FAILED (train is null)";

        train.setDelayMinutes(train.getDelayMinutes() + delayMinutes);
        trainRepository.updateTrain(train);
        
        StringBuilder sb = new StringBuilder();
        sb.append("[ADMIN] Train '").append(train.getName()).append("' is now delayed by ").append(train.getDelayMinutes()).append(" minute(s). Notifying customers...\n");

        List<Booking> affectedBookings = bookingRepository.getBookingsForTrain(train);
        if (affectedBookings.isEmpty()) {
            sb.append("[ADMIN] No customers to notify.\n");
        }
        for (Booking booking : affectedBookings) {
            String emailStr = emailService.sendDelayNotification(booking.getCustomer().getEmail(), booking.getCustomer().getName(), train, train.getDelayMinutes());
            sb.append(emailStr);
        }
        return sb.toString();
    }
}


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

    public boolean addStation(Station station) {
        boolean result = stationRepository.addStation(station);
        System.out.println("[ADMIN] addStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)"));
        return result;
    }

    public boolean removeStation(int id) {
        boolean result = stationRepository.removeStation(id);
        System.out.println("[ADMIN] removeStation(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    public boolean updateStation(Station station) {
        boolean result = stationRepository.updateStation(station);
        System.out.println("[ADMIN] updateStation('" + station.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    // --- Route Management ---

    public boolean addRoute(Route route) {
        boolean result = routeRepository.addRoute(route);
        System.out.println("[ADMIN] addRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)"));
        return result;
    }

    public boolean removeRoute(int id) {
        boolean result = routeRepository.removeRoute(id);
        System.out.println("[ADMIN] removeRoute(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    public boolean updateRoute(Route route) {
        boolean result = routeRepository.updateRoute(route);
        System.out.println("[ADMIN] updateRoute('" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    public boolean addStationToRoute(Route route, Station station) {
        if (route == null || station == null) return false;
        if (route.getStations().stream().anyMatch(s -> s.getId() == station.getId())) {
            System.out.println("[ADMIN] addStationToRoute: Station '" + station.getName() + "' already exists in route '" + route.getName() + "'.");
            return false;
        }
        route.getStations().add(station);
        boolean result = routeRepository.updateRoute(route);
        System.out.println("[ADMIN] addStationToRoute('" + station.getName() + "' -> '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED"));
        return result;
    }

    public boolean removeStationFromRoute(Route route, Station station) {
        if (route == null || station == null) return false;
        boolean removed = route.getStations().removeIf(s -> s.getId() == station.getId());
        if (!removed) {
            System.out.println("[ADMIN] removeStationFromRoute: Station '" + station.getName() + "' not found in route '" + route.getName() + "'.");
            return false;
        }
        boolean result = routeRepository.updateRoute(route);
        System.out.println("[ADMIN] removeStationFromRoute('" + station.getName() + "' from '" + route.getName() + "'): " + (result ? "SUCCESS" : "FAILED"));
        return result;
    }

    // --- Train Management ---

    public boolean addTrain(Train train) {
        boolean result = trainRepository.addTrain(train);
        System.out.println("[ADMIN] addTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (duplicate ID)"));
        return result;
    }

    public boolean removeTrain(int id) {
        boolean result = trainRepository.removeTrain(id);
        System.out.println("[ADMIN] removeTrain(id=" + id + "): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    public boolean updateTrain(Train train) {
        boolean result = trainRepository.updateTrain(train);
        System.out.println("[ADMIN] updateTrain('" + train.getName() + "'): " + (result ? "SUCCESS" : "FAILED (not found)"));
        return result;
    }

    // --- Booking & Delays ---

    public List<Booking> getBookingsForTrain(Train train) {
        return bookingRepository.getBookingsForTrain(train);
    }

    public void delayTrain(Train train, int delayMinutes) {
        if (train == null) return;

        train.setDelayMinutes(train.getDelayMinutes() + delayMinutes);
        trainRepository.updateTrain(train);
        System.out.println("[ADMIN] Train '" + train.getName() + "' is now delayed by " + train.getDelayMinutes() + " minute(s). Notifying customers...");

        List<Booking> affectedBookings = bookingRepository.getBookingsForTrain(train);
        if (affectedBookings.isEmpty()) {
            System.out.println("[ADMIN] No customers to notify.");
        }
        for (Booking booking : affectedBookings) {
            emailService.sendDelayNotification(booking.getCustomer().getEmail(), booking.getCustomer().getName(), train, train.getDelayMinutes());
        }
    }
}


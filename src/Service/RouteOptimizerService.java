package Service;

import Model.RouteOption;
import Model.Station;
import Model.Train;
import Model.Repository.TrainRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RouteOptimizerService {

    private final TrainRepository trainRepository;
    private static final int MIN_CHANGEOVER_MINUTES = 5;
    private static final double PRICE_PER_STATION = 20.0;

    public RouteOptimizerService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public String findFastestRoute(Station from, Station to) {
        if (from == null || to == null) {
            return "[OPTIMIZER] Error: departure and arrival stations cannot be null.\n";
        }

        List<RouteOption> options = findAllRouteOptions(from, to);

        if (options.isEmpty()) {
            return "[OPTIMIZER] No route found from " + from.getName() + " to " + to.getName() + ".\n";
        }

        RouteOption best = options.stream()
                .min(Comparator.comparing(RouteOption::getTotalDuration)
                        .thenComparing(RouteOption::getChanges)
                        .thenComparing(RouteOption::getTotalPrice))
                .orElse(null);

        return formatBestRoute("FASTEST", best);
    }

    public String findCheapestRoute(Station from, Station to) {
        if (from == null || to == null) {
            return "[OPTIMIZER] Error: departure and arrival stations cannot be null.\n";
        }

        List<RouteOption> options = findAllRouteOptions(from, to);

        if (options.isEmpty()) {
            return "[OPTIMIZER] No route found from " + from.getName() + " to " + to.getName() + ".\n";
        }

        RouteOption best = options.stream()
                .min(Comparator.comparing(RouteOption::getTotalPrice)
                        .thenComparing(RouteOption::getTotalDuration)
                        .thenComparing(RouteOption::getChanges))
                .orElse(null);

        return formatBestRoute("CHEAPEST", best);
    }

    public String showAllRouteOptions(Station from, Station to) {
        if (from == null || to == null) {
            return "[OPTIMIZER] Error: departure and arrival stations cannot be null.\n";
        }

        List<RouteOption> options = findAllRouteOptions(from, to);

        if (options.isEmpty()) {
            return "[OPTIMIZER] No route options found from " + from.getName() + " to " + to.getName() + ".\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[OPTIMIZER] All route options from ")
                .append(from.getName())
                .append(" to ")
                .append(to.getName())
                .append(":\n");

        int index = 1;
        for (RouteOption option : options) {
            sb.append("\nOption ").append(index++).append(":\n");
            sb.append(formatRouteOption(option));
        }

        return sb.toString();
    }

    private List<RouteOption> findAllRouteOptions(Station from, Station to) {
        List<RouteOption> options = new ArrayList<>();
        List<Train> trains = trainRepository.getAllTrains();

        for (Train train : trains) {
            if (isValidDirectSegment(train, from, to)) {
                options.add(createDirectOption(train, from, to));
            }
        }

        for (Train firstTrain : trains) {
            if (!containsStation(firstTrain, from)) {
                continue;
            }

            for (Train secondTrain : trains) {
                if (firstTrain.getId() == secondTrain.getId()) {
                    continue;
                }

                if (!containsStation(secondTrain, to)) {
                    continue;
                }

                for (Station middleStation : firstTrain.getRoute().getStations()) {
                    if (!isValidDirectSegment(firstTrain, from, middleStation)) {
                        continue;
                    }

                    if (!isValidDirectSegment(secondTrain, middleStation, to)) {
                        continue;
                    }

                    LocalTime arrivalAtMiddle = firstTrain.getStationTimes().get(middleStation);
                    LocalTime departureFromMiddle = secondTrain.getStationTimes().get(middleStation);

                    if (isValidChangeover(arrivalAtMiddle, departureFromMiddle)) {
                        options.add(createChangeoverOption(firstTrain, secondTrain, from, middleStation, to));
                    }
                }
            }
        }

        return options;
    }

    private RouteOption createDirectOption(Train train, Station from, Station to) {
        List<Train> trains = new ArrayList<>();
        trains.add(train);

        List<Station> stations = new ArrayList<>();
        stations.add(from);
        stations.add(to);

        LocalTime departureTime = train.getStationTimes().get(from);
        LocalTime arrivalTime = train.getStationTimes().get(to);

        double price = calculatePrice(train, from, to);

        return new RouteOption(trains, stations, departureTime, arrivalTime, price);
    }

    private RouteOption createChangeoverOption(Train firstTrain, Train secondTrain,
                                               Station from, Station middle, Station to) {
        List<Train> trains = new ArrayList<>();
        trains.add(firstTrain);
        trains.add(secondTrain);

        List<Station> stations = new ArrayList<>();
        stations.add(from);
        stations.add(middle);
        stations.add(to);

        LocalTime departureTime = firstTrain.getStationTimes().get(from);
        LocalTime arrivalTime = secondTrain.getStationTimes().get(to);

        double price = calculatePrice(firstTrain, from, middle)
                + calculatePrice(secondTrain, middle, to);

        return new RouteOption(trains, stations, departureTime, arrivalTime, price);
    }

    private boolean isValidDirectSegment(Train train, Station from, Station to) {
        if (train == null || from == null || to == null) {
            return false;
        }

        if (train.getRoute() == null || train.getRoute().getStations() == null) {
            return false;
        }

        if (train.getStationTimes() == null) {
            return false;
        }

        if (!train.getStationTimes().containsKey(from) || !train.getStationTimes().containsKey(to)) {
            return false;
        }

        int fromIndex = getStationIndex(train, from);
        int toIndex = getStationIndex(train, to);

        return fromIndex != -1 && toIndex != -1 && fromIndex < toIndex;
    }

    private boolean containsStation(Train train, Station station) {
        if (train == null || station == null || train.getRoute() == null || train.getRoute().getStations() == null) {
            return false;
        }

        for (Station current : train.getRoute().getStations()) {
            if (current.getId() == station.getId()) {
                return true;
            }
        }

        return false;
    }

    private int getStationIndex(Train train, Station station) {
        List<Station> stations = train.getRoute().getStations();

        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getId() == station.getId()) {
                return i;
            }
        }

        return -1;
    }

    private boolean isValidChangeover(LocalTime arrivalAtMiddle, LocalTime departureFromMiddle) {
        if (arrivalAtMiddle == null || departureFromMiddle == null) {
            return false;
        }

        LocalTime minimumDeparture = arrivalAtMiddle.plusMinutes(MIN_CHANGEOVER_MINUTES);

        return departureFromMiddle.equals(minimumDeparture) || departureFromMiddle.isAfter(minimumDeparture);
    }

    private double calculatePrice(Train train, Station from, Station to) {
        int fromIndex = getStationIndex(train, from);
        int toIndex = getStationIndex(train, to);

        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            return 0.0;
        }

        int stationDistance = toIndex - fromIndex;
        return stationDistance * PRICE_PER_STATION;
    }

    private String formatBestRoute(String criteria, RouteOption option) {
        StringBuilder sb = new StringBuilder();

        sb.append("[OPTIMIZER] Optimization criteria: ")
                .append(criteria)
                .append("\n");

        sb.append("Best route found:\n");
        sb.append(formatRouteOption(option));

        return sb.toString();
    }

    private String formatRouteOption(RouteOption option) {
        StringBuilder sb = new StringBuilder();

        long hours = option.getTotalDuration().toHours();
        long minutes = option.getTotalDuration().toMinutes() % 60;

        sb.append("Stations: ")
                .append(option.getStationPathAsString())
                .append("\n");

        sb.append("Trains: ")
                .append(option.getTrainPathAsString())
                .append("\n");

        sb.append("Departure: ")
                .append(option.getDepartureTime())
                .append("\n");

        sb.append("Arrival: ")
                .append(option.getArrivalTime())
                .append("\n");

        sb.append("Total duration: ")
                .append(hours)
                .append("h ")
                .append(minutes)
                .append("m\n");

        sb.append("Changes: ")
                .append(option.getChanges())
                .append("\n");

        sb.append("Estimated price: $")
                .append(option.getTotalPrice())
                .append("\n");

        return sb.toString();
    }
}
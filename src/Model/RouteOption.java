package Model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RouteOption {
    private List<Train> trains;
    private List<Station> stations;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double totalPrice;
    private int changes;

    public RouteOption() {
        this.trains = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    public RouteOption(List<Train> trains, List<Station> stations,
                       LocalTime departureTime, LocalTime arrivalTime,
                       double totalPrice) {
        this.trains = trains;
        this.stations = stations;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalPrice = totalPrice;
        this.changes = Math.max(0, trains.size() - 1);
    }

    public List<Train> getTrains() {
        return trains;
    }

    public void setTrains(List<Train> trains) {
        this.trains = trains;
        this.changes = Math.max(0, trains.size() - 1);
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getChanges() {
        return changes;
    }

    public Duration getTotalDuration() {
        if (departureTime == null || arrivalTime == null) {
            return Duration.ZERO;
        }

        if (arrivalTime.isBefore(departureTime)) {
            return Duration.between(departureTime, arrivalTime.plusHours(24));
        }

        return Duration.between(departureTime, arrivalTime);
    }

    public String getStationPathAsString() {
        if (stations == null || stations.isEmpty()) {
            return "(no stations)";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stations.size(); i++) {
            sb.append(stations.get(i).getName());
            if (i < stations.size() - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }

    public String getTrainPathAsString() {
        if (trains == null || trains.isEmpty()) {
            return "(no trains)";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trains.size(); i++) {
            sb.append(trains.get(i).getName());
            if (i < trains.size() - 1) {
                sb.append(" + ");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        long hours = getTotalDuration().toHours();
        long minutes = getTotalDuration().toMinutes() % 60;

        return "RouteOption{" +
                "trains=" + getTrainPathAsString() +
                ", stations=" + getStationPathAsString() +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", duration=" + hours + "h " + minutes + "m" +
                ", totalPrice=" + totalPrice +
                ", changes=" + changes +
                '}';
    }
}
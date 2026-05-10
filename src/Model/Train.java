package Model;

import java.time.LocalTime;
import java.util.Map;

public class Train {
    private int id;
    private String name;
    private Route route;
    private Map<Station, LocalTime> stationTimes;
    private int totalSeats;
    private int bookedSeats;
    private int delayMinutes;

    public Train() {}

    public Train(int id, String name, Route route, Map<Station, LocalTime> stationTimes, int totalSeats, int bookedSeats, int delayMinutes) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.stationTimes = stationTimes;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
        this.delayMinutes = delayMinutes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Route getRoute() {
        return route;
    }

    public Map<Station, LocalTime> getStationTimes() {
        return stationTimes;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setStationTimes(Map<Station, LocalTime> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }
}

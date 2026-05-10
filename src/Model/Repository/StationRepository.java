package Model.Repository;

import Model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StationRepository {

    private final List<Station> stationList = new ArrayList<>();

    public boolean addStation(Station station) {
        if (station == null || existsById(station.getId())) {
            return false;
        }

        stationList.add(station);
        return true;
    }

    public boolean removeStation(int id) {
        return stationList.removeIf(station -> station.getId() == id);
    }

    public boolean updateStation(Station station) {
        if (station == null) {
            return false;
        }

        for (int i = 0; i < stationList.size(); i++) {
            if (stationList.get(i).getId() == station.getId()) {
                stationList.set(i, station);
                return true;
            }
        }

        return false;
    }

    public Station findById(int id) {
        for (Station station : stationList) {
            if (station.getId() == id) {
                return station;
            }
        }

        return null;
    }

    public Station findByName(String name) {
        for (Station station : stationList) {
            if (Objects.equals(station.getName(), name)) {
                return station;
            }
        }

        return null;
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public List<Station> getAllStations() {
        return new ArrayList<>(stationList);
    }
}
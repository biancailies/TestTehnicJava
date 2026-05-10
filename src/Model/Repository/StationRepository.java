package Model.Repository;

import Model.Station;

import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private List<Station> stationList = new ArrayList<>();

    public boolean addStation(Station station) {
        if (station == null)
            return false;

        for (Station s : stationList) {
            if (s.getId() == station.getId())
                return false;
        }

        stationList.add(station);
        return true;
    }

    public boolean removeStation(int id) {
        for (Station s : stationList) {
            if (s.getId() == id) {
                stationList.remove(s);
                return true;
            }
        }
        return false;
    }

    public boolean updateStation(Station station) {
        if (station == null)
            return false;

        for (Station s : stationList) {
            if (s.getId() == station.getId()) {
                stationList.remove(s);
                stationList.add(station);
                return true;
            }
        }
        return false;
    }

    public Station findById(int id) {
        for (Station s : stationList) {
            if (s.getId() == id)
                return s;
        }
        return null;
    }

    public Station findByName(String name) {
        for (Station s : stationList) {
            if (s.getName().equals(name))
                return s;
        }
        return null;
    }

    public List<Station> getAllStations() {
        return stationList;
    }
}

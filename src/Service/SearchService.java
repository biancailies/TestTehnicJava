package Service;

import Model.Route;
import Model.Station;
import Model.Train;
import Model.Repository.TrainRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final TrainRepository trainRepository;

    public SearchService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void searchRoutes(Station from, Station to) {
        System.out.println("Searching routes from " + from.getName() + " to " + to.getName() + "...");
        boolean found = false;

        List<Train> allTrains = trainRepository.getAllTrains();

        // 1. Check for direct routes
        for (Train train : allTrains) {
            if (isDirectRoute(train, from, to)) {
                LocalTime dep = train.getStationTimes().get(from);
                LocalTime arr = train.getStationTimes().get(to);
                System.out.println("-> Direct Train: " + train.getName() + " | Departs: " + dep + " | Arrives: " + arr);
                found = true;
            }
        }

        // 2. Check for 1-stop changeovers
        for (Train train1 : allTrains) {
            if (!train1.getStationTimes().containsKey(from)) continue;
            
            for (Train train2 : allTrains) {
                if (train1.getId() == train2.getId()) continue;
                if (!train2.getStationTimes().containsKey(to)) continue;

                // Find a common station
                for (Station mid : train1.getRoute().getStations()) {
                    if (isDirectRoute(train1, from, mid) && isDirectRoute(train2, mid, to)) {
                        LocalTime arrMid = train1.getStationTimes().get(mid);
                        LocalTime depMid = train2.getStationTimes().get(mid);

                        // Allow at least 5 mins for changeover
                        if (arrMid.plusMinutes(5).isBefore(depMid) || arrMid.plusMinutes(5).equals(depMid)) {
                            System.out.println("-> Changeover at " + mid.getName() + ":");
                            System.out.println("   Train 1: " + train1.getName() + " | Departs " + from.getName() + ": " + train1.getStationTimes().get(from) + " | Arrives " + mid.getName() + ": " + arrMid);
                            System.out.println("   Train 2: " + train2.getName() + " | Departs " + mid.getName() + ": " + depMid + " | Arrives " + to.getName() + ": " + train2.getStationTimes().get(to));
                            found = true;
                        }
                    }
                }
            }
        }

        if (!found) {
            System.out.println("Error: No possible link found between " + from.getName() + " and " + to.getName() + ".");
        }
    }

    private boolean isDirectRoute(Train train, Station from, Station to) {
        if (!train.getStationTimes().containsKey(from) || !train.getStationTimes().containsKey(to)) {
            return false;
        }

        int fromIndex = getStationIndex(train.getRoute(), from);
        int toIndex = getStationIndex(train.getRoute(), to);

        return fromIndex != -1 && toIndex != -1 && fromIndex < toIndex;
    }

    private int getStationIndex(Route route, Station station) {
        List<Station> stations = route.getStations();
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getId() == station.getId()) {
                return i;
            }
        }
        return -1;
    }
}

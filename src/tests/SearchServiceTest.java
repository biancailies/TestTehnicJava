package tests;

import Model.Route;
import Model.Station;
import Model.Train;
import Model.Repository.TrainRepository;
import Service.SearchService;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SearchServiceTest {

    @Test
    public void testDirectRouteSearch() {

        TrainRepository trainRepository = new TrainRepository();

        Station s1 = new Station(1, "Bucuresti", "Bucuresti");
        Station s2 = new Station(2, "Brasov", "Brasov");

        Route route = new Route(
                1,
                "Route",
                new ArrayList<>() {{
                    add(s1);
                    add(s2);
                }}
        );

        Map<Station, LocalTime> times = new HashMap<>();
        times.put(s1, LocalTime.of(8, 0));
        times.put(s2, LocalTime.of(10, 0));

        Train train = new Train(
                1,
                "IR100",
                route,
                times,
                100,
                0,
                0
        );

        trainRepository.addTrain(train);

        SearchService searchService =
                new SearchService(trainRepository);

        String result =
                searchService.searchRoutes(s1, s2);

        assertTrue(result.contains("Direct Train"));
    }
}
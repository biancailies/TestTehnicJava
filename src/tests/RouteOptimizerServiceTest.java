package tests;

import Model.Route;
import Model.Station;
import Model.Train;
import Model.Repository.TrainRepository;
import Service.RouteOptimizerService;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RouteOptimizerServiceTest {

    @Test
    public void testFastestRoute() {

        TrainRepository repo = new TrainRepository();

        Station s1 = new Station(1, "A", "A");
        Station s2 = new Station(2, "B", "B");

        Route route = new Route(
                1,
                "R1",
                new ArrayList<>() {{
                    add(s1);
                    add(s2);
                }}
        );

        Map<Station, LocalTime> times = new HashMap<>();
        times.put(s1, LocalTime.of(8, 0));
        times.put(s2, LocalTime.of(9, 0));

        Train train = new Train(
                1,
                "IR1",
                route,
                times,
                100,
                0,
                0
        );

        repo.addTrain(train);

        RouteOptimizerService optimizer =
                new RouteOptimizerService(repo);

        String result =
                optimizer.findFastestRoute(s1, s2);

        assertTrue(result.contains("FASTEST"));
    }
}
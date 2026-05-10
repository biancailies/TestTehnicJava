package Model.Repository;

import Model.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteRepository {

    private final List<Route> routeList = new ArrayList<>();

    public boolean addRoute(Route route) {
        if (route == null || existsById(route.getId())) {
            return false;
        }

        routeList.add(route);
        return true;
    }

    public boolean removeRoute(int id) {
        return routeList.removeIf(route -> route.getId() == id);
    }

    public boolean updateRoute(Route route) {
        if (route == null) {
            return false;
        }

        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getId() == route.getId()) {
                routeList.set(i, route);
                return true;
            }
        }

        return false;
    }

    public Route findById(int id) {
        for (Route route : routeList) {
            if (route.getId() == id) {
                return route;
            }
        }

        return null;
    }

    public Route findByName(String name) {
        for (Route route : routeList) {
            if (Objects.equals(route.getName(), name)) {
                return route;
            }
        }

        return null;
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public List<Route> getAllRoutes() {
        return new ArrayList<>(routeList);
    }
}
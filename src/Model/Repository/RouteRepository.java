package Model.Repository;

import Model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private List<Route> routeList = new ArrayList<>();

    public boolean addRoute(Route route){
        if(route == null)
            return false;

        for(Route r : routeList) {
            if (r.getId() == route.getId())
                return false;
        }

        routeList.add(route);
        return true;

    }

    public boolean removeRoute(int id) {
        return routeList.removeIf(r -> r.getId() == id);
    }

    public boolean updateRoute(Route route) {
        if (route == null) return false;
        boolean removed = routeList.removeIf(r -> r.getId() == route.getId());
        if (removed) {
            routeList.add(route);
            return true;
        }
        return false;
    }

    public Route findById(int id){

        for(Route r : routeList){
            if(r.getId() == id){
                return r;
            }
        }

        return null;
    }

    public Route findByName(String name){
        for(Route r : routeList){
            if(r.getName().equals(name)){
                return r;
            }
        }

        return null;
    }

    public List<Route> getAllRoutes(){
        return routeList;
    }

}

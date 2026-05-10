package Model.Repository;

import Model.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainRepository {

    private List<Train> trainList = new ArrayList<>();

    public boolean addTrain(Train train){
        if(train == null)
            return false;

        for(Train t : trainList) {
            if (t.getId() == train.getId())
                return false;
        }

        trainList.add(train);
        return true;
    }

    public boolean removeTrain(int id) {
        return trainList.removeIf(t -> t.getId() == id);
    }

    public boolean updateTrain(Train train) {
        if (train == null) return false;
        boolean removed = trainList.removeIf(t -> t.getId() == train.getId());
        if (removed) {
            trainList.add(train);
            return true;
        }
        return false;
    }

    public Train findById(int id){
        for(Train t : trainList){
            if(t.getId() == id){
                return t;
            }
        }
        return null;
    }

    public List<Train> getAllTrains(){
        return trainList;
    }

}

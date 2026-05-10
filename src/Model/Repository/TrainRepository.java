package Model.Repository;

import Model.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainRepository {

    private final List<Train> trainList = new ArrayList<>();

    public boolean addTrain(Train train) {
        if (train == null || existsById(train.getId())) {
            return false;
        }

        trainList.add(train);
        return true;
    }

    public boolean removeTrain(int id) {
        return trainList.removeIf(train -> train.getId() == id);
    }

    public boolean updateTrain(Train train) {
        if (train == null) {
            return false;
        }

        for (int i = 0; i < trainList.size(); i++) {
            if (trainList.get(i).getId() == train.getId()) {
                trainList.set(i, train);
                return true;
            }
        }

        return false;
    }

    public Train findById(int id) {
        for (Train train : trainList) {
            if (train.getId() == id) {
                return train;
            }
        }

        return null;
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public List<Train> getAllTrains() {
        return new ArrayList<>(trainList);
    }
}
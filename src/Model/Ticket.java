package Model;

public class Ticket {
    private int id;
    private Train train;
    private Station departureStation;
    private Station arrivalStation;
    private int seatNumber;
    private Double price;

    public Ticket () {}

    public Ticket(int id, Train train, Station departureStation, Station arrivalStation, int seatNumber, Double price) {
        this.id = id;
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Train getTrain() {
        return train;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

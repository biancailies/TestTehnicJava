package Model.Repository;

import Model.Booking;
import Model.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository {

    private List<Booking> bookingList = new ArrayList<>();

    public boolean addBooking(Booking booking){
        if(booking == null)
            return false;

        for(Booking b : bookingList) {
            if (b.getId() == booking.getId())
                return false;
        }

        bookingList.add(booking);
        return true;
    }

    public boolean removeBooking(int id) {
        return bookingList.removeIf(b -> b.getId() == id);
    }

    public Booking findById(int id){
        for(Booking b : bookingList){
            if(b.getId() == id){
                return b;
            }
        }
        return null;
    }

    public List<Booking> getAllBookings(){
        return bookingList;
    }

    public List<Booking> getBookingsForTrain(Train train) {
        if (train == null) return new ArrayList<>();
        return bookingList.stream()
                .filter(b -> b.getTickets() != null && 
                             !b.getTickets().isEmpty() && 
                             b.getTickets().get(0).getTrain() != null && 
                             b.getTickets().get(0).getTrain().getId() == train.getId())
                .collect(Collectors.toList());
    }

}

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

    public boolean removeBooking(int id){
        for(Booking b : bookingList) {
            if (b.getId() == id){
                bookingList.remove(b);
                return true;
            }
        }
        return false;
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
        if(train == null) return new ArrayList<>();
        return bookingList.stream()
                .filter(b -> b.getTicket() != null && 
                             b.getTicket().getTrain() != null && 
                             b.getTicket().getTrain().getId() == train.getId())
                .collect(Collectors.toList());
    }

}

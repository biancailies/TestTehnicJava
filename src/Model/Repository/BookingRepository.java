package Model.Repository;

import Model.Booking;
import Model.Ticket;
import Model.Train;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private final List<Booking> bookingList = new ArrayList<>();

    public boolean addBooking(Booking booking) {
        if (booking == null || existsById(booking.getId())) {
            return false;
        }

        bookingList.add(booking);
        return true;
    }

    public boolean removeBooking(int id) {
        return bookingList.removeIf(booking -> booking.getId() == id);
    }

    public Booking findById(int id) {
        for (Booking booking : bookingList) {
            if (booking.getId() == id) {
                return booking;
            }
        }

        return null;
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingList);
    }

    public List<Booking> getBookingsForTrain(Train train) {
        List<Booking> result = new ArrayList<>();

        if (train == null) {
            return result;
        }

        for (Booking booking : bookingList) {
            for (Ticket ticket : booking.getTickets()) {
                if (ticket.getTrain().getId() == train.getId()) {
                    result.add(booking);
                    break;
                }
            }
        }

        return result;
    }
}
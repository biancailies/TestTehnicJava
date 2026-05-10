package Service;

import Model.Booking;
import Model.Customer;
import Model.Station;
import Model.Ticket;
import Model.Train;
import Model.Repository.BookingRepository;
import Model.Repository.TrainRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final EmailService emailService;
    private int nextBookingId = 1;
    private int nextTicketId = 1;

    public BookingService(BookingRepository bookingRepository, TrainRepository trainRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.trainRepository = trainRepository;
        this.emailService = emailService;
    }

    public boolean bookTickets(Train train, Station departure, Station arrival, int count, 
                               Customer customer, Double pricePerTicket) {
        if (train == null || count <= 0 || customer == null) return false;

        if (train.getTotalSeats() - train.getBookedSeats() < count) {
            System.out.println("Error: Overbooking prevented. Train " + train.getName() + " does not have enough seats.");
            return false;
        }

        List<Booking> currentBookings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int seatNumber = train.getBookedSeats() + 1;
            train.setBookedSeats(seatNumber);
            
            Ticket ticket = new Ticket(nextTicketId++, train, departure, arrival, seatNumber, pricePerTicket);
            Booking booking = new Booking(nextBookingId++, customer, ticket, LocalDateTime.now());
            
            bookingRepository.addBooking(booking);
            currentBookings.add(booking);
        }

        trainRepository.updateTrain(train);

        for (Booking booking : currentBookings) {
            emailService.sendBookingConfirmation(booking);
        }

        return true;
    }
}

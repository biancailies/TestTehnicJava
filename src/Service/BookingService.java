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

    public String bookTickets(Train train, Station departure, Station arrival, int count, 
                               Customer customer, Double pricePerTicket) {
        if (departure == null || arrival == null) {
            return "Error: Departure and arrival stations cannot be null.\n";
        }

        if (pricePerTicket == null || pricePerTicket < 0) {
            return "Error: Invalid ticket price.\n";
        }

        if (train == null || count <= 0 || customer == null) {
            return "Error: Invalid parameters for booking.\n";
        }

        if (train.getRoute() == null || train.getStationTimes() == null) {
            return "Error: Train route or schedule is invalid.\n";
        }

        // Strict Validations
        if (!train.getStationTimes().containsKey(departure) || !train.getStationTimes().containsKey(arrival)) {
            return "Error: Departure or Arrival station is not on the train's route.\n";
        }
        
        java.time.LocalTime depTime = train.getStationTimes().get(departure);
        java.time.LocalTime arrTime = train.getStationTimes().get(arrival);
        
        if (!depTime.isBefore(arrTime)) {
            return "Error: Invalid route direction. Departure must be before arrival.\n";
        }

        if (train.getTotalSeats() - train.getBookedSeats() < count) {
            return "Error: Overbooking prevented. Train " + train.getName() + " does not have enough seats.\n";
        }

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int seatNumber = train.getBookedSeats() + 1;
            train.setBookedSeats(seatNumber);
            Ticket ticket = new Ticket(nextTicketId++, train, departure, arrival, seatNumber, pricePerTicket);
            tickets.add(ticket);
        }

        Booking booking = new Booking(nextBookingId++, customer, tickets, LocalDateTime.now());
        bookingRepository.addBooking(booking);
        trainRepository.updateTrain(train);

        return emailService.sendBookingConfirmation(booking);
    }
}

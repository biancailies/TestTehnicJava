package Model;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private int id;
    private Customer customer;
    private List<Ticket> tickets;
    private LocalDateTime bookingDate;

    public Booking() {}

    public Booking(int id, Customer customer, List<Ticket> tickets, LocalDateTime bookingDate) {
        this.id = id;
        this.customer = customer;
        this.tickets = tickets;
        this.bookingDate = bookingDate;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}


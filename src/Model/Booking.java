package Model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private Customer customer;
    private Ticket ticket;
    private LocalDateTime bookingDate;

    public Booking() {}

    public Booking(int id, Customer customer, Ticket ticket, LocalDateTime bookingDate) {
        this.id = id;
        this.customer = customer;
        this.ticket = ticket;
        this.bookingDate = bookingDate;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ticket getTicket() {
        return ticket;
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

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}


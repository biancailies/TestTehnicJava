package Service;

import Model.Booking;
import Model.Train;

public class EmailService {

    public void sendBookingConfirmation(Booking booking) {
        String email = booking.getCustomer().getEmail();
        System.out.println("--------------------------------------------------");
        System.out.println("EMAIL TO: " + email);
        System.out.println("SUBJECT: Booking Confirmation - Ticket #" + booking.getTicket().getId());
        System.out.println("BODY: Dear " + booking.getCustomer().getName() + ",");
        System.out.println("Your booking is confirmed.");
        System.out.println("Train: " + booking.getTicket().getTrain().getName());
        System.out.println("Route: " + booking.getTicket().getDepartureStation().getName()
                + " -> " + booking.getTicket().getArrivalStation().getName());
        System.out.println("Seat Number: " + booking.getTicket().getSeatNumber());
        System.out.println("Price: $" + booking.getTicket().getPrice());
        System.out.println("Booking Date: " + booking.getBookingDate().toLocalDate()
                + " at " + booking.getBookingDate().toLocalTime().withNano(0));
        System.out.println("Thank you for choosing us!");
        System.out.println("--------------------------------------------------");
        System.out.println("[NOTE: Email delivery is simulated via console output.]");
    }

    public void sendDelayNotification(String email, String customerName, Train train, int delayMinutes) {
        System.out.println("--------------------------------------------------");
        System.out.println("EMAIL TO: " + email);
        System.out.println("SUBJECT: Train Delay Notification - " + train.getName());
        System.out.println("BODY: Dear " + customerName + ",");
        System.out.println("We regret to inform you that train " + train.getName()
                + " is delayed by " + delayMinutes + " minutes.");
        System.out.println("We apologize for the inconvenience.");
        System.out.println("--------------------------------------------------");
        System.out.println("[NOTE: Email delivery is simulated via console output.]");
    }
}


package Service;

import Model.Booking;
import Model.Train;

public class EmailService {

    public String sendBookingConfirmation(Booking booking) {
        StringBuilder sb = new StringBuilder();
        String email = booking.getCustomer().getEmail();
        sb.append("--------------------------------------------------\n");
        sb.append("EMAIL TO: ").append(email).append("\n");
        sb.append("SUBJECT: Booking Confirmation - Booking #").append(booking.getId()).append("\n");
        sb.append("BODY: Dear ").append(booking.getCustomer().getName()).append(",\n");
        sb.append("Your booking is confirmed for ").append(booking.getTickets().size()).append(" ticket(s).\n");
        
        for (Model.Ticket t : booking.getTickets()) {
            sb.append("\n  - Ticket #").append(t.getId())
              .append(" | Train: ").append(t.getTrain().getName())
              .append(" | Route: ").append(t.getDepartureStation().getName()).append(" -> ").append(t.getArrivalStation().getName())
              .append(" | Seat: ").append(t.getSeatNumber())
              .append(" | Price: $").append(t.getPrice()).append("\n");
        }
        
        sb.append("\nBooking Date: ").append(booking.getBookingDate().toLocalDate())
          .append(" at ").append(booking.getBookingDate().toLocalTime().withNano(0)).append("\n");
        sb.append("Thank you for choosing us!\n");
        sb.append("--------------------------------------------------\n");
        sb.append("[NOTE: Email delivery is simulated via string output.]\n");
        return sb.toString();
    }

    public String sendDelayNotification(String email, String customerName, Train train, int delayMinutes) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------\n");
        sb.append("EMAIL TO: ").append(email).append("\n");
        sb.append("SUBJECT: Train Delay Notification - ").append(train.getName()).append("\n");
        sb.append("BODY: Dear ").append(customerName).append(",\n");
        sb.append("We regret to inform you that train ").append(train.getName())
          .append(" is delayed by ").append(delayMinutes).append(" minutes.\n");
        sb.append("We apologize for the inconvenience.\n");
        sb.append("--------------------------------------------------\n");
        sb.append("[NOTE: Email delivery is simulated via string output.]\n");
        return sb.toString();
    }
}


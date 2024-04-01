package sofe3980;

public class Ticket {
    private int bookingId; // Identifier for the booking this ticket belongs to
    private Flight flight; // The flight this ticket is for
    private String passengerName; // Name of the passenger

    // Constructor
    public Ticket(int bookingId, Flight flight, String passengerName) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passengerName = passengerName;
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getPassengerName() {
        return passengerName;
    }

    // Setters
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    // Assuming a meaningful toString implementation to provide ticket details
    // @Override
    // public String toString() {
    // return "Ticket{" +
    // "bookingId=" + bookingId +
    // ", flight=" + flight +
    // ", passengerName='" + passengerName + '\'' +
    // '}';
    // }
    @Override
    public String toString() {
        return "Ticket for " + passengerName + "\n" +
                "Booking ID: " + bookingId + "\n" +
                "Flight: " + flight.getFlightId() + "\n" +
                "From: " + flight.getDepartureLocation() + " at " + flight.getDepartureTime() + "\n" +
                "To: " + flight.getDestinationLocation() + " at " + flight.getArrivalTime() + "\n" +
                "Price: $" + flight.getPrice();
    }
}

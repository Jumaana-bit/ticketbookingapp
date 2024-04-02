package sofe3980;

public class Ticket {
    private int bookingId; // Identifier for the booking this ticket belongs to
    private Flight flight; // The flight this ticket is for
    private String passengerName; // Name of the passenger
    private String ticketNumber; // Unique ticket number
    private String seatNumber; // Seat number assigned to the ticket
    private String flightDetails; // Field to store flight details

    // Default constructor
    public Ticket() {
    }

    // Constructor
    public Ticket(int bookingId, Flight flight, String passengerName, String ticketNumber, String seatNumber) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passengerName = passengerName;
        this.ticketNumber = ticketNumber;
        this.seatNumber = seatNumber;
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

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getFlightDetails() {
        return flightDetails;
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

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setFlightDetails(String flightDetails) {
        this.flightDetails = flightDetails;
    }

    @Override
    public String toString() {
        return "Ticket for " + passengerName + "\n" +
                "Booking ID: " + bookingId + "\n" +
                "Ticket Number: " + ticketNumber + "\n" +
                "Seat: " + seatNumber + "\n" +
                "Flight: " + flight.getFlightId() + "\n" +
                "From: " + flight.getDepartureLocation() + " at " + flight.getDepartureTime() + "\n" +
                "To: " + flight.getDestinationLocation() + " at " + flight.getArrivalTime() + "\n" +
                "Price: $" + flight.getPrice();
    }
}

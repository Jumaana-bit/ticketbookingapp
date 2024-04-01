package sofe3980;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingManager {

    private List<Booking> bookings;
    private int nextBookingId;

    public BookingManager() {
        this.bookings = new ArrayList<>();
        this.nextBookingId = 1; // Start with booking ID 1 then increment from here
    }

    /**
     * Creates a new booking with the provided user, list of flights, and booking
     * type,
     * and adds it to the list of bookings.
     * 
     * @param user        The user making the booking.
     * @param flights     The list of flights included in the booking.
     * @param bookingType The type of booking (one-way, round-trip).
     * @return The created Booking object.
     */
    public Booking createBooking(User user, List<Flight> flights, String bookingType) {
        int bookingId = nextBookingId++; // auto increment ID value
        Booking newBooking = new Booking(bookingId, user, flights, bookingType);
        // set bookingId, calculate totalPrice, generate tickets, etc. all here
        bookings.add(newBooking);
        return newBooking;
    }

    /**
     * Cancels an existing booking by removing it from the list of bookings.
     * 
     * @param bookingId The ID of the booking to cancel.
     * @return true if the booking was successfully canceled, false otherwise.
     */
    public boolean cancelBooking(int bookingId) {
        return bookings.removeIf(booking -> booking.getBookingId() == bookingId);
    }

    /**
     * Retrieves a booking by its ID.
     * 
     * @param bookingId The ID of the booking to retrieve.
     * @return An Optional containing the Booking if found, or an empty Optional
     *         otherwise.
     */
    public Optional<Booking> getBookingById(int bookingId) {
        return null;
    }

    /**
     * Checks if the proposed itinerary forms a cyclic trip, returning to the same airport.
     * @param itinerary List of flights representing the proposed itinerary.
     * @return true if the itinerary is cyclic, false otherwise.
     */
    public boolean isCyclicItinerary(List<Flight> itinerary) {
        return false;
    }
}

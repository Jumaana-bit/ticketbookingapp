package sofe3980;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BookingManager {

    private Map<Integer, Booking> bookingsMap; // HashMap to store bookings by their ID
    private int nextBookingId;

    public BookingManager() {
        this.bookingsMap = new HashMap<>();
        this.nextBookingId = 1; // Start with booking ID 1 then increment from here
    }

    /**
     * Creates a new booking with the provided user, list of flights, and booking
     * type, and adds it to the list of bookings.
     * 
     * @param user        The user making the booking.
     * @param flights     The list of flights included in the booking.
     * @param bookingType The type of booking (one-way, round-trip).
     * @return The created Booking object.
     */
    public Booking createBooking(User user, List<Flight> flights, String bookingType) {
        // Check if the itinerary is cyclic
        if (isCyclicItinerary(flights)) {
            // Handle the cyclic itinerary case. Options include returning null, throwing an exception, or other handling.
            System.out.println("Cannot create booking: Itinerary is cyclic.");
            return null; // Example: return null if the itinerary is cyclic.
        }
    
        // If the itinerary is not cyclic, proceed to create the booking
        int bookingId = nextBookingId++; // Auto increment ID value
        Booking newBooking = new Booking(bookingId, user, flights, bookingType);
        bookingsMap.put(bookingId, newBooking); // Store the new booking in the map
        return newBooking;
    }
    
    /**
     * Cancels an existing booking by removing it from the list of bookings.
     * 
     * @param bookingId The ID of the booking to cancel.
     * @return true if the booking was successfully canceled, false otherwise.
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingsMap.get(bookingId);
        if (booking != null && "active".equals(booking.getStatus())) {
            booking.cancel(); // This should set the booking status to "canceled"
            return true;
        }
        return false;
    }

    /**
     * Retrieves a booking by its ID.
     * 
     * @param bookingId The ID of the booking to retrieve.
     * @return An Optional containing the Booking if found, or an empty Optional
     *         otherwise.
     */
    public Optional<Booking> getBookingById(int bookingId) {
        return Optional.ofNullable(bookingsMap.get(bookingId));
    }

    /**
     * A cyclic itinerary in the context of multi-stop flights (excluding valid
     * round-trips) occurs when there's an unnecessary return to a previously
     * visited city that doesn't serve as the final destination back to the starting
     * point. For instance, a trip from A to B, then back to A, and then onward to C
     * is cyclic because the return to A before continuing to C is unnecessary.
     * 
     * @param itinerary List of flights representing the proposed itinerary.
     * @return true if the itinerary is cyclic, false otherwise.
     */
    public boolean isCyclicItinerary(List<Flight> itinerary) {
        Set<String> visitedCities = new HashSet<>();
        visitedCities.add(itinerary.get(0).getDepartureLocation()); // Add the starting city

        for (int i = 0; i < itinerary.size(); i++) {
            Flight flight = itinerary.get(i);
            String destinationCity = flight.getDestinationLocation();

            // If the destination city has been visited before, it's potentially cyclic
            if (visitedCities.contains(destinationCity)) {
                // If it's the final leg and the destination is the starting city, it's a valid
                // round-trip
                if (i == itinerary.size() - 1 && destinationCity.equals(itinerary.get(0).getDepartureLocation())) {
                    return false; // It's a valid round-trip, not cyclic
                } else {
                    return true; // Found a cycle
                }
            }
            visitedCities.add(destinationCity); // Add the destination city as visited
        }

        return false; // No cyclic pattern found
    }

}

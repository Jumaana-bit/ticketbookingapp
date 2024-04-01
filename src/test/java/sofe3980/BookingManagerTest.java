package sofe3980;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookingManagerTest {

    private BookingManager bookingManager;
    private User mockUser;
    private List<Flight> mockFlights;

    @Before
    public void setUp() throws Exception {
        bookingManager = new BookingManager();

        // Initialize a mock user
        mockUser = new User(1, "John Doe", "johndoe@example.com", "password", LocalDate.of(1990, 1, 1), "AB1234567");

        // Initialize a list of mock flights
        mockFlights = Arrays.asList(
                new Flight(1, LocalDateTime.of(2024, 4, 10, 8, 0), LocalDateTime.of(2024, 4, 10, 10, 0), "CityA",
                        "CityB", 200.00),
                new Flight(2, LocalDateTime.of(2024, 4, 12, 15, 0), LocalDateTime.of(2024, 4, 12, 17, 0), "CityB",
                        "CityC", 250.00));
    }

    @Test
    public void testCreateBooking() {
        // Test for creating a one-way booking
        Booking oneWayBooking = bookingManager.createBooking(mockUser, mockFlights, "one-way");
        assertNotNull("One-way booking should be created", oneWayBooking);
        assertEquals("One-way booking should have the correct user", mockUser, oneWayBooking.getUser());
        assertEquals("One-way booking should have the correct flights", mockFlights, oneWayBooking.getFlights());
        assertEquals("One-way booking should have the correct booking type", "one-way", oneWayBooking.getBookingType());

        // Prepare return flight for the round-trip booking
        Flight returnFlight = new Flight(3, LocalDateTime.of(2024, 4, 14, 10, 0), LocalDateTime.of(2024, 4, 14, 12, 0),
                "CityC", "CityA", 200.00);
        List<Flight> roundTripFlights = new ArrayList<>(mockFlights);
        roundTripFlights.add(returnFlight); // Add the return flight to create a round-trip

        // Test for creating a round-trip booking
        Booking roundTripBooking = bookingManager.createBooking(mockUser, roundTripFlights, "round-trip");
        assertNotNull("Round-trip booking should be created", roundTripBooking);
        assertEquals("Round-trip booking should have the correct user", mockUser, roundTripBooking.getUser());
        assertEquals("Round-trip booking should have the correct flights", roundTripFlights,
                roundTripBooking.getFlights());
        assertEquals("Round-trip booking should have the correct booking type", "round-trip",
                roundTripBooking.getBookingType());
    }

    @Test
    public void testCancelBooking() {
        // Create a booking to cancel
        Booking bookingToCancel = bookingManager.createBooking(mockUser, mockFlights, "one-way");
        int bookingIdToCancel = bookingToCancel.getBookingId();

        // Attempt to cancel the booking
        boolean cancellationResult = bookingManager.cancelBooking(bookingIdToCancel);
        assertTrue("Booking should be canceled successfully", cancellationResult);

        // Retrieve the booking again and check its status
        Optional<Booking> canceledBooking = bookingManager.getBookingById(bookingIdToCancel);
        assertTrue("Canceled booking should be retrievable", canceledBooking.isPresent());
        assertEquals("Canceled booking should be marked as canceled", "canceled", canceledBooking.get().getStatus());
    }

    @Test
    public void testGetBookingById() {
        // Step 1: Create a booking
        Booking expectedBooking = bookingManager.createBooking(mockUser, mockFlights, "one-way");
        int bookingId = expectedBooking.getBookingId(); // Retrieve the ID of the created booking

        // Step 2: Retrieve the booking by ID
        Optional<Booking> retrievedBookingOpt = bookingManager.getBookingById(bookingId);

        // Step 3: Verify the retrieved booking
        assertTrue("Booking should be found by ID", retrievedBookingOpt.isPresent());

        Booking retrievedBooking = retrievedBookingOpt.get();
        assertNotNull("Retrieved booking should not be null", retrievedBooking);
        assertEquals("Retrieved booking should have the correct user", expectedBooking.getUser(),
                retrievedBooking.getUser());
        assertEquals("Retrieved booking should have the correct flights", expectedBooking.getFlights(),
                retrievedBooking.getFlights());
        assertEquals("Retrieved booking should have the correct booking type", expectedBooking.getBookingType(),
                retrievedBooking.getBookingType());
    }

    @Test
    public void testIsCyclicItinerary() {
        // Valid multi-stop itinerary (non-cyclic)
        Flight flight1 = new Flight(1, LocalDateTime.of(2024, 4, 10, 8, 0), LocalDateTime.of(2024, 4, 10, 10, 0), "CityA", "CityB", 200.00);
        Flight flight2 = new Flight(2, LocalDateTime.of(2024, 4, 12, 15, 0), LocalDateTime.of(2024, 4, 12, 17, 0), "CityB", "CityC", 250.00);
        Flight flight3 = new Flight(3, LocalDateTime.of(2024, 4, 14, 10, 0), LocalDateTime.of(2024, 4, 14, 12, 0), "CityC", "CityD", 200.00);
        List<Flight> nonCyclicItinerary = Arrays.asList(flight1, flight2, flight3);
    
        // Cyclic itinerary due to unnecessary return to a previously visited city (CityA) before final destination
        Flight flight4 = new Flight(4, LocalDateTime.of(2024, 4, 16, 10, 0), LocalDateTime.of(2024, 4, 16, 12, 0), "CityB", "CityA", 200.00);
        Flight flight5 = new Flight(5, LocalDateTime.of(2024, 4, 18, 10, 0), LocalDateTime.of(2024, 4, 18, 12, 0), "CityA", "CityC", 250.00);
        List<Flight> cyclicItinerary = Arrays.asList(flight1, flight4, flight5); // A->B->A->C is cyclic due to return to A
    
        // Test non-cyclic itinerary
        assertFalse("Itinerary should not be cyclic", bookingManager.isCyclicItinerary(nonCyclicItinerary));
    
        // Test cyclic itinerary
        assertTrue("Itinerary should be cyclic", bookingManager.isCyclicItinerary(cyclicItinerary));
    }
    

}

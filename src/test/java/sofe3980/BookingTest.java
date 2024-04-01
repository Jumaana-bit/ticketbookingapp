package sofe3980;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// @RunWith(Parameterized.class)
public class BookingTest {

    private User user;
    private List<Flight> oneWayFlights; // Class-level variable for one-way flights
    private List<Flight> roundTripFlights; // Class-level variable for round-trip flights
    private Booking oneWayBooking;
    private Booking roundTripBooking;

    @Before
    public void setUp() {
        user = new User(1, "John Doe", "john.doe@example.com", "password123", LocalDate.of(1990, 1, 1), "AB1234567");

        // Initialize the list of flights for a one-way trip
        oneWayFlights = Arrays.asList(
                new Flight(1, LocalDateTime.of(2024, 4, 10, 8, 0), LocalDateTime.of(2024, 4, 10, 10, 0), "CityA",
                        "CityB", 200.00),
                new Flight(2, LocalDateTime.of(2024, 4, 12, 15, 0), LocalDateTime.of(2024, 4, 12, 17, 0), "CityB",
                        "CityC", 250.00));

        // Initialize a return flight for a round-trip
        Flight returnFlight = new Flight(3, LocalDateTime.of(2024, 4, 14, 10, 0), LocalDateTime.of(2024, 4, 14, 12, 0),
                "CityC", "CityA", 200.00);

        // Combine one-way flights and the return flight for the round-trip
        roundTripFlights = new ArrayList<>(oneWayFlights);
        roundTripFlights.add(returnFlight);

        oneWayBooking = new Booking(1, user, oneWayFlights, "one-way");
        roundTripBooking = new Booking(2, user, roundTripFlights, "round-trip");
    }

    @Test
    public void testGenerateTickets() {
        // Test for one-way booking ticket generation
        String ticketInfoOneWay = oneWayBooking.generateTickets();
        assertNotNull("Tickets for one-way booking should not be null", oneWayBooking.getTickets());
        assertFalse("Tickets list for one-way booking should not be empty", oneWayBooking.getTickets().isEmpty());
        assertEquals("The number of tickets for one-way booking should match the number of flights",
                oneWayBooking.getFlights().size(), oneWayBooking.getTickets().size());
        assertNotNull("Generated ticket info for one-way booking should not be null", ticketInfoOneWay);

        // Reset the tickets list for the one-way booking before testing the round-trip
        // to avoid counting tickets twice
        oneWayBooking.setTickets(new ArrayList<>());

        // Test for round-trip booking ticket generation
        String ticketInfoRoundTrip = roundTripBooking.generateTickets();
        assertNotNull("Tickets for round-trip booking should not be null", roundTripBooking.getTickets());
        assertFalse("Tickets list for round-trip booking should not be empty", roundTripBooking.getTickets().isEmpty());
        assertEquals("The number of tickets for round-trip booking should match the number of flights",
                roundTripBooking.getFlights().size(), roundTripBooking.getTickets().size());
        assertNotNull("Generated ticket info for round-trip booking should not be null", ticketInfoRoundTrip);
    }

    @Test
    public void testCalculateTotalPrice() {
        // Test for a one-way booking
        double calculatedTotalPriceOneWay = oneWayBooking.calculateTotalPrice();
        // Sum the prices of all one-way flights
        double expectedTotalPriceOneWay = oneWayFlights.stream().mapToDouble(Flight::getPrice).sum();
        assertEquals("The calculated total price for a one-way booking should match the sum of flight prices",
                expectedTotalPriceOneWay, calculatedTotalPriceOneWay, 0.01);

        // Test for a round-trip booking
        double calculatedTotalPriceRoundTrip = roundTripBooking.calculateTotalPrice();
        // Sum the prices of all round-trip flights
        double expectedTotalPriceRoundTrip = roundTripFlights.stream().mapToDouble(Flight::getPrice).sum();

        assertEquals("The calculated total price for a round-trip booking should match the sum of all flight prices",
                expectedTotalPriceRoundTrip, calculatedTotalPriceRoundTrip, 0.01);
    }

}
